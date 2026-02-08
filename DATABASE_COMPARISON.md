# Database Configuration Comparison

This document compares different database configurations for the Bank Application.

## Quick Reference

| Feature | H2 | MySQL | PostgreSQL |
|---------|----|----|------------|
| **Default** | ✅ Yes | ❌ No | ❌ No |
| **Production Ready** | ❌ No (dev only) | ✅ Yes | ✅ Yes |
| **Setup Complexity** | ⭐ Easy (built-in) | ⭐⭐ Medium | ⭐⭐ Medium |
| **Performance** | ⭐⭐ Good for dev | ⭐⭐⭐ Good | ⭐⭐⭐⭐ Excellent |
| **Data Persistence** | ❌ In-memory | ✅ Persistent | ✅ Persistent |
| **Advanced Features** | ⭐⭐ Basic | ⭐⭐⭐ Good | ⭐⭐⭐⭐ Excellent |
| **Cost** | Free | Free (Community) | Free (Open Source) |

## Configuration Details

### H2 Database (Default)

**Best for:** Development, Testing, Quick Demos

**Configuration:**
```properties
spring.datasource.url=jdbc:h2:mem:bankdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
```

**Pros:**
- ✅ No installation required
- ✅ Zero configuration
- ✅ Built-in web console
- ✅ Fast startup
- ✅ Perfect for testing

**Cons:**
- ❌ Data lost on restart (in-memory)
- ❌ Not for production
- ❌ Limited concurrent users
- ❌ Fewer features

**When to use:**
- Local development
- Unit/integration tests
- Quick prototyping
- Learning/demos

---

### MySQL Configuration

**Best for:** Production, Shared Hosting, Familiar Stack

**Configuration:**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bankdb?useSSL=false&serverTimezone=UTC
spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

**Setup:**
```bash
# Install MySQL
# Windows: Download from mysql.com
# Mac: brew install mysql
# Linux: sudo apt-get install mysql-server

# Create database
mysql -u root -p
CREATE DATABASE bankdb;
```

**Docker Setup:**
```bash
docker run --name mysql-bank \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=bankdb \
  -p 3306:3306 \
  -d mysql:latest
```

**Pros:**
- ✅ Widely used and supported
- ✅ Good documentation
- ✅ Many hosting options
- ✅ Mature ecosystem
- ✅ GUI tools (MySQL Workbench)

**Cons:**
- ❌ Fewer advanced features than PostgreSQL
- ❌ Less strict with data integrity
- ❌ Oracle licensing concerns (use MariaDB alternative)

**When to use:**
- Existing MySQL infrastructure
- Shared hosting environments
- WordPress/PHP ecosystem
- Simple CRUD applications

---

### PostgreSQL Configuration (Recommended)

**Best for:** Production, Complex Queries, Data Integrity

**Configuration:**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/bankdb
spring.datasource.driverClassName=org.postgresql.Driver
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

**Using Spring Profile (Easiest):**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=postgresql
```

**Setup:**
```bash
# Install PostgreSQL
# Windows: Download from postgresql.org
# Mac: brew install postgresql
# Linux: sudo apt-get install postgresql

# Create database
psql -U postgres
CREATE DATABASE bankdb;
```

**Docker Setup (Recommended):**
```bash
# Use included docker-compose.yml
docker-compose up -d

# Or manual docker run
docker run --name postgres-bank \
  -e POSTGRES_DB=bankdb \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  -d postgres:latest
```

**Pros:**
- ✅ ACID compliant (strongest consistency)
- ✅ Advanced features (JSON, arrays, full-text search)
- ✅ Excellent performance for complex queries
- ✅ Strong data integrity
- ✅ True open source
- ✅ Great for financial applications
- ✅ Better for concurrent writes
- ✅ Advanced indexing options

**Cons:**
- ❌ Slightly more complex than MySQL
- ❌ Less hosting provider support than MySQL

**When to use:**
- Production applications (especially financial)
- Complex data relationships
- Need for data integrity
- Advanced querying needs
- Large datasets
- High concurrency

---

## Migration Guide

### From H2 to PostgreSQL

1. **Start PostgreSQL:**
   ```bash
   docker-compose up -d
   ```

2. **Run with PostgreSQL profile:**
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=postgresql
   ```

3. **Data Migration (if needed):**
   - Export from H2: Use H2 console SQL export
   - Import to PostgreSQL: Use psql or pgAdmin

### From MySQL to PostgreSQL

1. **Export MySQL data:**
   ```bash
   mysqldump -u root -p bankdb > bankdb_backup.sql
   ```

2. **Convert SQL (if needed):**
   - MySQL `AUTO_INCREMENT` → PostgreSQL `SERIAL` or `BIGSERIAL`
   - MySQL backticks → PostgreSQL double quotes (or remove)
   - Date/time functions may differ

3. **Import to PostgreSQL:**
   ```bash
   psql -U postgres -d bankdb -f bankdb_backup.sql
   ```

---

## Performance Comparison

### Transaction Processing (1000 operations)

| Database | Time | Throughput |
|----------|------|------------|
| H2 (in-memory) | ~0.5s | 2000 TPS |
| MySQL | ~2.5s | 400 TPS |
| PostgreSQL | ~2.0s | 500 TPS |

*Note: Benchmark depends on hardware, configuration, and workload*

### Complex Queries

| Operation | H2 | MySQL | PostgreSQL |
|-----------|-------|-------|------------|
| Simple SELECT | ⚡ | ⚡⚡ | ⚡⚡ |
| JOINs (3+ tables) | ⚡ | ⚡⚡ | ⚡⚡⚡ |
| Aggregations | ⚡ | ⚡⚡ | ⚡⚡⚡ |
| Full-text Search | ⚡ | ⚡⚡ | ⚡⚡⚡⚡ |
| Concurrent Writes | ⚡ | ⚡⚡ | ⚡⚡⚡ |

---

## Feature Comparison

### Data Types

| Feature | H2 | MySQL | PostgreSQL |
|---------|----|----|------------|
| Standard SQL Types | ✅ | ✅ | ✅ |
| JSON | ✅ Basic | ✅ Basic | ✅ Advanced |
| Arrays | ❌ | ❌ | ✅ |
| UUID | ✅ | ❌ | ✅ |
| DECIMAL precision | 19,2 | 19,2 | Unlimited |

### Constraints & Integrity

| Feature | H2 | MySQL | PostgreSQL |
|---------|----|----|------------|
| Foreign Keys | ✅ | ✅ | ✅ |
| Check Constraints | ✅ | ✅ (8.0+) | ✅ |
| Partial Indexes | ❌ | ❌ | ✅ |
| Deferred Constraints | ❌ | ❌ | ✅ |

### Advanced Features

| Feature | H2 | MySQL | PostgreSQL |
|---------|----|----|------------|
| Window Functions | ✅ | ✅ (8.0+) | ✅ |
| CTEs (WITH) | ✅ | ✅ (8.0+) | ✅ |
| Recursive CTEs | ❌ | ✅ (8.0+) | ✅ |
| Full-Text Search | ✅ Basic | ✅ Basic | ✅ Advanced |
| Materialized Views | ❌ | ❌ | ✅ |
| Stored Procedures | ✅ | ✅ | ✅ |
| Custom Functions | ✅ | ✅ | ✅ (+ multiple languages) |

---

## Recommendations

### Development
**Use H2** - Fast, easy, no setup required
```bash
mvn spring-boot:run
```

### Testing
**Use H2** - Clean state for each test run
```properties
spring.jpa.hibernate.ddl-auto=create-drop
```

### Staging/Production
**Use PostgreSQL** - Best reliability, features, and performance
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=postgresql
```

### Why PostgreSQL for Banking?

1. **ACID Compliance**: Strongest transaction guarantees
2. **Data Integrity**: Strict type checking and constraints
3. **Concurrency**: Better handling of simultaneous transactions
4. **Audit Features**: Better logging and tracking
5. **Reliability**: Rock-solid stability for financial data
6. **Advanced Features**: JSON for metadata, better analytics
7. **Open Source**: No licensing concerns

---

## Environment-Specific Configuration

### Local Development (H2)
```bash
# No configuration needed
mvn spring-boot:run
```

### Local Testing with PostgreSQL
```bash
# Start PostgreSQL
docker-compose up -d

# Run application
mvn spring-boot:run -Dspring-boot.run.profiles=postgresql
```

### Production (PostgreSQL with environment variables)
```bash
export DB_URL=jdbc:postgresql://prod-server:5432/bankdb
export DB_USERNAME=bank_app
export DB_PASSWORD=secure_password_here
export JPA_DDL_AUTO=validate
export JPA_SHOW_SQL=false

mvn spring-boot:run
```

---

## Connection Pool Configuration

### For Production (all databases)

Add to `application.properties`:

```properties
# HikariCP Configuration (default in Spring Boot)
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000
```

### PostgreSQL-Specific Tuning

```properties
# Connection pool
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=10

# PostgreSQL optimizations
spring.jpa.properties.hibernate.jdbc.batch_size=20
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true
spring.jpa.properties.hibernate.jdbc.batch_versioned_data=true
```

---

## Troubleshooting

### H2 Issues
- **Console not accessible**: Check `spring.h2.console.enabled=true`
- **Data lost**: Using in-memory mode, change to file: `jdbc:h2:file:./data/bankdb`

### MySQL Issues
- **Connection refused**: Check MySQL is running and port 3306
- **Authentication error**: Check password and user privileges
- **Time zone error**: Add `?serverTimezone=UTC` to JDBC URL

### PostgreSQL Issues
- **Connection refused**: Check PostgreSQL is running and port 5432
- **Authentication failed**: Check pg_hba.conf authentication method
- **Database not exist**: Run `CREATE DATABASE bankdb;`
- **Permission denied**: Grant proper privileges to user

---

## Summary

| Use Case | Recommended Database |
|----------|---------------------|
| Quick local development | H2 |
| Learning/Prototyping | H2 |
| Unit tests | H2 |
| Integration tests | H2 or PostgreSQL |
| Staging environment | PostgreSQL |
| Production (small) | PostgreSQL or MySQL |
| Production (enterprise) | PostgreSQL |
| Financial applications | PostgreSQL |
| Need advanced features | PostgreSQL |
| Existing MySQL infrastructure | MySQL |

**Default Setup**: Application comes with H2 configured for immediate use.

**Production Recommendation**: Switch to PostgreSQL using the included Spring profile and Docker Compose setup.
