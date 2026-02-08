@echo off
REM PostgreSQL Setup Script for Windows
REM This script helps set up and run the Bank Application with PostgreSQL

echo ========================================
echo Bank Application - PostgreSQL Setup
echo ========================================
echo.

:menu
echo Select an option:
echo 1. Start PostgreSQL with Docker
echo 2. Start PostgreSQL with pgAdmin
echo 3. Create database manually
echo 4. Run application with PostgreSQL
echo 5. Run application with H2 (default)
echo 6. Stop PostgreSQL Docker container
echo 7. Exit
echo.
set /p choice="Enter your choice (1-7): "

if "%choice%"=="1" goto start_postgres
if "%choice%"=="2" goto start_pgadmin
if "%choice%"=="3" goto create_db
if "%choice%"=="4" goto run_postgres
if "%choice%"=="5" goto run_h2
if "%choice%"=="6" goto stop_postgres
if "%choice%"=="7" goto end
goto menu

:start_postgres
echo.
echo Starting PostgreSQL container...
docker-compose up -d postgres
echo.
echo PostgreSQL started successfully!
echo Connection details:
echo   Host: localhost
echo   Port: 5432
echo   Database: bankdb
echo   Username: postgres
echo   Password: postgres
echo.
pause
goto menu

:start_pgadmin
echo.
echo Starting PostgreSQL with pgAdmin...
docker-compose --profile with-pgadmin up -d
echo.
echo Services started successfully!
echo PostgreSQL: localhost:5432
echo pgAdmin: http://localhost:5050
echo   Email: admin@bank.com
echo   Password: admin
echo.
pause
goto menu

:create_db
echo.
echo Creating database manually...
echo Run this command in your PostgreSQL client:
echo CREATE DATABASE bankdb;
echo.
pause
goto menu

:run_postgres
echo.
echo Running application with PostgreSQL...
echo Make sure PostgreSQL is running first!
echo.
set SPRING_PROFILES_ACTIVE=postgresql
mvn spring-boot:run -Dspring-boot.run.profiles=postgresql
pause
goto menu

:run_h2
echo.
echo Running application with H2 database...
mvn spring-boot:run
pause
goto menu

:stop_postgres
echo.
echo Stopping PostgreSQL container...
docker-compose down
echo.
echo PostgreSQL stopped successfully!
echo.
pause
goto menu

:end
echo.
echo Exiting...
echo.
exit /b 0
