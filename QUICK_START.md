# Quick Start Guide - Bank Application

## Database Options

### Option 1: H2 (Default - Easiest) ⭐
No setup required! Data is in-memory (resets on restart).

### Option 2: PostgreSQL (Production-Ready) 🚀
Requires Docker. Data persists between restarts.

```bash
# Start PostgreSQL
docker-compose up -d
```

---

## 1. Start the Application

### With H2 (Default - No Setup)

#### Option A: Using Maven Command
```bash
cd c:\Users\shrey\OneDrive\Desktop\Projects\bank-application
mvn spring-boot:run
```

#### Option B: Using IDE (IntelliJ/Eclipse)
1. Open the project in your IDE
2. Navigate to `src/main/java/com/bank/BankApplication.java`
3. Right-click and select "Run"

#### Option C: Build and Run JAR
```bash
mvn clean package
java -jar target/bank-application-1.0.0.jar
```

### With PostgreSQL (Docker Required)

#### Option A: Using Setup Script (Windows)
```bash
setup-postgres.bat
# Choose: 1 (Start PostgreSQL), then 4 (Run with PostgreSQL)
```

#### Option B: Using Maven Command
```bash
# Start PostgreSQL first
docker-compose up -d

# Run application
mvn spring-boot:run -Dspring-boot.run.profiles=postgresql
```

#### Option C: Using Environment Variables
```bash
# Windows PowerShell
$env:DB_URL="jdbc:postgresql://localhost:5432/bankdb"
$env:DB_DRIVER="org.postgresql.Driver"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="postgres"
$env:JPA_PLATFORM="org.hibernate.dialect.PostgreSQLDialect"

mvn spring-boot:run
```

📖 **See `POSTGRESQL_QUICK_REF.md` for PostgreSQL quick reference**

## 2. Verify Application is Running

Open browser and check:
- Application: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- H2 Console (H2 only): http://localhost:8080/h2-console
- pgAdmin (PostgreSQL with profile): http://localhost:5050

You should see Swagger UI with all API endpoints!

**Console Logs to Look For:**
```
✅ Started BankApplication in X.XXX seconds
✅ Tomcat started on port(s): 8080
```

## 3. Test with Postman - Complete Flow

### Step 1: Register a Customer
```
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "firstName": "Test",
  "lastName": "User",
  "email": "test@example.com",
  "username": "testuser",
  "password": "password123",
  "phone": "1234567890",
  "address": "123 Test Street, Test City, TC 12345"
}
```

### Step 2: Login
```
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "username": "testuser",
  "password": "password123"
}
```

**Copy the token from response!**

### Step 3: Set Authorization Header
For all subsequent requests:
```
Authorization: Bearer YOUR_TOKEN_HERE
```

### Step 4: Create Account
```
POST http://localhost:8080/api/accounts
Authorization: Bearer YOUR_TOKEN_HERE
Content-Type: application/json

{
  "accountName": "My Savings",
  "accountType": "SAVINGS"
}
```

**Copy the accountNumber from response!**

### Step 5: Deposit Money
```
POST http://localhost:8080/api/transactions/deposit
Authorization: Bearer YOUR_TOKEN_HERE
Content-Type: application/json

{
  "accountNumber": "YOUR_ACCOUNT_NUMBER",
  "amount": 10000.00,
  "description": "Initial deposit"
}
```

### Step 6: View Dashboard
```
GET http://localhost:8080/api/auth/dashboard
Authorization: Bearer YOUR_TOKEN_HERE
```

### Step 7: Withdraw Money
```
POST http://localhost:8080/api/transactions/withdraw
Authorization: Bearer YOUR_TOKEN_HERE
Content-Type: application/json

{
  "accountNumber": "YOUR_ACCOUNT_NUMBER",
  "amount": 2000.00,
  "description": "Cash withdrawal"
}
```

### Step 8: Get Account Statement
```
GET http://localhost:8080/api/transactions/statement?accountNumber=YOUR_ACCOUNT_NUMBER&page=0&size=10
Authorization: Bearer YOUR_TOKEN_HERE
```

## 4. Import Postman Collection

1. Open Postman
2. Click "Import" button
3. Select file: `Bank-API-Postman-Collection.json`
4. Collection will be imported with all endpoints
5. Create a new Environment:
   - Add variable: `jwt_token` (will be auto-set after login)
   - Add variable: `account_number` (will be auto-set after account creation)

## 5. Test All Features

Using Postman Collection:
1. Authentication folder → Register Customer
2. Authentication folder → Login (token auto-saved)
3. Authentication folder → Get Dashboard
4. Account Management folder → Create Account (account number auto-saved)
5. Transactions folder → Deposit Money
6. Transactions folder → Withdraw Money
7. Account Management folder → Create Account (create 2nd account for transfer)
8. Transactions folder → Fund Transfer
9. Transactions folder → Get Account Statement
10. Account Management folder → Search Accounts

## 6. Troubleshooting

### Port 8080 already in use?
Change port in `application.properties`:
```properties
server.port=8081
```

### Can't connect to application?
1. Check if application started successfully (look for "Started BankApplication")
2. Check firewall settings
3. Try: http://localhost:8080/swagger-ui.html

### Token expired?
Login again to get a new token (valid for 24 hours)

### Database issues?

**H2 (Default):**
- Using H2 in-memory DB - no setup needed
- Data resets on restart
- Access console at http://localhost:8080/h2-console

**PostgreSQL:**
- Check Docker: `docker ps` (should see postgres-bank)
- Check logs: `docker logs postgres-bank`
- Restart: `docker-compose restart`

## 7. Check Database

### H2 Console (Default)
1. Open: http://localhost:8080/h2-console
2. JDBC URL: `jdbc:h2:mem:bankdb`
3. Username: `sa`
4. Password: `password`
5. Click "Connect"
6. Run SQL: `SELECT * FROM CUSTOMERS;`

### PostgreSQL (Docker)
```bash
# List tables
docker exec -it postgres-bank psql -U postgres -d bankdb -c "\dt"

# View data
docker exec -it postgres-bank psql -U postgres -d bankdb -c "SELECT * FROM customers;"

# Or use pgAdmin
# Start with: docker-compose --profile with-pgadmin up -d
# Access: http://localhost:5050 (admin@bank.com / admin)
```

## 8. API Documentation

Complete API documentation available at:
- Swagger UI: http://localhost:8080/swagger-ui.html
- You can test all APIs directly from Swagger!

## 9. Account Types Available

- `SAVINGS` - Savings Account
- `CURRENT` - Current Account
- `FIXED_DEPOSIT` - Fixed Deposit Account
- `SALARY` - Salary Account

## 10. Important Notes

- All amounts should be positive numbers with up to 2 decimal places
- Account numbers are auto-generated (format: ACC + timestamp + random)
- Transaction IDs are auto-generated (format: TXN + timestamp + random)
- JWT token expires after 24 hours
- Cannot delete account with non-zero balance
- Cannot transfer to the same account

## Success Indicators

✅ Application starts without errors
✅ Swagger UI loads at http://localhost:8080/swagger-ui.html
✅ You can register a customer
✅ You can login and get JWT token
✅ You can create an account
✅ You can deposit and withdraw money
✅ Dashboard shows correct summary
✅ Account statement shows all transactions

## Database Selection Guide

| Use Case | Database | Why |
|----------|----------|-----|
| Quick testing | H2 | No setup, instant start |
| Learning/Development | H2 | Simple, resets cleanly |
| Production-like testing | PostgreSQL | Persistent, realistic |
| Production deployment | PostgreSQL | Reliable, feature-rich |

## Additional Resources

- **PostgreSQL Setup:** `POSTGRESQL_SETUP.md` - Detailed setup guide
- **Quick Reference:** `POSTGRESQL_QUICK_REF.md` - Common commands
- **Database Comparison:** `DATABASE_COMPARISON.md` - Feature comparison
- **Full Documentation:** `README.md` - Complete project docs

## Support

If you encounter any issues:
1. Check application logs in console
2. Verify all prerequisites are installed (Java 17+, Maven 3.6+)
3. For PostgreSQL: Check `POSTGRESQL_QUICK_REF.md`
4. Check README.md for detailed information
5. Use Swagger UI to test individual endpoints
