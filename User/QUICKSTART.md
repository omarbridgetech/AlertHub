# Quick Start Guide - User Microservice

## Prerequisites
- ✅ Java 17 or higher
- ✅ Maven 3.6+
- ✅ MySQL 8.0+
- ✅ IDE (IntelliJ IDEA, Eclipse, or VS Code)

## Step-by-Step Setup

### 1. Database Setup
```sql
-- Start MySQL and create database
CREATE DATABASE mst_user_db;
```

### 2. Configure Database Connection
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mst_user_db
spring.datasource.username=YOUR_MYSQL_USERNAME
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

### 3. Build the Project
```bash
cd User
mvn clean install
```

### 4. Run the Application
```bash
mvn spring-boot:run
```

Or run directly from your IDE by executing the `UserApplication` main class.

### 5. Verify Application is Running
Open your browser and navigate to:
```
http://localhost:8081/swagger-ui.html
```

You should see the Swagger UI interface with all available APIs.

### 6. Initialize Sample Data (Optional)
Execute the SQL script to add sample roles and users:
```bash
mysql -u root -p mst_user_db < src/main/resources/init-db.sql
```

## Testing the APIs

### Option 1: Using Swagger UI
1. Go to `http://localhost:8081/swagger-ui.html`
2. Click on any endpoint
3. Click "Try it out"
4. Fill in the parameters
5. Click "Execute"

### Option 2: Using cURL

**Create a User:**
```bash
curl -X POST http://localhost:8081/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "phone": "1234567890",
    "password": "password123"
  }'
```

**Get All Users:**
```bash
curl http://localhost:8081/users
```

**Get User by ID:**
```bash
curl http://localhost:8081/users/1
```

**Update User:**
```bash
curl -X PUT http://localhost:8081/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_updated",
    "email": "john.updated@example.com"
  }'
```

**Delete User:**
```bash
curl -X DELETE http://localhost:8081/users/1
```

**Get All Roles:**
```bash
curl http://localhost:8081/roles
```

**Assign Role to User:**
```bash
curl -X POST http://localhost:8081/users/1/roles/add \
  -H "Content-Type: application/json" \
  -d '{
    "roleId": 1
  }'
```

**Revoke Role from User:**
```bash
curl -X POST http://localhost:8081/users/1/roles/remove \
  -H "Content-Type: application/json" \
  -d '{
    "roleId": 1
  }'
```

### Option 3: Using Postman
1. Import the `User-Microservice-API.postman_collection.json` file
2. Update the base URL if needed
3. Execute the requests

## Running Tests
```bash
mvn test
```

## Common Issues & Solutions

### Issue 1: Database Connection Failed
**Solution:** Verify MySQL is running and credentials are correct in `application.properties`

### Issue 2: Port 8081 Already in Use
**Solution:** Change the port in `application.properties`:
```properties
server.port=8082
```

### Issue 3: Tables Not Created
**Solution:** Check that `spring.jpa.hibernate.ddl-auto=update` is set in `application.properties`

### Issue 4: Lombok Not Working
**Solution:** 
- Enable annotation processing in your IDE
- For IntelliJ: Settings → Build → Compiler → Annotation Processors → Enable
- Install Lombok plugin for your IDE

## Project Structure Overview
```
User/
├── src/main/java/com/Alnsor/User/
│   ├── controller/     # REST API endpoints
│   ├── service/        # Business logic
│   ├── repository/     # Database access
│   ├── entity/         # Database models
│   ├── dto/            # API request/response objects
│   ├── mapper/         # Entity-DTO conversion
│   ├── exception/      # Error handling
│   └── config/         # Configuration classes
└── src/main/resources/
    ├── application.properties  # Configuration
    └── init-db.sql            # Sample data
```

## Next Steps
1. ✅ Run the application
2. ✅ Test the APIs using Swagger UI
3. ✅ Initialize sample roles and users
4. ✅ Integrate with other microservices (Security, Action, etc.)
5. ✅ Add authentication/authorization
6. ✅ Deploy to production environment

## Useful Endpoints
- **Swagger UI**: http://localhost:8081/swagger-ui.html
- **API Docs JSON**: http://localhost:8081/api-docs
- **Health Check**: http://localhost:8081/actuator/health (if actuator is added)

## Development Tips
- Use Swagger UI for interactive API testing
- Check logs for detailed error messages
- Use the init-db.sql script to quickly set up test data
- All validation errors return HTTP 400 with field details
- Foreign key constraints are enforced at database level

## Support
For issues or questions, refer to:
- README.md - Complete documentation
- PROJECT_STRUCTURE.md - Detailed architecture
- Swagger UI - API reference

## Happy Coding! 🚀
