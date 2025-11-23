# Quick Start Guide - Evaluation Service

## 🚀 Quick Start (5 Minutes)

### Step 1: Prerequisites Check
```powershell
# Check Java version (need 17+)
java -version

# Check MySQL is running
mysql -u root -p -e "SELECT 1;"

# Check Kafka is running (if you have it installed)
# If not, the service will compile and run, but Kafka notifications will fail gracefully
```

### Step 2: Build the Project
```powershell
cd "c:\Users\hassa\Downloads\Evaluationn\Evaluationn"
mvn clean install -DskipTests
```

### Step 3: Start the Application
```powershell
mvn spring-boot:run
```

You should see:
```
Started EvaluationApplication in X seconds
```

### Step 4: Load Sample Data
Open another terminal and run:
```powershell
mysql -u root -p1234 alert_hub < sample-data.sql
```

This creates 3 developers (DEV001, DEV002, DEV003) with realistic task data spanning 30 days.

### Step 5: Test the API

#### Option A: Use Swagger UI (Recommended)
1. Open browser: http://localhost:8080/swagger-ui.html
2. Try the "GET /evaluation/developer/most-label" endpoint
3. Set `label` = `bug` and `since` = `30`
4. Click "Execute"

#### Option B: Use cURL
```powershell
# Find developer with most bug tasks in last 30 days
curl "http://localhost:8080/evaluation/developer/most-label?label=bug&since=30"

# Get label aggregation for DEV001
curl "http://localhost:8080/evaluation/developer/DEV001/label-aggregate?since=30"

# Get total task count for DEV001
curl "http://localhost:8080/evaluation/developer/DEV001/task-amount?since=30"
```

#### Option C: Import Postman Collection
1. Open Postman
2. Import `Evaluation-Service.postman_collection.json`
3. Run any of the 11 pre-configured requests

## 📊 Expected Results

### Endpoint 1: Most Bug Tasks
```json
{
  "developerId": "DEV002",
  "label": "bug",
  "count": 15,
  "sinceDays": 30
}
```
*(DEV002 has more bug tasks than DEV001)*

### Endpoint 2: Label Aggregation for DEV001
```json
{
  "developerId": "DEV001",
  "sinceDays": 30,
  "labelCounts": [
    {"label": "bug", "count": 10},
    {"label": "enhancement", "count": 7},
    {"label": "feature", "count": 15}
  ]
}
```

### Endpoint 3: Task Count for DEV001
```json
{
  "developerId": "DEV001",
  "sinceDays": 30,
  "taskCount": 32
}
```

## 🔍 Verify Kafka Notifications

If Kafka is running, check the "email" topic:
```bash
# Consume messages from the email topic
kafka-console-consumer --bootstrap-server localhost:9092 --topic email --from-beginning
```

You should see JSON messages like:
```json
{
  "to": "manager@example.com",
  "subject": "Developer with Most bug Tasks",
  "body": "Developer DEV002 has the most tasks with label 'bug': 15 tasks in the last 30 days."
}
```

## ❌ Troubleshooting

### Issue: "Access denied for user 'root'@'localhost'"
**Solution:** Update password in `application.properties`:
```properties
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

### Issue: "Table 'alert_hub.platformInformation' doesn't exist"
**Solution:** The table is auto-created. Just start the app first, then load sample data.

### Issue: Kafka connection errors
**Solution:** Kafka is optional for development. The service works without it, but notifications won't be sent. To disable errors, comment out Kafka config temporarily.

### Issue: Port 8080 already in use
**Solution:** Add to `application.properties`:
```properties
server.port=8081
```

## 🧪 Run Tests
```powershell
# Run all tests
mvn test

# Run only service tests
mvn test -Dtest=EvaluationServiceImplTest
```

Expected output:
```
Tests run: 11, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## 📚 Documentation Links

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs
- **Full README**: See `README.md`
- **Implementation Details**: See `IMPLEMENTATION_SUMMARY.md`

## 🎯 What's Next?

1. **Explore the API** via Swagger UI
2. **Test with Postman** using the provided collection
3. **Review the code** - Clean, well-documented, production-ready
4. **Run the tests** - 11 comprehensive unit tests
5. **Customize** - Add more features or integrate with your system

## 💡 Pro Tips

1. **Change manager email**: Update `evaluation.manager.email` in `application.properties`
2. **Add more sample data**: Edit `sample-data.sql` and reload
3. **Test different time ranges**: Try `since=7` or `since=90`
4. **Monitor logs**: Check `spring-boot-*.log` for detailed execution logs
5. **Performance**: The service uses JPA repository methods optimized for MySQL

## ✅ Success Checklist

- [ ] Application starts without errors
- [ ] Swagger UI loads at http://localhost:8080/swagger-ui.html
- [ ] Sample data loaded successfully
- [ ] All three endpoints return valid JSON responses
- [ ] Tests pass with `mvn test`

**If all boxes are checked, you're good to go!** 🎉

---

**Need help?** Check `IMPLEMENTATION_SUMMARY.md` for detailed technical information.
