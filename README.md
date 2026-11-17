# Harmoni Auth Service

Authentication and access-control (RBAC) service for the Harmoni POS platform.
It issues JWTs, refreshes them, and manages users, roles, permissions and scoped
role assignments. The service is reached through the API gateway, which validates
the JWT and forwards the caller identity to the other services.

## Tech stack

| Concern | Choice |
| --- | --- |
| Language | Java 17+ |
| Framework | Spring Boot 4.0.7 (Web MVC, Security, Validation) |
| Persistence | MyBatis + MySQL (`db-harmoni-auth`) |
| Tokens | JJWT 0.12.5 (HMAC-SHA) |
| Password hashing | BCrypt |
| Build | Maven (`./mvnw` or `mvn`) |

## Architecture

The code follows a ports-and-adapters (hexagonal) layout:

```
com.harmoni.auth
├── adapter
│   ├── in                     # Driving adapters (REST controllers)
│   │   ├── AuthController
│   │   ├── AuthoritiesController
│   │   ├── RoleController
│   │   ├── PermissionController
│   │   ├── UserController
│   │   └── UserRoleController
│   └── out                    # Driven adapters
│       ├── JwtTokenProviderAdapter
│       ├── PasswordEncoderAdapter
│       └── *RepositoryAdapter
├── application
│   ├── port
│   │   ├── in                 # Use-case interfaces
│   │   └── out                # JwtTokenProvider, PasswordEncoder
│   ├── usecase                # Use-case implementations, RbacGuard, RbacAuditRecorder
│   └── exception              # Application exceptions
├── domain
│   └── model                  # User, Role, Permission, RefreshToken, UserRoleKey, ...
├── infrastructure
│   └── persistence.mybatis
│       ├── mapper             # MyBatis mapper interfaces
│       └── repository         # Repository interfaces + MyBatis-backed implementations
├── web
│   ├── dto                    # Request/response transport objects
│   ├── filter                 # JwtAuthoritiesFilter, HeaderAuthoritiesFilter
│   └── handler                # @RestControllerAdvice exception handlers
├── component
│   └── JwtUtil                # Token generation/parsing
└── config
    ├── SecurityConfig         # Filter chain, password encoder
    ├── HexagonalConfig        # Bean wiring
    └── DbAuthSQLConfig        # DataSource, SqlSessionFactory, transaction manager
```

Dependency direction is inward: controllers and adapters depend on the
application use cases, which depend on the domain model and the output ports.
`HexagonalConfig` wires the concrete adapters into the use cases.

## Domain model

| Table | Purpose |
| --- | --- |
| `users` | Account identity, BCrypt password, enabled flag |
| `roles` | Role name, `scope_type` (`BRAND`/`CHAIN`/`STORE`), unique `code`, `system` flag |
| `permissions` | Permission name (e.g. `menu:view`), module/action/category/label metadata |
| `user_roles` | Scoped assignment: `(user_id, role_id, scope_type, scope_id)` |
| `role_permissions` | Many-to-many role ↔ permission |
| `refresh_tokens` | Persisted refresh token with `expires_at` |
| `rbac_audit` | Audit trail of access-control changes (`actor`, `action`, `target`, `detail`) |

`scope_type`/`scope_id` on `user_roles` is what scopes a role to a brand, chain or
store. `GLOBAL` is used for platform administrators.

## Authentication flow

1. `POST /api/v1/auth/login` with `{ "username", "password" }`.
2. `AuthenticateUserUseCaseImpl` loads the user, verifies the BCrypt password,
   loads the user's scoped assignments, roles and permissions, and asks
   `JwtTokenProvider` for a token pair.
3. The access token carries these claims:
   - `sub` — username
   - `userId`
   - `roles` — role objects (name, code, scope type)
   - `permissions` — flattened permission names
   - `scopes` — `[{ scopeType, scopeId }]` assignments
4. `POST /api/v1/auth/refresh-token` with the raw refresh token string reloads
   the current roles/permissions (so a refreshed token reflects current grants)
   and returns a new pair.

Access tokens live for `harmoni.auth.jwt.expired.time` (default 24h); refresh
tokens for `harmoni.auth.jwt.refresh.expired.time` (default 7 days).

## Authorization model

- **Permissions** are the unit of enforcement. Controllers use
  `@PreAuthorize("hasAnyAuthority('...')")`.
- **Roles** bundle permissions and declare a default `scope_type`.
- **Assignments** bind a role to a user at a scope (`BRAND`, `CHAIN`, `STORE`,
  `GLOBAL`). The gateway forwards these as `X-Scopes` (`TYPE:id,...`) so the
  downstream services can restrict data access.
- **Least privilege** — `RbacGuard` prevents an operator from granting a
  permission they do not hold, and `UserManagementUseCaseImpl` restricts
  assigning the `OWN` (Pemilik) and `SYS` (System Administrator) roles to a
  system administrator.
- **Audit** — every role, permission and assignment change is written to
  `rbac_audit` by `RbacAuditRecorder`, attributed to the signed-in operator.

## Security filters

`SecurityConfig` permits `/api/v1/**` at the URL level and relies on method
security for authorization. Two filters populate the `SecurityContext`:

1. `JwtAuthoritiesFilter` — validates a `Bearer` token and adds the token's
   permissions and `ROLE_<code>`/`ROLE_<name>` authorities. This covers
   service-to-service calls that carry only the token.
2. `HeaderAuthoritiesFilter` — when no authentication is present, builds it from
   the gateway headers `X-Username`, `X-Roles` and `X-Permissions`.

The JWT filter runs first, so a valid bearer token takes precedence over the
forwarded headers.

## API reference

### Authentication

| Method | Path | Description |
| --- | --- | --- |
| `POST` | `/api/v1/auth/login` | Authenticate and return `{ accessToken, refreshToken }` |
| `POST` | `/api/v1/auth/refresh-token` | Exchange a refresh token for a new pair |
| `GET` | `/api/v1/auth/me/authorities` | Effective roles, role codes, permissions and scopes of the caller |

### Roles

| Method | Path | Authority |
| --- | --- | --- |
| `GET` | `/api/v1/auth/roles` | `rbac:manage`, `rbac:read`, `employee:view` |
| `GET` | `/api/v1/auth/roles/{id}` | `rbac:manage`, `rbac:read`, `employee:view` |
| `POST` | `/api/v1/auth/roles` | `rbac:manage`, `rbac:write` |
| `PUT` | `/api/v1/auth/roles/{id}` | `rbac:manage`, `rbac:write` |
| `DELETE` | `/api/v1/auth/roles/{id}` | `rbac:manage`, `rbac:delete` |
| `PUT` | `/api/v1/auth/roles/{id}/permissions` | `rbac:manage`, `rbac:write` |
| `POST` | `/api/v1/auth/roles/{id}/duplicate` | `rbac:manage`, `rbac:write` |

System roles (`system = true`) cannot be deleted, and their name, scope type and
code cannot be changed. The `OWN` and `SYS` roles are hidden from operators who
are not system administrators.

### Permissions

| Method | Path | Authority |
| --- | --- | --- |
| `GET` | `/api/v1/auth/permissions` | `rbac:manage`, `rbac:read`, `employee:view` |
| `GET` | `/api/v1/auth/permissions/{id}` | `rbac:manage`, `rbac:read`, `employee:view` |
| `POST` | `/api/v1/auth/permissions` | `rbac:manage` |
| `PUT` | `/api/v1/auth/permissions/{id}` | `rbac:manage` |
| `DELETE` | `/api/v1/auth/permissions/{id}` | `rbac:manage` |

### Users

| Method | Path | Authority |
| --- | --- | --- |
| `POST` | `/api/v1/user` | `user:write`, `employee:edit`, `rbac:manage` |
| `PUT` | `/api/v1/user/{username}` | `user:write`, `employee:edit`, `rbac:manage` |
| `DELETE` | `/api/v1/user/{username}` | `user:delete`, `employee:edit`, `rbac:manage` |

### Scoped assignments

| Method | Path | Authority |
| --- | --- | --- |
| `GET` | `/api/v1/auth/users/{userId}/assignments` | `employee:view`, `rbac:manage`, `user:read` |
| `GET` | `/api/v1/auth/users/ids?roleId=&scopeType=` | `employee:view`, `rbac:manage`, `user:read` |
| `PUT` | `/api/v1/auth/users/{userId}/assignments` | `employee:edit`, `rbac:manage`, `user:write` |
| `POST` | `/api/v1/auth/assignments/batch` | `employee:view`, `rbac:manage`, `user:read` |

Responses use `CommonDto` (`timeStamp`, `httpStatus`, `data`, `error`). Errors are
mapped by the `web/handler` advices: `400` for validation/business input, `401`
for authentication failures, `403` for denied `@PreAuthorize` checks, `404` for
missing resources, and `500` for unhandled exceptions.

## Configuration

Key properties in `src/main/resources/application.properties`:

```properties
server.port=8081
server.address=127.0.0.1

spring.datasource.auth.mysql.jdbc-url=jdbc:mysql://localhost:3306/db-harmoni-auth
spring.datasource.auth.mysql.username=pos-user
spring.datasource.auth.mysql.password=forlearn27!

harmoni.auth.jwt.secret=<base64 HMAC key>
harmoni.auth.jwt.expired.time=86400000          # 24h
harmoni.auth.jwt.refresh.expired.time=604800000 # 7d
```

`harmoni.auth.jwt.secret` must be the same key the gateway uses to validate
tokens. The checked-in values are development defaults; override them per
environment.

## Running locally

```bash
# 1. Create the schema and seed data in MySQL (db-harmoni-auth)
# 2. Build
./mvnw -DskipTests package

# 3. Run
java -jar target/auth-0.0.1-SNAPSHOT.jar
# or
./mvnw spring-boot:run
```

The service listens on `127.0.0.1:8081`. In the full stack it is reached through
the gateway on port `8080`; the dashboard and other services call the gateway,
not this service directly.

## Testing

```bash
./mvnw test
```

Unit tests cover the use cases, adapters, DTOs, exception handlers and `JwtUtil`.
The Spring context test (`AuthApplicationTests`) runs against the in-memory H2
schema in `src/test/resources/schema.sql`.

## Notes

- The gateway is responsible for validating the JWT for external traffic and for
  injecting `X-Username`, `X-Roles`, `X-Permissions` and `X-Scopes`. This service
  still validates bearer tokens itself so internal calls remain protected.
- `Role.id`, `User.id` and the related foreign keys are `BIGINT`/`Long`; keep the
  MyBatis XML `jdbcType`/`parameterType` in sync with the Java types to avoid
  `ClassCastException` at runtime.
- The `ARCHITECTURE_SUMMARY.md`, `HEXAGONAL_ARCHITECTURE.md` and
  `FINAL_SUMMARY.md` files are historical refactoring notes; this README reflects
  the current code.
