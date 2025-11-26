# User Microservice - Complete File Structure

## Project Root
```
User/
├── pom.xml                                 # Maven dependencies and build configuration
├── README.md                               # Project documentation
├── mvnw                                    # Maven wrapper (Unix)
├── mvnw.cmd                                # Maven wrapper (Windows)
└── src/
    ├── main/
    │   ├── java/com/Alnsor/User/
    │   │   ├── UserApplication.java        # Spring Boot main application class
    │   │   ├── config/
    │   │   │   └── OpenApiConfig.java      # OpenAPI/Swagger configuration
    │   │   ├── controller/
    │   │   │   ├── UserController.java     # User REST endpoints
    │   │   │   └── RoleController.java     # Role REST endpoints
    │   │   ├── dto/
    │   │   │   ├── UserDto.java           # User response DTO
    │   │   │   ├── CreateUserRequest.java # Create user request DTO
    │   │   │   ├── UpdateUserRequest.java # Update user request DTO
    │   │   │   ├── RoleDto.java           # Role response DTO
    │   │   │   └── RoleAssignmentRequest.java # Role assignment DTO
    │   │   ├── entity/
    │   │   │   ├── UserEntity.java        # User JPA entity
    │   │   │   ├── RoleEntity.java        # Role JPA entity
    │   │   │   └── UserRoleEntity.java    # User-Role mapping entity
    │   │   ├── exception/
    │   │   │   ├── UserNotFoundException.java     # Custom exception
    │   │   │   ├── RoleNotFoundException.java     # Custom exception
    │   │   │   ├── DuplicateUserException.java    # Custom exception
    │   │   │   ├── ErrorResponse.java             # Error response DTO
    │   │   │   └── GlobalExceptionHandler.java    # Global exception handler
    │   │   ├── mapper/
    │   │   │   ├── UserMapper.java        # User entity-DTO mapper
    │   │   │   └── RoleMapper.java        # Role entity-DTO mapper
    │   │   ├── repository/
    │   │   │   ├── UserRepository.java    # User JPA repository
    │   │   │   ├── RoleRepository.java    # Role JPA repository
    │   │   │   └── UserRoleRepository.java # UserRole JPA repository
    │   │   └── service/
    │   │       ├── UserService.java       # User business logic
    │   │       └── RoleService.java       # Role business logic
    │   └── resources/
    │       ├── application.properties     # Application configuration
    │       ├── init-db.sql               # Database initialization script
    │       ├── static/                   # Static resources
    │       └── templates/                # Template files
    └── test/
        └── java/com/Alnsor/User/
            ├── UserApplicationTests.java  # Application context test
            └── service/
                ├── UserServiceTest.java   # User service unit tests
                └── RoleServiceTest.java   # Role service unit tests
```

## Layer Architecture

### 1. Controller Layer (REST API)
- **UserController**: Handles all user-related HTTP requests
  - GET /users - List all users
  - GET /users/{id} - Get user by ID
  - POST /users - Create new user
  - PUT /users/{id} - Update user
  - DELETE /users/{id} - Delete user
  - POST /users/{id}/roles/add - Assign role
  - POST /users/{id}/roles/remove - Revoke role

- **RoleController**: Handles role-related HTTP requests
  - GET /roles - List all roles

### 2. Service Layer (Business Logic)
- **UserService**: User business operations
  - User CRUD operations
  - Role assignment/revocation
  - Username/email uniqueness validation
  - User queries by username/email

- **RoleService**: Role business operations
  - Role retrieval
  - Role queries

### 3. Repository Layer (Data Access)
- **UserRepository**: User database operations
- **RoleRepository**: Role database operations
- **UserRoleRepository**: User-Role mapping operations

### 4. Entity Layer (JPA Entities)
- **UserEntity**: Maps to 'users' table
- **RoleEntity**: Maps to 'role' table
- **UserRoleEntity**: Maps to 'user_role' table

### 5. DTO Layer (Data Transfer Objects)
- **UserDto**: User response object
- **CreateUserRequest**: Create user input
- **UpdateUserRequest**: Update user input
- **RoleDto**: Role response object
- **RoleAssignmentRequest**: Role assignment input

### 6. Mapper Layer (Entity-DTO Conversion)
- **UserMapper**: Converts UserEntity ↔ UserDto
- **RoleMapper**: Converts RoleEntity ↔ RoleDto

### 7. Exception Layer (Error Handling)
- **UserNotFoundException**: User not found (404)
- **RoleNotFoundException**: Role not found (404)
- **DuplicateUserException**: Duplicate username/email (409)
- **ErrorResponse**: Standardized error response
- **GlobalExceptionHandler**: Centralized exception handling

### 8. Configuration Layer
- **OpenApiConfig**: Swagger/OpenAPI documentation setup

## Key Features Implemented

✅ Complete CRUD operations for users
✅ Role management system
✅ User-role assignment and revocation
✅ Input validation with Jakarta Validation
✅ Custom exception handling
✅ RESTful API design
✅ OpenAPI/Swagger documentation
✅ MySQL database integration
✅ JPA/Hibernate ORM
✅ Lombok for boilerplate reduction
✅ Clean architecture (layered design)
✅ Unit tests with Mockito
✅ Comprehensive error responses
✅ Transaction management

## Database Relationships

- **One-to-Many**: UserEntity → UserRoleEntity
- **One-to-Many**: RoleEntity → UserRoleEntity
- **Many-to-One**: UserRoleEntity → UserEntity
- **Many-to-One**: UserRoleEntity → RoleEntity

## Running the Application

1. **Start MySQL** server
2. **Configure** database credentials in `application.properties`
3. **Run** application: `mvn spring-boot:run`
4. **Access** Swagger UI: `http://localhost:8081/swagger-ui.html`
5. **Initialize** database with sample data: Run `init-db.sql` script

## Testing

Run all tests:
```bash
mvn test
```

Test coverage includes:
- UserService: 18 test cases
- RoleService: 6 test cases

## Notes

- Server runs on port **8081** (configurable)
- Database name: **mst_user_db**
- All entities use auto-generated IDs
- Passwords stored as plain text (encrypt in production)
- EAGER fetching for user roles
- Transactional service methods
- Validation on all input DTOs
