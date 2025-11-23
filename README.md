# Evaluation Service - Alert Hub Microservice

A production-ready Spring Boot 3 microservice for calculating developer statistics from the `platformInformation` table and sending evaluation results to the notifications queue.

## Technology Stack

- **Java**: 17+
- **Spring Boot**: 3.x (4.0.0)
- **Database**: MySQL 8
- **Message Queue**: Apache Kafka
- **Documentation**: Springdoc OpenAPI (Swagger)
- **Build Tool**: Maven
- **Testing**: JUnit 5 + Mockito

## Architecture

Clean layered architecture:
- **Controller Layer**: REST API endpoints with validation and Swagger documentation
- **Service Layer**: Business logic and Kafka notification handling
- **Repository Layer**: JPA queries for database operations
- **Entity Layer**: JPA entities mapped to database tables
- **DTO Layer**: Data transfer objects for API responses
- **Config Layer**: Spring configuration (Kafka)
- **Exception Layer**: Custom exceptions and global exception handling

## Features

### 1. Find Developer with Most Label Occurrences
- **Endpoint**: `GET /evaluation/developer/most-label`
- **Parameters**: 
  - `label` (required): Label to search for
  - `since` (required): Number of days back from now
- **Returns**: Developer ID, label, count, and time frame

### 2. Aggregate Labels for Developer
- **Endpoint**: `GET /evaluation/developer/{developerId}/label-aggregate`
- **Parameters**: 
  - `developerId` (path): Developer ID
  - `since` (query): Number of days back from now
- **Returns**: Aggregated counts for each label

### 3. Get Total Task Count for Developer
- **Endpoint**: `GET /evaluation/developer/{developerId}/task-amount`
- **Parameters**: 
  - `developerId` (path): Developer ID
  - `since` (query): Number of days back from now
- **Returns**: Total number of tasks

## Database Configuration

### MySQL Setup
```sql
CREATE DATABASE alert_hub;
```

### Configuration (application.properties)
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/alert_hub?createDatabaseIfNotExist=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=1234
spring.jpa.hibernate.ddl-auto=update
```

### Platform Information Table
The `platformInformation` table is automatically created with the following structure:
- `id` (Primary Key, Auto-generated)
- `timestamp` (DateTime)
- `owner_id` (String)
- `project` (String)
- `tag` (String)
- `label` (String)
- `developer_id` (String)
- `task_number` (String)
- `env` (String)
- `user_story` (String)
- `task_point` (Integer)
- `sprint` (String)

## Kafka Integration

### Configuration
- **Bootstrap Servers**: `localhost:9092`
- **Topic**: `email`
- **Serialization**: JSON

### Notification Messages
All evaluation endpoints automatically send result notifications to the `email` Kafka topic with:
- `to`: Manager email (configurable via `evaluation.manager.email` property)
- `subject`: Summary of the evaluation
- `body`: Human-readable details of the result

## Running the Application

### Prerequisites
1. MySQL 8 running on `localhost:3306`
2. Apache Kafka running on `localhost:9092`
3. Java 17+ installed
4. Maven installed

### Build and Run
```bash
# Navigate to project directory
cd Evaluationn

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

### Access Points
- **Application**: `http://localhost:8080`
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI Docs**: `http://localhost:8080/v3/api-docs`

## API Examples

### 1. Find Developer with Most Bug Tasks (Last 30 Days)
```bash
curl -X GET "http://localhost:8080/evaluation/developer/most-label?label=bug&since=30"
```

**Response:**
```json
{
  "developerId": "DEV001",
  "label": "bug",
  "count": 15,
  "sinceDays": 30
}
```

### 2. Get Label Aggregation for Developer
```bash
curl -X GET "http://localhost:8080/evaluation/developer/DEV001/label-aggregate?since=30"
```

**Response:**
```json
{
  "developerId": "DEV001",
  "sinceDays": 30,
  "labelCounts": [
    {
      "label": "bug",
      "count": 10
    },
    {
      "label": "feature",
      "count": 15
    }
  ]
}
```

### 3. Get Total Task Count for Developer
```bash
curl -X GET "http://localhost:8080/evaluation/developer/DEV001/task-amount?since=30"
```

**Response:**
```json
{
  "developerId": "DEV001",
  "sinceDays": 30,
  "taskCount": 42
}
```

## Testing

### Run All Tests
```bash
mvn test
```

### Test Coverage
- Unit tests for `EvaluationServiceImpl`
- All service methods tested with normal and edge cases
- Kafka notification verification
- Exception handling validation

## Error Handling

All errors return a standard `ApiError` response:
```json
{
  "message": "Error description",
  "error": "Error type",
  "status": 404,
  "timestamp": "2025-11-23T10:30:00"
}
```

### HTTP Status Codes
- `200 OK`: Successful request
- `400 Bad Request`: Invalid parameters or validation errors
- `404 Not Found`: Resource not found
- `500 Internal Server Error`: Unexpected server error

## Configuration Properties

### application.properties
```properties
# Application Name
spring.application.name=evaluation-service

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/alert_hub?createDatabaseIfNotExist=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=1234

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Kafka
spring.kafka.bootstrap-servers=localhost:9092

# Swagger/OpenAPI
springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true

# Manager Email for Notifications
evaluation.manager.email=manager@example.com
```

## Package Structure

```
com.alerthub.evaluation
├── config
│   └── KafkaConfig.java
├── controller
│   └── EvaluationController.java
├── dto
│   ├── ApiError.java
│   ├── DeveloperLabelAggregationResponse.java
│   ├── DeveloperTaskAmountResponse.java
│   ├── EvaluationResultMessage.java
│   ├── LabelCountDto.java
│   └── MostLabelDeveloperResponse.java
├── entity
│   └── PlatformInformation.java
├── exception
│   ├── BadRequestException.java
│   ├── GlobalExceptionHandler.java
│   └── ResourceNotFoundException.java
├── repository
│   └── PlatformInformationRepository.java
├── service
│   ├── EvaluationService.java
│   └── EvaluationServiceImpl.java
└── EvaluationApplication.java
```

## Production Considerations

1. **Security**: Add Spring Security for authentication/authorization
2. **Monitoring**: Integrate Spring Actuator for health checks and metrics
3. **Logging**: Configure centralized logging (ELK stack)
4. **Caching**: Add Redis for frequently accessed data
5. **Rate Limiting**: Implement API rate limiting
6. **Database**: Use connection pooling (HikariCP is included by default)
7. **Kafka**: Configure retry and error handling strategies
8. **Environment**: Use Spring Profiles for different environments

## Support

For issues or questions, contact the Alert Hub Team at support@alerthub.com
