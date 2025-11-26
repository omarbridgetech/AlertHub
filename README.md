# User Microservice - MST Alert Hub

## Overview
The User microservice is part of the MST Alert Hub system and manages user accounts, roles, and permissions.

## Features
- User CRUD operations
- Role management
- User-role assignment/revocation
- Username and email uniqueness validation
- RESTful API endpoints
- OpenAPI/Swagger documentation
- Comprehensive exception handling
- Unit tests with Mockito

## Technology Stack
- **Java**: 17
- **Spring Boot**: 3.5.8
- **Database**: MySQL
- **Build Tool**: Maven
- **ORM**: Hibernate/JPA
- **API Documentation**: SpringDoc OpenAPI (Swagger)
- **Testing**: JUnit 5, Mockito
- **Utilities**: Lombok

## Database Schema

### Table: users
| Column   | Type    | Constraints              |
|----------|---------|--------------------------|
| id       | INT     | PK, Auto Increment       |
| username | VARCHAR | NOT NULL, UNIQUE         |
| email    | VARCHAR | NOT NULL, UNIQUE         |
| phone    | VARCHAR |                          |
| password | VARCHAR | NOT NULL                 |

### Table: role
| Column | Type    | Constraints              |
|--------|---------|--------------------------|
| id     | INT     | PK, Auto Increment       |
| role   | VARCHAR | NOT NULL, UNIQUE         |

### Table: user_role
| Column  | Type | Constraints                   |
|---------|------|-------------------------------|
| id      | INT  | PK, Auto Increment            |
| user_id | INT  | FK → users.id, NOT NULL       |
| role_id | INT  | FK → role.id, NOT NULL        |

## API Endpoints

### User Management

#### Get All Users
```
GET /users
Response: List<UserDto>
```

#### Get User by ID
```
GET /users/{id}
Response: UserDto
```

#### Create User
```
POST /users
Request Body: CreateUserRequest
{
  "username": "string",
  "email": "string",
  "phone": "string",
  "password": "string"
}
Response: UserDto (201 Created)
```

#### Update User
```
PUT /users/{id}
Request Body: UpdateUserRequest
{
  "username": "string",
  "email": "string",
  "phone": "string",
  "password": "string"
}
Response: UserDto
```

#### Delete User
```
DELETE /users/{id}
Response: 204 No Content
```

#### Assign Role to User
```
POST /users/{id}/roles/add
Request Body: RoleAssignmentRequest
{
  "roleId": 1
}
Response: UserDto
```

#### Revoke Role from User
```
POST /users/{id}/roles/remove
Request Body: RoleAssignmentRequest
{
  "roleId": 1
}
Response: UserDto
```

### Role Management

#### Get All Roles
```
GET /roles
Response: List<RoleDto>
```

## Configuration

### Database Configuration
Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mst_user_db
spring.datasource.username=root
spring.datasource.password=root
```

### Server Port
```properties
server.port=8081
```

## Running the Application

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+

### Steps
1. Ensure MySQL is running
2. Update database credentials in `application.properties`
3. Run the application:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8081`

## API Documentation
Access Swagger UI at:
```
http://localhost:8081/swagger-ui.html
```

OpenAPI JSON documentation:
```
http://localhost:8081/api-docs
```

## Running Tests
```bash
mvn test
```

## Project Structure
```
src/main/java/com/Alnsor/User/
├── config/              # Configuration classes
├── controller/          # REST controllers
├── dto/                 # Data Transfer Objects
├── entity/              # JPA entities
├── exception/           # Custom exceptions and handlers
├── mapper/              # Entity-DTO mappers
├── repository/          # JPA repositories
└── service/             # Business logic

src/test/java/com/Alnsor/User/
└── service/             # Service layer tests
```

## Exception Handling
The microservice includes comprehensive exception handling:
- `UserNotFoundException` - HTTP 404
- `RoleNotFoundException` - HTTP 404
- `DuplicateUserException` - HTTP 409
- `MethodArgumentNotValidException` - HTTP 400 (Validation errors)
- Generic exceptions - HTTP 500

## Sample Role Examples
Common roles that can be stored in the `role` table:
- `createAction`
- `updateMetric`
- `deleteAction`
- `read`
- `admin`
- `viewer`

## Integration with Other Microservices
This User microservice can be integrated with a Security/Authentication service for:
- User authentication
- Permission checks
- JWT token generation (implemented by Security service)
- Role-based access control

## Future Enhancements
- Password encryption (BCrypt)
- Role hierarchy
- User profile management
- Audit logging
- Soft delete functionality
- Pagination for user lists

## License
Apache 2.0

## Contact
MST Alert Hub Team - support@mstalerthub.com
