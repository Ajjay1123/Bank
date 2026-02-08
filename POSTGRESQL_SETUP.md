# PostgreSQL Setup Guide

This guide explains how to configure and run the Bank Application with PostgreSQL database.

## Prerequisites

1. **Install PostgreSQL**
   - Download from: https://www.postgresql.org/download/
   - Or use Docker: `docker run --name postgres-bank -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres`

2. **Create Database**
   ```sql
   -- Connect to PostgreSQL (using psql or any client)
   CREATE DATABASE bankdb;
   ```

## Configuration Options

### Option 1: Using Spring Profile (Recommended)

The project includes a PostgreSQL-specific configuration profile.

**Run with PostgreSQL profile:**
```bash
# Using Maven
mvn spring-boot:run -Dspring-boot.run.profiles=postgresql

# Or set environment variable
set SPRING_PROFILES_ACTIVE=postgresql  # Windows
export SPRING_PROFILES_ACTIVE=postgresql  # Linux/Mac

# Then run normally
mvn spring-boot:run
```

**Default PostgreSQL settings** (in `application-postgresql.properties`):
- URL: `jdbc:postgresql://localhost:5432/bankdb`
- Username: `postgres`
- Password: `postgres`

### Option 2: Using Environment Variables

Set these environment variables before running the application:

**Windows (PowerShell):**
```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/bankdb"
$env:DB_DRIVER="org.postgresql.Driver"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="your_password"
$env:JPA_PLATFORM="org.hibernate.dialect.PostgreSQLDialect"
$env:H2_CONSOLE_ENABLED="false"
```

**Windows (Command Prompt):**
```cmd
set DB_URL=jdbc:postgresql://localhost:5432/bankdb
set DB_DRIVER=org.postgresql.Driver
set DB_USERNAME=postgres
set DB_PASSWORD=your_password
set JPA_PLATFORM=org.hibernate.dialect.PostgreSQLDialect
set H2_CONSOLE_ENABLED=false
```

**Linux/Mac:**
```bash
export DB_URL=jdbc:postgresql://localhost:5432/bankdb
export DB_DRIVER=org.postgresql.Driver
export DB_USERNAME=postgres
export DB_PASSWORD=your_password
export JPA_PLATFORM=org.hibernate.dialect.PostgreSQLDialect
export H2_CONSOLE_ENABLED=false
```

### Option 3: Modify application.properties

Edit `src/main/resources/application.properties` and change the defaults:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/bankdb
spring.datasource.driverClassName=org.postgresql.Driver
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.h2.console.enabled=false
```

## Docker PostgreSQL Setup

### Quick Start with Docker

```bash
# Pull and run PostgreSQL container
docker run --name postgres-bank \
  -e POSTGRES_DB=bankdb \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  -d postgres:latest

# Verify container is running
docker ps

# View PostgreSQL logs
docker logs postgres-bank
```

### Docker Compose (Optional)

Create `docker-compose.yml`:

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:latest
    container_name: postgres-bank
    environment:
      POSTGRES_DB: bankdb
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    restart: unless-stopped

volumes:
  postgres_data:
```

Run with: `docker-compose up -d`

## Verifying Database Connection

### Using psql

```bash
# Connect to database
psql -h localhost -U postgres -d bankdb

# List tables (after running the application)
\dt

# View table structure
\d customers
\d accounts
\d transactions
```

### Using pgAdmin

1. Download pgAdmin: https://www.pgadmin.org/
2. Create new server connection:
   - Host: localhost
   - Port: 5432
   - Database: bankdb
   - Username: postgres
   - Password: your_password

## Running the Application

1. **Start PostgreSQL** (if not already running)

2. **Build the project:**
   ```bash
   mvn clean install
   ```

3. **Run with PostgreSQL:**
   ```bash
   # Using profile
   mvn spring-boot:run -Dspring-boot.run.profiles=postgresql
   
   # Or with environment variables set
   mvn spring-boot:run
   ```

4. **Verify startup:**
   - Check console for: "Started BankApplication"
   - No database connection errors
   - Tables created automatically (if ddl-auto=update)

## Database Schema

The application will automatically create these tables:

- **customers** - User accounts and authentication
- **accounts** - Bank accounts with balance
- **transactions** - Transaction history

### DDL Mode Options

In `application.properties` or environment variable `JPA_DDL_AUTO`:

- `update` (default) - Updates schema automatically
- `create` - Drops and recreates schema on startup
- `create-drop` - Creates schema on startup, drops on shutdown
- `validate` - Only validates schema
- `none` - No schema management

## Switching Between Databases

### Development: H2 (Default)
```bash
mvn spring-boot:run
```

### Testing/Production: PostgreSQL
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=postgresql
```

## Troubleshooting

### Connection Refused

**Error:** `Connection to localhost:5432 refused`

**Solution:**
- Verify PostgreSQL is running: `docker ps` or check services
- Check port 5432 is not in use: `netstat -an | findstr 5432` (Windows) or `lsof -i :5432` (Mac/Linux)

### Authentication Failed

**Error:** `password authentication failed for user "postgres"`

**Solution:**
- Verify username and password in configuration
- Check PostgreSQL pg_hba.conf for authentication settings

### Database Does Not Exist

**Error:** `database "bankdb" does not exist`

**Solution:**
```bash
# Connect to PostgreSQL
psql -h localhost -U postgres

# Create database
CREATE DATABASE bankdb;
```

### Driver Not Found

**Error:** `No suitable driver found for jdbc:postgresql`

**Solution:**
- Run `mvn clean install` to download PostgreSQL driver
- Verify `postgresql` dependency in pom.xml

## Production Considerations

1. **Use connection pooling** (HikariCP is included by default)
2. **Set appropriate ddl-auto** (use `validate` or `none` in production)
3. **Enable SSL** for database connections
4. **Use secrets management** for passwords (not hardcoded)
5. **Regular backups** of PostgreSQL database
6. **Monitor connection pool** and query performance

### Production Configuration Example

```properties
spring.datasource.url=jdbc:postgresql://prod-server:5432/bankdb?ssl=true&sslmode=require
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false

# Connection Pool Settings
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
```

## Additional Resources

- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Spring Data JPA Documentation](https://spring.io/projects/spring-data-jpa)
- [Hibernate PostgreSQL Dialect](https://docs.jboss.org/hibernate/orm/current/javadocs/org/hibernate/dialect/PostgreSQLDialect.html)
