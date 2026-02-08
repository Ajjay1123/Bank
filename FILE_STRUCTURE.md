# 📁 Bank Application - Complete File Structure

## Project Root
```
bank-application/
├── pom.xml                                    # Maven configuration
├── .gitignore                                 # Git ignore rules
├── README.md                                  # Main documentation
├── QUICK_START.md                            # Quick setup guide
├── TESTING_GUIDE.md                          # API testing examples
├── PROJECT_SUMMARY.md                        # Implementation summary
├── FINAL_VERIFICATION.md                     # Verification checklist
├── Bank-API-Postman-Collection.json         # Postman collection
└── src/
    ├── main/
    │   ├── java/com/bank/
    │   │   ├── BankApplication.java          # Main application class
    │   │   │
    │   │   ├── aspect/
    │   │   │   └── LoggingAspect.java       # AOP logging
    │   │   │
    │   │   ├── config/
    │   │   │   ├── SecurityConfig.java       # Security configuration
    │   │   │   └── OpenApiConfig.java        # Swagger configuration
    │   │   │
    │   │   ├── controller/
    │   │   │   ├── AuthController.java       # Auth endpoints
    │   │   │   ├── AccountController.java    # Account endpoints
    │   │   │   └── TransactionController.java # Transaction endpoints
    │   │   │
    │   │   ├── dto/
    │   │   │   ├── request/
    │   │   │   │   ├── CustomerRegistrationRequest.java
    │   │   │   │   ├── LoginRequest.java
    │   │   │   │   ├── AccountRequest.java
    │   │   │   │   ├── DepositRequest.java
    │   │   │   │   ├── WithdrawalRequest.java
    │   │   │   │   └── FundTransferRequest.java
    │   │   │   │
    │   │   │   └── response/
    │   │   │       ├── CustomerResponse.java
    │   │   │       ├── LoginResponse.java
    │   │   │       ├── AccountResponse.java
    │   │   │       ├── TransactionResponse.java
    │   │   │       ├── DashboardResponse.java
    │   │   │       ├── PagedResponse.java
    │   │   │       └── ApiResponse.java
    │   │   │
    │   │   ├── entity/
    │   │   │   ├── Customer.java             # Customer entity
    │   │   │   ├── Account.java              # Account entity
    │   │   │   └── Transaction.java          # Transaction entity
    │   │   │
    │   │   ├── exception/
    │   │   │   ├── ResourceNotFoundException.java
    │   │   │   ├── BadRequestException.java
    │   │   │   ├── InsufficientBalanceException.java
    │   │   │   ├── AccountInactiveException.java
    │   │   │   └── GlobalExceptionHandler.java
    │   │   │
    │   │   ├── repository/
    │   │   │   ├── CustomerRepository.java   # Customer data access
    │   │   │   ├── AccountRepository.java    # Account data access
    │   │   │   └── TransactionRepository.java # Transaction data access
    │   │   │
    │   │   ├── security/
    │   │   │   ├── UserPrincipal.java        # User details
    │   │   │   ├── CustomUserDetailsService.java
    │   │   │   ├── JwtTokenProvider.java     # JWT generation
    │   │   │   ├── JwtAuthenticationFilter.java
    │   │   │   └── JwtAuthenticationEntryPoint.java
    │   │   │
    │   │   ├── service/
    │   │   │   ├── AuthService.java          # Auth business logic
    │   │   │   ├── AccountService.java       # Account business logic
    │   │   │   └── TransactionService.java   # Transaction business logic
    │   │   │
    │   │   └── util/
    │   │       ├── ResponseMapper.java       # DTO mapping
    │   │       └── AccountNumberGenerator.java # ID generation
    │   │
    │   └── resources/
    │       └── application.properties         # Configuration
    │
    └── test/
        └── java/com/bank/service/
            ├── AccountServiceTest.java        # Account service tests
            └── TransactionServiceTest.java    # Transaction service tests
```

## File Count Summary

| Category | Count |
|----------|-------|
| **Java Source Files** | 41 |
| **Test Files** | 2 |
| **Configuration Files** | 2 (pom.xml, application.properties) |
| **Documentation Files** | 6 |
| **Total Files** | 51 |

## Lines of Code (Approximate)

| Category | Lines |
|----------|-------|
| **Entity Classes** | ~300 |
| **Controllers** | ~400 |
| **Services** | ~600 |
| **Repositories** | ~150 |
| **DTOs** | ~400 |
| **Security** | ~500 |
| **Exception Handling** | ~300 |
| **Configuration** | ~200 |
| **Tests** | ~400 |
| **Utilities** | ~150 |
| **Total Code** | ~3,400+ |

## File Descriptions

### Core Application Files

1. **BankApplication.java**
   - Main Spring Boot application class
   - Enables JPA auditing
   - Application entry point

### Controllers (REST API)

2. **AuthController.java**
   - Register, Login, Dashboard, Logout endpoints
   - Returns JWT tokens
   - Customer authentication

3. **AccountController.java**
   - CRUD operations for accounts
   - Search and filter functionality
   - Account management

4. **TransactionController.java**
   - Deposit, Withdraw, Transfer operations
   - Account statement retrieval
   - Transaction history with date range

### Services (Business Logic)

5. **AuthService.java**
   - Customer registration logic
   - Login authentication
   - Dashboard data aggregation

6. **AccountService.java**
   - Account creation and management
   - Balance validation
   - Search and filter implementation

7. **TransactionService.java**
   - Transaction processing
   - Balance updates
   - Fund transfer logic

### Repositories (Data Access)

8. **CustomerRepository.java**
   - Customer CRUD operations
   - Username and email lookups
   - Custom queries

9. **AccountRepository.java**
   - Account CRUD operations
   - Account number lookup
   - Search queries with pagination

10. **TransactionRepository.java**
    - Transaction CRUD operations
    - Statement queries with pagination
    - Date range filtering

### Entities (Database Models)

11. **Customer.java**
    - Customer data model
    - One-to-many with accounts
    - Auditing enabled

12. **Account.java**
    - Account data model
    - Many-to-one with customer
    - One-to-many with transactions

13. **Transaction.java**
    - Transaction data model
    - Many-to-one with account
    - Supports transfers

### DTOs (Data Transfer Objects)

**Request DTOs:**
14. CustomerRegistrationRequest.java - Customer signup
15. LoginRequest.java - Login credentials
16. AccountRequest.java - Account creation/update
17. DepositRequest.java - Deposit operation
18. WithdrawalRequest.java - Withdrawal operation
19. FundTransferRequest.java - Fund transfer

**Response DTOs:**
20. CustomerResponse.java - Customer data
21. LoginResponse.java - Login with token
22. AccountResponse.java - Account data
23. TransactionResponse.java - Transaction data
24. DashboardResponse.java - Dashboard summary
25. PagedResponse.java - Paginated results
26. ApiResponse.java - Standard API response

### Security

27. **UserPrincipal.java** - Spring Security user details
28. **CustomUserDetailsService.java** - User loading
29. **JwtTokenProvider.java** - JWT creation and validation
30. **JwtAuthenticationFilter.java** - Request filtering
31. **JwtAuthenticationEntryPoint.java** - Auth error handling

### Configuration

32. **SecurityConfig.java** - Security rules and JWT setup
33. **OpenApiConfig.java** - Swagger/OpenAPI configuration

### Exception Handling

34. **ResourceNotFoundException.java** - 404 errors
35. **BadRequestException.java** - 400 errors
36. **InsufficientBalanceException.java** - Balance errors
37. **AccountInactiveException.java** - Status errors
38. **GlobalExceptionHandler.java** - Centralized error handling

### Utilities

39. **ResponseMapper.java** - Entity to DTO mapping
40. **AccountNumberGenerator.java** - Unique ID generation

### AOP

41. **LoggingAspect.java** - Automatic API logging

### Tests

42. **AccountServiceTest.java** - Account service unit tests
43. **TransactionServiceTest.java** - Transaction service unit tests

### Configuration Files

44. **pom.xml** - Maven dependencies and build configuration
45. **application.properties** - Application settings

### Documentation

46. **README.md** - Complete project documentation
47. **QUICK_START.md** - Quick setup guide
48. **TESTING_GUIDE.md** - API testing examples
49. **PROJECT_SUMMARY.md** - Implementation summary
50. **FINAL_VERIFICATION.md** - Verification checklist
51. **Bank-API-Postman-Collection.json** - Postman collection

## Dependencies (Key Libraries)

```xml
Spring Boot 3.2.1
  ├── spring-boot-starter-web
  ├── spring-boot-starter-data-jpa
  ├── spring-boot-starter-security
  ├── spring-boot-starter-validation
  ├── spring-boot-starter-aop
  └── spring-boot-starter-test

JWT (0.12.3)
  ├── jjwt-api
  ├── jjwt-impl
  └── jjwt-jackson

Database
  ├── h2database
  └── mysql-connector-j

Documentation
  └── springdoc-openapi-starter-webmvc-ui (2.3.0)

Utilities
  └── lombok
```

## API Endpoints Reference

### Authentication (4)
- POST /api/auth/register
- POST /api/auth/login
- GET /api/auth/dashboard
- POST /api/auth/logout

### Accounts (6)
- POST /api/accounts
- GET /api/accounts
- GET /api/accounts/{id}
- PUT /api/accounts/{id}
- DELETE /api/accounts/{id}
- GET /api/accounts/search

### Transactions (5)
- POST /api/transactions/deposit
- POST /api/transactions/withdraw
- POST /api/transactions/transfer
- GET /api/transactions/statement
- GET /api/transactions/history

## Database Tables

1. **customers** - User accounts
2. **accounts** - Bank accounts
3. **transactions** - Transaction records

## How Files Work Together

```
User Request
    ↓
Controller (REST endpoint)
    ↓
DTO (Request validation)
    ↓
Service (Business logic)
    ↓
Repository (Database access)
    ↓
Entity (Database model)
    ↓
Repository (Query results)
    ↓
Service (Process data)
    ↓
Mapper (Entity → DTO)
    ↓
Controller (Response)
    ↓
API Response (JSON)
```

## Cross-Cutting Concerns

- **Security:** JwtAuthenticationFilter intercepts all requests
- **Logging:** LoggingAspect logs all controller and service calls
- **Exception Handling:** GlobalExceptionHandler catches all exceptions
- **Validation:** Bean Validation validates all request DTOs

## Quick File Navigation

Need to modify:
- **API endpoints?** → controllers/
- **Business logic?** → services/
- **Database queries?** → repositories/
- **Data models?** → entities/
- **Request/Response format?** → dto/
- **Security rules?** → config/SecurityConfig.java
- **Error handling?** → exception/GlobalExceptionHandler.java
- **Logging behavior?** → aspect/LoggingAspect.java
- **Configuration?** → resources/application.properties

---

**Total Project Size:** ~3,400+ lines of code across 51 files  
**All files are organized following Spring Boot best practices**
