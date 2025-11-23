# User Microservice - Architecture Diagrams

## 1. Layered Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     CLIENT (HTTP Requests)                   │
└───────────────────────────┬─────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                   CONTROLLER LAYER                           │
│  ┌──────────────────┐          ┌──────────────────┐         │
│  │ UserController   │          │ RoleController   │         │
│  │ - getAllUsers()  │          │ - getAllRoles()  │         │
│  │ - getUserById()  │          └──────────────────┘         │
│  │ - createUser()   │                                        │
│  │ - updateUser()   │    + GlobalExceptionHandler           │
│  │ - deleteUser()   │                                        │
│  │ - assignRole()   │                                        │
│  │ - revokeRole()   │                                        │
│  └──────────────────┘                                        │
└───────────────────────────┬─────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                      DTO LAYER                               │
│  ┌────────────────┐  ┌──────────────┐  ┌─────────────┐     │
│  │ UserDto        │  │ CreateUser   │  │ UpdateUser  │     │
│  │ RoleDto        │  │ Request      │  │ Request     │     │
│  └────────────────┘  └──────────────┘  └─────────────┘     │
└───────────────────────────┬─────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                     MAPPER LAYER                             │
│  ┌──────────────────┐          ┌──────────────────┐         │
│  │ UserMapper       │          │ RoleMapper       │         │
│  │ - toDto()        │          │ - toDto()        │         │
│  └──────────────────┘          └──────────────────┘         │
└───────────────────────────┬─────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                    SERVICE LAYER                             │
│  ┌──────────────────┐          ┌──────────────────┐         │
│  │ UserService      │          │ RoleService      │         │
│  │ - getAllUsers()  │          │ - getAllRoles()  │         │
│  │ - getUserById()  │          │ - getRoleById()  │         │
│  │ - createUser()   │          │ - getRoleByName()│         │
│  │ - updateUser()   │          └──────────────────┘         │
│  │ - deleteUser()   │                                        │
│  │ - assignRole()   │     @Transactional                    │
│  │ - revokeRole()   │     Business Logic                    │
│  └──────────────────┘     Validation                        │
└───────────────────────────┬─────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                  REPOSITORY LAYER                            │
│  ┌──────────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │ UserRepository   │  │ RoleRepo     │  │ UserRoleRepo │  │
│  │ (JpaRepository)  │  │ (JpaRepo)    │  │ (JpaRepo)    │  │
│  └──────────────────┘  └──────────────┘  └──────────────┘  │
└───────────────────────────┬─────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                     ENTITY LAYER                             │
│  ┌──────────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │ UserEntity       │  │ RoleEntity   │  │ UserRoleEnt  │  │
│  │ @Entity          │  │ @Entity      │  │ @Entity      │  │
│  └──────────────────┘  └──────────────┘  └──────────────┘  │
└───────────────────────────┬─────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                   MySQL DATABASE                             │
│  ┌──────────┐      ┌──────────┐      ┌──────────────┐      │
│  │  users   │      │   role   │      │  user_role   │      │
│  └──────────┘      └──────────┘      └──────────────┘      │
└─────────────────────────────────────────────────────────────┘
```

## 2. Database Entity Relationships

```
┌─────────────────────────┐
│      UserEntity         │
├─────────────────────────┤
│ - id (PK)               │
│ - username (UNIQUE)     │
│ - email (UNIQUE)        │
│ - phone                 │
│ - password              │
│ - userRoles (List)      │
└──────────┬──────────────┘
           │ 1
           │
           │ OneToMany
           │
           ▼ *
┌─────────────────────────┐
│   UserRoleEntity        │
├─────────────────────────┤
│ - id (PK)               │
│ - user (FK)             │◄───── ManyToOne
│ - role (FK)             │
└──────────┬──────────────┘
           │ *
           │
           │ ManyToOne
           │
           ▼ 1
┌─────────────────────────┐
│      RoleEntity         │
├─────────────────────────┤
│ - id (PK)               │
│ - role (UNIQUE)         │
│ - userRoles (List)      │
└─────────────────────────┘

Relationship: Many-to-Many (User ←→ Role)
Implementation: Through junction table (user_role)
```

## 3. API Request Flow

```
1. CLIENT REQUEST
   │
   ├─→ POST /users
   │   Body: CreateUserRequest
   │
   ▼
2. UserController.createUser()
   │
   ├─→ @Valid validation
   │   (username, email, password checks)
   │
   ▼
3. UserService.createUser()
   │
   ├─→ Check username uniqueness
   ├─→ Check email uniqueness
   ├─→ Create UserEntity
   ├─→ Save to database
   │
   ▼
4. UserRepository.save()
   │
   ├─→ JPA/Hibernate
   │
   ▼
5. MySQL Database
   │
   ├─→ INSERT INTO users
   │
   ▼
6. Return UserEntity
   │
   ▼
7. UserMapper.toDto()
   │
   ├─→ Convert Entity → DTO
   │
   ▼
8. Return UserDto to Client
   │
   └─→ HTTP 201 Created
       Body: UserDto (with roles)
```

## 4. Exception Handling Flow

```
┌─────────────────────────┐
│    Controller Method    │
│    throws Exception     │
└──────────┬──────────────┘
           │
           ▼
┌─────────────────────────┐
│ GlobalExceptionHandler  │
│   @ControllerAdvice     │
└──────────┬──────────────┘
           │
           ├──→ UserNotFoundException → 404 Not Found
           │
           ├──→ RoleNotFoundException → 404 Not Found
           │
           ├──→ DuplicateUserException → 409 Conflict
           │
           ├──→ MethodArgumentNotValidException → 400 Bad Request
           │
           └──→ Exception → 500 Internal Server Error
           │
           ▼
┌─────────────────────────┐
│    ErrorResponse DTO    │
│  - timestamp            │
│  - status               │
│  - error                │
│  - message              │
│  - path                 │
└─────────────────────────┘
```

## 5. Test Architecture

```
┌─────────────────────────────────────────┐
│         UserServiceTest                 │
│         @ExtendWith(MockitoExtension)   │
├─────────────────────────────────────────┤
│ @Mock                                   │
│  - UserRepository                       │
│  - RoleRepository                       │
│  - UserRoleRepository                   │
│  - UserMapper                           │
│                                         │
│ @InjectMocks                            │
│  - UserService                          │
│                                         │
│ Test Methods (18):                      │
│  - getAllUsers()                        │
│  - getUserById() - success              │
│  - getUserById() - not found            │
│  - createUser() - success               │
│  - createUser() - duplicate username    │
│  - createUser() - duplicate email       │
│  - updateUser() - success               │
│  - updateUser() - not found             │
│  - deleteUser() - success               │
│  - deleteUser() - not found             │
│  - assignRole() - success               │
│  - assignRole() - user not found        │
│  - assignRole() - role not found        │
│  - revokeRole() - success               │
│  - revokeRole() - user not found        │
│  - getUserByUsername() - success        │
│  - getUserByUsername() - not found      │
└─────────────────────────────────────────┘
```

## 6. Deployment Architecture

```
┌──────────────────────────────────────────────┐
│           MST Alert Hub System               │
├──────────────────────────────────────────────┤
│                                              │
│  ┌────────────────┐    ┌─────────────────┐  │
│  │   Gateway      │───→│  User Service   │  │
│  │   (Port 8080)  │    │  (Port 8081)    │  │
│  └────────────────┘    └────────┬────────┘  │
│         │                       │            │
│         ▼                       ▼            │
│  ┌────────────────┐    ┌─────────────────┐  │
│  │ Security       │───→│  MySQL DB       │  │
│  │ Service        │    │  (Port 3306)    │  │
│  │ (Port 8082)    │    │  mst_user_db    │  │
│  └────────────────┘    └─────────────────┘  │
│         │                                    │
│         ▼                                    │
│  ┌────────────────┐                          │
│  │  Action        │                          │
│  │  Service       │                          │
│  │  (Port 8083)   │                          │
│  └────────────────┘                          │
│                                              │
└──────────────────────────────────────────────┘
```

## 7. Data Flow: Assign Role to User

```
Step 1: Client Request
POST /users/1/roles/add
Body: { "roleId": 2 }

Step 2: Controller Validation
@Valid RoleAssignmentRequest
- Check roleId is not null

Step 3: Service Layer
UserService.assignRoleToUser(1, 2)
├─→ Find UserEntity by id=1
├─→ Find RoleEntity by id=2
├─→ Check if user already has role
├─→ Create UserRoleEntity
├─→ Add to user.userRoles list
└─→ Save UserEntity

Step 4: Database Operations
INSERT INTO user_role (user_id, role_id) VALUES (1, 2)

Step 5: Response Mapping
UserEntity → UserMapper → UserDto
Include all roles for the user

Step 6: HTTP Response
200 OK
Body: UserDto with updated roles list
```

## 8. Technology Stack Layers

```
┌─────────────────────────────────────┐
│        Presentation Layer           │
│  - REST Controllers                 │
│  - Swagger UI                       │
│  - Exception Handlers               │
└────────────┬────────────────────────┘
             │
┌────────────▼────────────────────────┐
│         Business Layer              │
│  - Services (@Transactional)        │
│  - DTOs                             │
│  - Mappers                          │
│  - Validation Logic                 │
└────────────┬────────────────────────┘
             │
┌────────────▼────────────────────────┐
│       Persistence Layer             │
│  - JPA Repositories                 │
│  - Entities (@Entity)               │
│  - Hibernate ORM                    │
└────────────┬────────────────────────┘
             │
┌────────────▼────────────────────────┐
│         Database Layer              │
│  - MySQL Database                   │
│  - Tables (users, role, user_role)  │
└─────────────────────────────────────┘
```

---

**Note**: These diagrams use ASCII art for portability. For production documentation, consider generating actual UML diagrams using tools like PlantUML, draw.io, or Lucidchart.
