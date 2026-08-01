# Hexagonal Architecture Refactoring - Summary

## Overview
This document summarizes the refactoring of the authentication service to implement hexagonal architecture (also known as ports and adapters pattern). The goal was to separate concerns, improve testability, and make the system more maintainable by establishing clear boundaries between layers.

## Architectural Layers

### 1. Domain Layer (Core)
**Location**: `src/main/java/com/harmoni/auth/domain/`
**Purpose**: Contains pure business logic with no dependencies on external frameworks or libraries.

**Components**:
- **Entities**: User, Role, Permission, RefreshToken (with behavior and state)
- **Domain Services**: AuthenticationDomainService (contains business rules)
- **Repository Ports**: Interfaces defining data access contracts
  - UserRepository, RoleRepository, PermissionRepository, RefreshTokenRepository
- **Domain Exceptions**: Domain-specific exception types

### 2. Application Layer
**Location**: `src/main/java/com/harmoni/auth/application/`
**Purpose**: Contains use cases that orchestrate domain objects to implement application-specific business rules.

**Components**:
- **Use Cases**: 
  - AuthenticateUserUseCase: Handles user authentication
  - RefreshTokenUseCase: Handles token refresh operations
- **Ports (Interfaces)**:
  - Input Ports: Define what the application offers (AuthenticateUserUseCase, RefreshTokenUseCase)
  - Output Ports: Define what the application needs from outside (JwtTokenProvider, PasswordEncoder)
- **DTOs**: Data transfer objects for communication between layers
- **Application Exceptions**: Application-specific exception types

### 3. Adapter Layer (Infrastructure)
**Location**: `src/main/java/com/harmoni/auth/adapter/`
**Purpose**: Contains implementations of ports that connect to external systems (databases, web frameworks, etc.).

**Components**:
- **Primary Adapters (Driving Adapters)**:
  - AuthController: Handles HTTP requests and delegates to use cases
- **Secondary Adapters (Driven Adapters)**:
  - Repository Adapters: Implement domain repository ports using MyBatis
    - UserRepositoryAdapter
    - RoleRepositoryAdapter
    - PermissionRepositoryAdapter
    - RefreshTokenRepositoryAdapter
  - Infrastructure Adapters:
    - JwtTokenProviderAdapter: Wraps existing JwtUtil component
    - PasswordEncoderAdapter: Wraps Spring Security PasswordEncoder

### 4. Configuration Layer
**Location**: `src/main/java/com/harmoni/auth/config/`
**Purpose**: Wires all components together using dependency injection.

**Component**:
- HexagonalConfig: Spring configuration that creates and connects all components

## Key Benefits Achieved

### 1. Separation of Concerns
- Each layer has a single, well-defined responsibility
- Business logic is isolated from infrastructure concerns
- Changes in one layer have minimal impact on others

### 2. Improved Testability
- Domain logic can be tested without Spring, databases, or HTTP frameworks
- Use cases can be tested with mock repositories
- Adapters can be tested in isolation

**Example Domain Test**:
```java
// Test authentication logic without any framework dependencies
AuthenticationDomainService authService = new AuthenticationDomainService(mockPasswordEncoder);
User user = new User("test", "test@test.com", "hashedPassword");
// ... test authentication logic
```

### 3. Framework Independence
- Core business logic contains no Spring annotations
- Could potentially switch to Micronaut, Quarkus, or plain Java with minimal changes
- Database technology (MyBatis) is isolated in adapter layer

### 4. Flexibility and Extensibility
- New database technologies: Implement new repository adapters
- Different authentication methods: Add new use cases or modify existing ones
- Different delivery mechanisms: Add new adapters (gRPC, GraphQL, message queues)
- Different token implementations: Replace JwtTokenProviderAdapter

## Dependency Flow
```
HTTP Request → AuthController (Adapter In)
               → AuthenticateUserUseCase (Application - uses input port)
               → AuthenticationDomainService (Domain Service)
               → UserRepository, RoleRepository (Output Ports)
               → UserRepositoryAdapter, RoleRepositoryAdapter (Adapters Out)
               → UserMapper, RoleMapper (MyBatis)
               → Database
```

## Example Flow: User Authentication

1. **HTTP Request** arrives at `/api/v1/auth/login`
2. **AuthController** (primary adapter) receives request
3. Controller calls `authenticateUserUseCase.authenticate(username, password)`
4. **AuthenticateUserUseCaseImpl** (application service) executes:
   - Calls `userRepository.findByUsername(username)` (output port)
   - Validates password using `passwordEncoder.matches()` (output port)
   - Loads user roles via `roleRepository.findRolesByUserId()`
   - Generates tokens using `jwtTokenProvider.generateToken()` (output port)
5. **Repository Adapters** (secondary adapters) implement the ports using MyBatis
6. **Infrastructure Adapters** wrap existing utilities (JwtUtil, PasswordEncoder)
7. **Response** flows back through the same layers to the HTTP client

## Files Created

### Domain Layer
- `domain/model/User.java`, `Role.java`, `Permission.java`, `RefreshToken.java`
- `domain/service/AuthenticationDomainService.java`
- `domain/repository/UserRepository.java`, `RoleRepository.java`, etc.
- `domain/exception/DomainException.java`, `AuthenticationException.java`

### Application Layer
- `application/usecase/AuthenticateUserUseCaseImpl.java`, `RefreshTokenUseCaseImpl.java`
- `application/port/in/AuthenticateUserUseCase.java`, `RefreshTokenUseCase.java`
- `application/port/out/JwtTokenProvider.java`, `PasswordEncoder.java`
- `application/dto/LoginDto.java`, `, `AuthResponseDto.java`
- `application/exception/AuthenticationException.java`

### Adapter Layer
- `adapter/in/AuthController.java`
- `adapter/out/UserRepositoryAdapter.java`, `RoleRepositoryAdapter.java`, etc.
- `adapter/out/JwtTokenProviderAdapter.java`, `PasswordEncoderAdapter.java`

### Adapter Layer
- `adapter/in/AuthController.java`
- `adapter/out/UserRepositoryAdapter.java`, `RoleRepositoryAdapter.java`, etc.
- `adapter/out/JwtTokenProviderAdapter.java`, `PasswordEncoderAdapter.java`

### Configuration
- `config/HexagonalConfig.java`

## How This Improves the Original Code

**Before**: Mixed concerns - controllers directly used services that accessed mappers, business logic scattered across layers, tight coupling to Spring and MyBatis.

**After**: 
- Clear layer separation with well-defined interfaces
- Business logic can evolve independently of infrastructure
- Easy to test business rules in isolation
- Simple to swap out implementations (different database, auth mechanism, etc.)
- Explicit dependencies make the system easier to understand and maintain

This refactoring provides a solid foundation for future enhancements while maintaining backward compatibility with existing APIs.