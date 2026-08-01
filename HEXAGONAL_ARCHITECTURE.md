# Hexagonal Architecture Refactoring Guide

This document explains the refactoring of the authentication service to use hexagonal architecture (also known as ports and adapters pattern).

## Overview

Hexagonal architecture is an architectural pattern that aims to create loosely coupled application components that can be easily connected to their software environment by means of ports and adapters. This makes the system more maintainable, testable, and flexible.

## Structure

The refactored application follows this structure:

```
com.harmoni.auth
├── adapter                    # Adapter layer (infrastructure)
│   ├── in                     # Primary adapters (driving adapters)
│   │   └── AuthController.java    # HTTP controller (receives requests)
│   └── out                    # Secondary adapters (driven adapters)
│       ├── JwtTokenProviderAdapter.java  # JWT token adapter
│       ├── PasswordEncoderAdapter.java   # Password encoder adapter
│       ├── PermissionRepositoryAdapter.java # RepositoryAdapter.java        # Repository adapters
│       └── ...
├── application                # Application layer (use cases)
│   ├── dto                    # Data transfer objects
│   ├── exception              # Application exceptions
│   ├── port                   # Ports (interfaces)
│   │   ├── in                 # Input ports (use case interfaces)
│   │   └── out                # Output ports (service interfaces)
│   └── usecase                # Use case implementations
├── domain                     # Domain layer (core business logic)
│   ├── exception              # Domain exceptions
│   ├── model                  # Domain entities
│   ├── repository             # Repository interfaces (ports)
│   └── service                # Domain services
└── config                     # Configuration
    └── HexagonalConfig.java   # Dependency injection configuration
```

## Key Principles Applied

1. **Dependency Inversion**: Dependencies point inward - outer layers depend on inner layers, but inner layers have no knowledge of outer layers.

2. **Ports and Adapters**: 
   - Ports are interfaces that define how the core application interacts with the outside world
   - Adapters implement these ports to connect to specific technologies (databases, web frameworks, etc.)

3. **Separation of Concerns**: Each layer has a distinct responsibility:
   - Domain: Contains business logic and rules
   - Application: Contains use cases that orchestrate domain objects
   - Adapter: Contains technology-specific code (web controllers, database mappers)

## Layers Explained

### Domain Layer (Core)
Contains:
- **Entities**: User, Role, Permission, RefreshToken (business objects with identity and behavior)
- **Value Objects**: (not explicitly shown but could be added for things like Email, Password)
- **Domain Services**: AuthenticationDomainService (business logic that doesn't belong to a single entity)
- **Repository Interfaces**: Ports that define how data is accessed (UserRepository, RoleRepository, etc.)
- **Domain Exceptions**: Exceptions specific to the business domain

### Application Layer
Contains:
- **Use Cases**: Application services that implement specific user goals (AuthenticateUserUseCase, RefreshTokenUseCase)
- **DTOs**: Data transfer objects for communication between layers
- **Ports**: Interfaces that define what the application layer needs from the outside world
  - Input Ports: Define what actions can be performed on the application (use case interfaces)
  - Output Ports: Define what services the application needs (JwtTokenProvider, PasswordEncoder, etc.)

### Adapter Layer (Infrastructure)
Contains:
- **Primary Adapters (Driving Adapters)**: 
  - AuthController: Handles HTTP requests and directs them to use cases
- **Secondary Adapters (Driven Adapters)**:
  - Repository Adapters: Implement repository interfaces using MyBatis
  - JwtTokenProviderAdapter: Wraps the existing JwtUtil component
  - PasswordEncoderAdapter: Wraps Spring Security's PasswordEncoder

## Dependency Flow

```
HTTP Request → AuthController (Primary Adapter) 
              → AuthenticateUserUseCase (Application Service - uses input port)
              → AuthenticationDomainService (Domain Service)
              → UserRepository, RoleRepository (Output Ports)
              → UserRepositoryAdapter, RoleRepositoryAdapter (Secondary Adapters)
              → UserMapper, RoleMapper (Existing MyBatis mappers)
              → Database
```

## Benefits of This Approach

1. **Testability**: The domain logic can be tested in isolation without needing a database or web framework
2. **Flexibility**: Database or web framework can be changed without affecting business logic
3. **Maintainability**: Clear separation of concerns makes the code easier to understand and modify
4. **Scalability**: New adapters can be added for different protocols (e.g., gRPC, message queues) without changing core logic

## How to Use

1. The domain layer contains pure business logic with no dependencies on frameworks
2. The application layer defines use cases and depends only on the domain layer
3. The adapter layer implements the interfaces defined in the application and domain layers
4. Spring configuration wires everything together using dependency injection

## Example Flow

1. User sends HTTP POST to /api/v1/auth/login with username and password
2. AuthController (primary adapter) receives the request
3. AuthController calls authenticateUserUseCase.authenticate(username, password)
4. AuthenticateUserUseCaseImpl (application service) coordinates:
   - Calls userRepository.findByUsername(username) (output port)
   - Validates password using passwordEncoder.matches() (output port)
   - Loads user roles using roleRepository.findRolesByUserId()
   - Generates tokens using jwtTokenProvider.generateToken() (output port)
5. Repository adapters implement the repository ports using MyBatis mappers
6. JWT and password adapter implementations wrap existing utility classes
7. Response is returned through the adapter layers back to the HTTP client

This architecture makes it easy to:
- Replace MyBatis with another ORM or database technology
- Replace JWT with another token mechanism
- Add new authentication methods (OAuth, LDAP, etc.) 
- Expose the same functionality through different interfaces (gRPC, GraphQL, etc.)