# PostgreSQL Quick Reference Card

## 🚀 Quick Start (30 seconds)

```bash
# 1. Start PostgreSQL
docker-compose up -d

# 2. Run application
mvn spring-boot:run -Dspring-boot.run.profiles=postgresql

# 3. Access Swagger
# Open: http://localhost:8080/swagger-ui.html
```

---

## 📋 Three Ways to Use PostgreSQL

### Method 1: Spring Profile ⭐ RECOMMENDED
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=postgresql
```

### Method 2: Environment Variables
```bash
# Windows PowerShell
$env:DB_URL="jdbc:postgresql://localhost:5432/bankdb"
$env:DB_DRIVER="org.postgresql.Driver"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="postgres"
$env:JPA_PLATFORM="org.hibernate.dialect.PostgreSQLDialect"
mvn spring-boot:run

# Linux/Mac
export DB_URL=jdbc:postgresql://localhost:5432/bankdb
export DB_DRIVER=org.postgresql.Driver
export DB_USERNAME=postgres
export DB_PASSWORD=postgres
export JPA_PLATFORM=org.hibernate.dialect.PostgreSQLDialect
mvn spring-boot:run
```

### Method 3: Edit application.properties
Change defaults in `src/main/resources/application.properties`

---

## 🐳 Docker Commands

```bash
# Start PostgreSQL only
docker-compose up -d postgres

# Start PostgreSQL + pgAdmin
docker-compose --profile with-pgadmin up -d

# Stop all services
docker-compose down

# Stop and remove data (reset)
docker-compose down -v

# View logs
docker logs postgres-bank

# Access PostgreSQL shell
docker exec -it postgres-bank psql -U postgres -d bankdb

# List tables
docker exec -it postgres-bank psql -U postgres -d bankdb -c "\dt"
```

---

## 🎯 Connection Details

### PostgreSQL (Docker)
```
Host:     localhost
Port:     5432
Database: bankdb
Username: postgres
Password: postgres
URL:      jdbc:postgresql://localhost:5432/bankdb
```

### pgAdmin (Optional)
```
URL:      http://localhost:5050
Email:    admin@bank.com
Password: admin
```

### H2 (Default - No Setup)
```
URL:      http://localhost:8080/h2-console
JDBC:     jdbc:h2:mem:bankdb
Username: sa
Password: password
```

---

## 🛠️ Setup Scripts

### Windows
```bash
setup-postgres.bat
# Interactive menu with options
```

### Linux/Mac
```bash
chmod +x setup-postgres.sh
./setup-postgres.sh
# Interactive menu with options
```

---

## ✅ Verify Setup

### Check PostgreSQL is Running
```bash
docker ps
# Should show: postgres-bank
```

### Test Database Connection
```bash
docker exec -it postgres-bank psql -U postgres -c "\l"
# Should list: bankdb
```

### Check Application Logs
Look for these messages when starting:
```
✅ HikariPool - Starting...
✅ HikariPool - Start completed.
✅ Started BankApplication in X.XXX seconds
```

---

## 🔧 Common Issues & Fixes

### Port 5432 Already in Use
```bash
# Stop existing PostgreSQL
docker stop $(docker ps -q --filter ancestor=postgres)
# Or change port in docker-compose.yml: "5433:5432"
```

### Database Does Not Exist
```bash
docker exec -it postgres-bank psql -U postgres -c "CREATE DATABASE bankdb;"
```

### Connection Refused
```bash
# Restart PostgreSQL
docker-compose restart postgres
```

### Permission Denied
```bash
# Linux/Mac - make script executable
chmod +x setup-postgres.sh
```

---

## 📊 Database Comparison

| Feature | H2 | PostgreSQL |
|---------|----|----|
| Setup | ⚡ None | ⚡⚡ Docker |
| Production | ❌ No | ✅ Yes |
| Performance | ⭐⭐ Dev | ⭐⭐⭐⭐ Prod |
| Data Loss | ⚠️ On restart | ✅ Persistent |
| Best For | Development | Production |

---

## 🎓 Useful PostgreSQL Commands

### In psql Shell
```sql
-- List databases
\l

-- Connect to database
\c bankdb

-- List tables
\dt

-- Describe table
\d customers

-- View table data
SELECT * FROM customers;

-- Exit
\q
```

### From Command Line
```bash
# Run SQL file
docker exec -i postgres-bank psql -U postgres -d bankdb < schema-postgresql.sql

# Backup database
docker exec postgres-bank pg_dump -U postgres bankdb > backup.sql

# Restore database
docker exec -i postgres-bank psql -U postgres -d bankdb < backup.sql
```

---

## 🔐 Production Checklist

- [ ] Change default passwords
- [ ] Use environment variables for secrets
- [ ] Set `spring.jpa.hibernate.ddl-auto=validate`
- [ ] Set `spring.jpa.show-sql=false`
- [ ] Configure connection pool size
- [ ] Enable SSL for database connection
- [ ] Set up regular backups
- [ ] Configure monitoring
- [ ] Use persistent volumes

---

## 📚 Documentation Files

| File | Purpose |
|------|---------|
| `README.md` | Main project documentation |
| `POSTGRESQL_SETUP.md` | Detailed PostgreSQL setup guide |
| `DATABASE_COMPARISON.md` | Compare H2, MySQL, PostgreSQL |
| `POSTGRESQL_COMPATIBILITY_SUMMARY.md` | Change summary |
| `schema-postgresql.sql` | Manual schema creation |
| `docker-compose.yml` | Docker configuration |

---

## 💡 Tips

1. **Development**: Use H2 (default) - fastest startup
2. **Testing**: Use PostgreSQL with Docker - realistic environment
3. **Production**: Use PostgreSQL - reliable and feature-rich
4. **Data Exploration**: Use pgAdmin (http://localhost:5050)
5. **Quick Reset**: `docker-compose down -v && docker-compose up -d`

---

## 🆘 Need Help?

1. Check application logs for errors
2. Review `POSTGRESQL_SETUP.md` for detailed guide
3. See `DATABASE_COMPARISON.md` for feature comparison
4. Check Docker logs: `docker logs postgres-bank`
5. Verify connection: `docker exec -it postgres-bank psql -U postgres`

---

## 🎯 Common Use Cases

### Switch from H2 to PostgreSQL
```bash
# No code changes needed!
docker-compose up -d
mvn spring-boot:run -Dspring-boot.run.profiles=postgresql
```

### Reset Database
```bash
docker-compose down -v
docker-compose up -d
mvn spring-boot:run -Dspring-boot.run.profiles=postgresql
```

### View Database in GUI
```bash
docker-compose --profile with-pgadmin up -d
# Open: http://localhost:5050
# Add server: postgres-bank, localhost, 5432
```

### Production Deployment
```bash
# Set environment variables
export DB_URL=jdbc:postgresql://prod-server:5432/bankdb
export DB_USERNAME=prod_user
export DB_PASSWORD=secure_password
export JPA_DDL_AUTO=validate
export JPA_SHOW_SQL=false

# Run application
mvn spring-boot:run
```

---

## ⚡ Performance Tips

1. **Use Connection Pooling** (enabled by default with HikariCP)
2. **Create Indexes** (see `schema-postgresql.sql`)
3. **Set appropriate pool size** (10-20 for most cases)
4. **Use prepared statements** (automatic with JPA)
5. **Enable query caching** if needed

---

## 🎉 Success Indicators

When everything works correctly, you'll see:

✅ Docker: `postgres-bank` container running  
✅ Application: "Started BankApplication" message  
✅ Database: Tables created automatically  
✅ API: Swagger UI accessible  
✅ Endpoints: All APIs responding  

---

**Quick Start Reminder:**
```bash
docker-compose up -d
mvn spring-boot:run -Dspring-boot.run.profiles=postgresql
```

That's it! Your Bank Application is now running with PostgreSQL. 🚀
