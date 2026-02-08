# 🚀 Bank Application - Testing Guide

## Application is Running Successfully! ✅

**Application URL:** http://localhost:8081  
**Swagger UI:** http://localhost:8081/swagger-ui.html  
**H2 Console:** http://localhost:8081/h2-console

---

## Quick Test in Postman

### 1️⃣ Register a New Customer

```http
POST http://localhost:8081/api/auth/register
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

**Expected Response (201 Created):**
```json
{
  "success": true,
  "message": "Customer registered successfully",
  "data": {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "username": "johndoe",
    "phone": "1234567890",
    "address": "123 Main Street, New York, NY 10001",
    "status": "ACTIVE",
    "createdAt": "2024-01-24T09:30:00",
    "updatedAt": "2024-01-24T09:30:00"
  },
  "errors": null,
  "timestamp": "2024-01-24T09:30:00"
}
```

---

### 2️⃣ Login

```http
POST http://localhost:8081/api/auth/login
Content-Type: application/json

{
  "username": "johndoe",
  "password": "password123"
}
```

**Expected Response (200 OK):**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzA2MDg2MjAwLCJleHAiOjE3MDYxNzI2MDB9.xxxx",
    "tokenType": "Bearer",
    "customer": {
      "id": 1,
      "firstName": "John",
      "lastName": "Doe",
      ...
    }
  }
}
```

**⚠️ IMPORTANT: Copy the `token` value from the response!**

---

### 3️⃣ Get Dashboard (Requires Authentication)

```http
GET http://localhost:8081/api/auth/dashboard
Authorization: Bearer YOUR_TOKEN_HERE
```

Replace `YOUR_TOKEN_HERE` with the actual token from login response.

**Expected Response (200 OK):**
```json
{
  "success": true,
  "message": "Dashboard fetched successfully",
  "data": {
    "customer": { ... },
    "totalAccounts": 0,
    "totalBalance": 0.00,
    "totalTransactions": 0
  }
}
```

---

### 4️⃣ Create Bank Account

```http
POST http://localhost:8081/api/accounts
Authorization: Bearer YOUR_TOKEN_HERE
Content-Type: application/json

{
  "accountName": "My Savings Account",
  "accountType": "SAVINGS"
}
```

**Account Types:** `SAVINGS`, `CURRENT`, `FIXED_DEPOSIT`, `SALARY`

**Expected Response (201 Created):**
```json
{
  "success": true,
  "message": "Account created successfully",
  "data": {
    "id": 1,
    "accountNumber": "ACC20240124093000001234",
    "accountName": "My Savings Account",
    "accountType": "SAVINGS",
    "balance": 0.00,
    "status": "ACTIVE",
    "createdAt": "2024-01-24T09:30:00",
    "updatedAt": "2024-01-24T09:30:00"
  }
}
```

**⚠️ IMPORTANT: Copy the `accountNumber` value!**

---

### 5️⃣ Deposit Money

```http
POST http://localhost:8081/api/transactions/deposit
Authorization: Bearer YOUR_TOKEN_HERE
Content-Type: application/json

{
  "accountNumber": "ACC20240124093000001234",
  "amount": 10000.00,
  "description": "Initial deposit"
}
```

**Expected Response (201 Created):**
```json
{
  "success": true,
  "message": "Deposit successful",
  "data": {
    "id": 1,
    "transactionId": "TXN20240124093100012345",
    "type": "DEPOSIT",
    "amount": 10000.00,
    "balanceBefore": 0.00,
    "balanceAfter": 10000.00,
    "description": "Initial deposit",
    "status": "SUCCESS",
    "accountNumber": "ACC20240124093000001234",
    "createdAt": "2024-01-24T09:31:00"
  }
}
```

---

### 6️⃣ Withdraw Money

```http
POST http://localhost:8081/api/transactions/withdraw
Authorization: Bearer YOUR_TOKEN_HERE
Content-Type: application/json

{
  "accountNumber": "ACC20240124093000001234",
  "amount": 2000.00,
  "description": "ATM withdrawal"
}
```

**Expected Response (201 Created):**
```json
{
  "success": true,
  "message": "Withdrawal successful",
  "data": {
    "id": 2,
    "transactionId": "TXN20240124093200012346",
    "type": "WITHDRAWAL",
    "amount": 2000.00,
    "balanceBefore": 10000.00,
    "balanceAfter": 8000.00,
    "description": "ATM withdrawal",
    "status": "SUCCESS",
    "accountNumber": "ACC20240124093000001234",
    "createdAt": "2024-01-24T09:32:00"
  }
}
```

---

### 7️⃣ Get Account Statement

```http
GET http://localhost:8081/api/transactions/statement?accountNumber=ACC20240124093000001234&page=0&size=10
Authorization: Bearer YOUR_TOKEN_HERE
```

**Expected Response (200 OK):**
```json
{
  "success": true,
  "message": "Account statement fetched successfully",
  "data": {
    "content": [
      {
        "id": 2,
        "transactionId": "TXN20240124093200012346",
        "type": "WITHDRAWAL",
        "amount": 2000.00,
        "balanceBefore": 10000.00,
        "balanceAfter": 8000.00,
        "description": "ATM withdrawal",
        "status": "SUCCESS",
        "createdAt": "2024-01-24T09:32:00"
      },
      {
        "id": 1,
        "transactionId": "TXN20240124093100012345",
        "type": "DEPOSIT",
        "amount": 10000.00,
        "balanceBefore": 0.00,
        "balanceAfter": 10000.00,
        "description": "Initial deposit",
        "status": "SUCCESS",
        "createdAt": "2024-01-24T09:31:00"
      }
    ],
    "pageNumber": 0,
    "pageSize": 10,
    "totalElements": 2,
    "totalPages": 1,
    "last": true,
    "first": true
  }
}
```

---

### 8️⃣ Create Second Account (for Fund Transfer)

```http
POST http://localhost:8081/api/accounts
Authorization: Bearer YOUR_TOKEN_HERE
Content-Type: application/json

{
  "accountName": "My Current Account",
  "accountType": "CURRENT"
}
```

Copy the new `accountNumber` from the response.

---

### 9️⃣ Fund Transfer

```http
POST http://localhost:8081/api/transactions/transfer
Authorization: Bearer YOUR_TOKEN_HERE
Content-Type: application/json

{
  "fromAccountNumber": "ACC20240124093000001234",
  "toAccountNumber": "ACC20240124093500005678",
  "amount": 1000.00,
  "description": "Transfer to current account"
}
```

**Expected Response (201 Created):**
```json
{
  "success": true,
  "message": "Fund transfer successful",
  "data": {
    "id": 3,
    "transactionId": "TXN20240124093600012347",
    "type": "TRANSFER_OUT",
    "amount": 1000.00,
    "balanceBefore": 8000.00,
    "balanceAfter": 7000.00,
    "description": "Transfer to current account",
    "status": "SUCCESS",
    "fromAccountNumber": "ACC20240124093000001234",
    "toAccountNumber": "ACC20240124093500005678",
    "createdAt": "2024-01-24T09:36:00"
  }
}
```

---

### 🔟 View All Accounts

```http
GET http://localhost:8081/api/accounts
Authorization: Bearer YOUR_TOKEN_HERE
```

**Expected Response (200 OK):**
```json
{
  "success": true,
  "message": "Accounts fetched successfully",
  "data": [
    {
      "id": 1,
      "accountNumber": "ACC20240124093000001234",
      "accountName": "My Savings Account",
      "accountType": "SAVINGS",
      "balance": 7000.00,
      "status": "ACTIVE",
      ...
    },
    {
      "id": 2,
      "accountNumber": "ACC20240124093500005678",
      "accountName": "My Current Account",
      "accountType": "CURRENT",
      "balance": 1000.00,
      "status": "ACTIVE",
      ...
    }
  ]
}
```

---

## 📝 Import Postman Collection

1. Open Postman
2. Click **Import** button
3. Select the file: `Bank-API-Postman-Collection.json`
4. Create environment and add variables:
   - `jwt_token` (will auto-populate after login)
   - `account_number` (will auto-populate after creating account)

---

## 🗄️ Access H2 Database Console

1. Open: http://localhost:8081/h2-console
2. Enter:
   - **JDBC URL:** `jdbc:h2:mem:bankdb`
   - **Username:** `sa`
   - **Password:** `password`
3. Click **Connect**
4. Run queries:
   ```sql
   SELECT * FROM CUSTOMERS;
   SELECT * FROM ACCOUNTS;
   SELECT * FROM TRANSACTIONS;
   ```

---

## 📚 API Documentation (Swagger)

Open Swagger UI: http://localhost:8081/swagger-ui.html

You can test all APIs directly from Swagger:
1. Click on any endpoint
2. Click **Try it out**
3. For authenticated endpoints:
   - Click **Authorize** button at top
   - Enter: `Bearer YOUR_TOKEN_HERE`
   - Click **Authorize**
4. Fill in request parameters
5. Click **Execute**

---

## ✅ Test Validation

Try these to see validation in action:

### Invalid Email
```json
{
  "email": "invalid-email"
}
```
**Response:** `Email must be valid`

### Short Password
```json
{
  "password": "123"
}
```
**Response:** `Password must be at least 6 characters`

### Invalid Phone
```json
{
  "phone": "12345"
}
```
**Response:** `Phone must be 10 digits`

### Negative Amount
```json
{
  "amount": -100
}
```
**Response:** `Amount must be greater than 0`

---

## 🛠️ Common Issues

### Token Expired
- Login again to get a new token
- Tokens are valid for 24 hours

### Insufficient Balance
- Check account balance before withdrawal/transfer
- View dashboard or account statement

### Account Not Found
- Verify account number is correct
- Check if you're using your own account

### Unauthorized (401)
- Ensure you added the token in Authorization header
- Format: `Bearer <token>`

---

## 🎯 Complete Test Checklist

- [ ] Register customer
- [ ] Login and get JWT token
- [ ] View dashboard
- [ ] Create savings account
- [ ] Deposit money
- [ ] View account statement
- [ ] Withdraw money
- [ ] Create second account
- [ ] Transfer funds between accounts
- [ ] Search/filter accounts
- [ ] Get transaction history with date range
- [ ] Try validation errors
- [ ] Access Swagger UI
- [ ] Access H2 Console

---

## 📊 Testing Metrics

Your application includes:
- ✅ **10 Core Requirements** - All implemented
- ✅ **7 Bonus Features** - All implemented
- ✅ **JWT Authentication** - Working
- ✅ **Input Validation** - All fields validated
- ✅ **AOP Logging** - All API calls logged
- ✅ **Global Exception Handling** - Consistent error format
- ✅ **Swagger Documentation** - Complete
- ✅ **Unit Tests** - Service layer covered
- ✅ **H2 Database** - Auto-configured
- ✅ **Layered Architecture** - Properly implemented

---

## 🎉 Success!

Your Bank Application is fully functional and ready for testing! All endpoints are working, authentication is secure, and the application follows best practices.

**Happy Testing! 🚀**
