# Bank Account Management System

A comprehensive REST API for Bank Account Management built with Spring Boot, featuring JWT authentication, transaction management, and complete CRUD operations.

## Features

### Core Requirements ✅
1. **Customer Login/Logout** - JWT token-based authentication
2. **Dashboard API** - Returns customer details and account summary
3. **Account Management** - Full CRUD operations for bank accounts
4. **Transactions** - Deposit/Withdraw with balance validation
5. **Account Statement** - Transaction history with pagination
6. **Input Validation** - Complete validation for all request fields
7. **Proper DTOs** - Well-structured request/response objects
8. **Database Support** - H2 (default), MySQL, PostgreSQL, Oracle compatible
9. **Layered Architecture** - Controller → Service → Repository
10. **Configuration** - All values in application.properties

### Bonus Features ✅
- **Fund Transfer** - Transfer money between accounts with transaction recording
- **Search & Filter** - Filter accounts by name, type, and status
- **Swagger/OpenAPI** - Complete API documentation at `/swagger-ui.html`
- **AOP Logging** - Aspect-based logging for all API calls
- **Global Exception Handling** - Consistent error response format
- **JWT Authentication** - Secure token-based authentication
- **Unit Tests** - JUnit and Mockito tests for service layer

## Technology Stack

- Java 17
- Spring Boot 3.2.1
- Spring Security with JWT
- Spring Data JPA
- H2 Database (default)
- MySQL support (optional)
- PostgreSQL support (optional)
- Lombok
- Swagger/OpenAPI 3.0
- JUnit 5 & Mockito
- Maven

## Prerequisites

- JDK 17 or higher
- Maven 3.6 or higher
- IDE (IntelliJ IDEA, Eclipse, or STS)
- Postman (for API testing)

## Getting Started

### 1. Clone or Navigate to Project Directory

```bash
cd c:\Users\shrey\OneDrive\Desktop\Projects\bank-application
```

### 2. Build the Project

```bash
mvn clean install
```

### 3. Run the Application

```bash
mvn spring-boot:run
```

Or run the main class `com.bank.BankApplication` from your IDE.

The application will start on `http://localhost:8080`

### 4. Access API Documentation

Once the application is running, access Swagger UI:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs

### 5. Access H2 Console (Optional)

- **H2 Console**: http://localhost:8080/h2-console
- **JDBC URL**: jdbc:h2:mem:bankdb
- **Username**: sa
- **Password**: password

## Configuration

### Default Configuration (H2 Database)

The application uses H2 in-memory database by default. No configuration needed.

### PostgreSQL Configuration (Recommended for Production)

**Option 1: Using Spring Profile** (Easiest)
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=postgresql
```

**Option 2: Environment Variables**
```bash
# Windows PowerShell
$env:DB_URL="jdbc:postgresql://localhost:5432/bankdb"
$env:DB_DRIVER="org.postgresql.Driver"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="postgres"
$env:JPA_PLATFORM="org.hibernate.dialect.PostgreSQLDialect"

# Then run
mvn spring-boot:run
```

**Option 3: Update `application.properties`**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/bankdb
spring.datasource.driverClassName=org.postgresql.Driver
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

📖 **See [POSTGRESQL_SETUP.md](POSTGRESQL_SETUP.md) for detailed PostgreSQL setup guide including Docker instructions.**

### MySQL Configuration

To use MySQL, update `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bankdb
spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

### Environment Variables

You can override any property using environment variables:

```bash
# Server Port
SERVER_PORT=8081

# Database Configuration
DB_URL=jdbc:mysql://localhost:3306/bankdb
DB_USERNAME=root
DB_PASSWORD=yourpassword

# JWT Configuration
JWT_SECRET=your-secret-key
JWT_EXPIRATION=86400000

# Logging
LOG_LEVEL=DEBUG
```

## API Endpoints

### Authentication

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/auth/register` | Register new customer | No |
| POST | `/api/auth/login` | Login and get JWT token | No |
| GET | `/api/auth/dashboard` | Get customer dashboard | Yes |
| POST | `/api/auth/logout` | Logout | Yes |

### Account Management

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/accounts` | Create new account | Yes |
| GET | `/api/accounts` | Get all accounts | Yes |
| GET | `/api/accounts/{id}` | Get account by ID | Yes |
| PUT | `/api/accounts/{id}` | Update account | Yes |
| DELETE | `/api/accounts/{id}` | Close account | Yes |
| GET | `/api/accounts/search` | Search/filter accounts | Yes |

### Transactions

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/transactions/deposit` | Deposit money | Yes |
| POST | `/api/transactions/withdraw` | Withdraw money | Yes |
| POST | `/api/transactions/transfer` | Transfer funds | Yes |
| GET | `/api/transactions/statement` | Get account statement | Yes |
| GET | `/api/transactions/history` | Get transaction history | Yes |

## Testing with Postman

### Step 1: Register a Customer

```http
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "username": "johndoe",
  "password": "password123",
  "phone": "1234567890",
  "address": "123 Main Street, New York, NY 10001"
}
```

### Step 2: Login

```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "username": "johndoe",
  "password": "password123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "tokenType": "Bearer",
    "customer": {...}
  }
}
```

**Copy the token from the response!**

### Step 3: Use Token for Authenticated Requests

For all subsequent requests, add the token to the Authorization header:

```
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

### Step 4: Create an Account

```http
POST http://localhost:8080/api/accounts
Authorization: Bearer YOUR_TOKEN_HERE
Content-Type: application/json

{
  "accountName": "My Savings Account",
  "accountType": "SAVINGS"
}
```

### Step 5: Deposit Money

```http
POST http://localhost:8080/api/transactions/deposit
Authorization: Bearer YOUR_TOKEN_HERE
Content-Type: application/json

{
  "accountNumber": "ACC20240124...",
  "amount": 1000.00,
  "description": "Initial deposit"
}
```

### Step 6: Get Dashboard

```http
GET http://localhost:8080/api/auth/dashboard
Authorization: Bearer YOUR_TOKEN_HERE
```

### Step 7: Get Account Statement

```http
GET http://localhost:8080/api/transactions/statement?accountNumber=ACC20240124...&page=0&size=10
Authorization: Bearer YOUR_TOKEN_HERE
```

## Complete Testing Flow

1. **Register** → Get customer created
2. **Login** → Get JWT token
3. **Create Account** → Get account number
4. **Deposit Money** → Add balance
5. **View Dashboard** → See summary
6. **Withdraw Money** → Reduce balance
7. **Create Second Account** → For transfers
8. **Transfer Funds** → Between accounts
9. **Get Statement** → View transactions
10. **Search Accounts** → Filter results

## Request/Response Examples

### Register Customer Request
```json
{
  "firstName": "Jane",
  "lastName": "Smith",
  "email": "jane.smith@example.com",
  "username": "janesmith",
  "password": "secure123",
  "phone": "9876543210",
  "address": "456 Oak Avenue, Los Angeles, CA 90001"
}
```

### Create Account Request
```json
{
  "accountName": "Emergency Fund",
  "accountType": "SAVINGS"
}
```

**Account Types:** `SAVINGS`, `CURRENT`, `FIXED_DEPOSIT`, `SALARY`

### Deposit Request
```json
{
  "accountNumber": "ACC20240124143022001234",
  "amount": 5000.00,
  "description": "Salary deposit"
}
```

### Withdraw Request
```json
{
  "accountNumber": "ACC20240124143022001234",
  "amount": 500.00,
  "description": "ATM withdrawal"
}
```

### Fund Transfer Request
```json
{
  "fromAccountNumber": "ACC20240124143022001234",
  "toAccountNumber": "ACC20240124143055005678",
  "amount": 1000.00,
  "description": "Transfer to savings"
}
```

## Error Handling

All errors follow a consistent format:

```json
{
  "success": false,
  "message": "Error message here",
  "errors": ["Detailed error 1", "Detailed error 2"],
  "timestamp": "2024-01-24T14:30:00"
}
```

**HTTP Status Codes:**
- `200` - Success
- `201` - Created
- `400` - Bad Request (validation errors, business logic errors)
- `401` - Unauthorized (invalid/missing token)
- `404` - Not Found
- `500` - Internal Server Error

## Running Tests

```bash
mvn test
```

## Project Structure

```
bank-application/
├── src/
│   ├── main/
│   │   ├── java/com/bank/
│   │   │   ├── aspect/          # AOP logging
│   │   │   ├── config/          # Configuration classes
│   │   │   ├── controller/      # REST controllers
│   │   │   ├── dto/             # Request/Response DTOs
│   │   │   ├── entity/          # JPA entities
│   │   │   ├── exception/       # Custom exceptions
│   │   │   ├── repository/      # Data repositories
│   │   │   ├── security/        # JWT & Security
│   │   │   ├── service/         # Business logic
│   │   │   ├── util/            # Utility classes
│   │   │   └── BankApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/bank/
│           └── service/         # Unit tests
└── pom.xml
```

## Database Schema

### Tables Created Automatically

1. **customers** - Customer information
2. **accounts** - Bank account details
3. **transactions** - Transaction records

## Security

- Passwords are encrypted using BCrypt
- JWT tokens expire after 24 hours (configurable)
- All endpoints except `/api/auth/**` require authentication
- CSRF protection disabled for REST API
- Stateless session management

## Troubleshooting

### Application won't start
- Check if port 8080 is available
- Verify Java 17+ is installed: `java -version`
- Check Maven installation: `mvn -version`

### Database errors
- For H2: No action needed, in-memory DB auto-creates
- For MySQL: Ensure database exists and credentials are correct

### JWT token errors
- Token expired: Login again to get new token
- Invalid token: Check Authorization header format: `Bearer <token>`

## Support

For issues or questions, check:
- Swagger UI for API documentation
- Application logs for detailed error messages
- H2 Console for database inspection

## License

This project is for educational/assessment purposes.
#   b a n k - a p p l i c a t i o n - s p r i n g - b o o t 
 
 