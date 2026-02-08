#!/bin/bash
# PostgreSQL Setup Script for Linux/Mac
# This script helps set up and run the Bank Application with PostgreSQL

echo "========================================"
echo "Bank Application - PostgreSQL Setup"
echo "========================================"
echo ""

show_menu() {
    echo "Select an option:"
    echo "1. Start PostgreSQL with Docker"
    echo "2. Start PostgreSQL with pgAdmin"
    echo "3. Create database manually"
    echo "4. Run application with PostgreSQL"
    echo "5. Run application with H2 (default)"
    echo "6. Stop PostgreSQL Docker container"
    echo "7. Exit"
    echo ""
}

start_postgres() {
    echo ""
    echo "Starting PostgreSQL container..."
    docker-compose up -d postgres
    echo ""
    echo "PostgreSQL started successfully!"
    echo "Connection details:"
    echo "  Host: localhost"
    echo "  Port: 5432"
    echo "  Database: bankdb"
    echo "  Username: postgres"
    echo "  Password: postgres"
    echo ""
    read -p "Press Enter to continue..."
}

start_pgadmin() {
    echo ""
    echo "Starting PostgreSQL with pgAdmin..."
    docker-compose --profile with-pgadmin up -d
    echo ""
    echo "Services started successfully!"
    echo "PostgreSQL: localhost:5432"
    echo "pgAdmin: http://localhost:5050"
    echo "  Email: admin@bank.com"
    echo "  Password: admin"
    echo ""
    read -p "Press Enter to continue..."
}

create_db() {
    echo ""
    echo "Creating database manually..."
    echo "Run this command in your PostgreSQL client:"
    echo "CREATE DATABASE bankdb;"
    echo ""
    read -p "Press Enter to continue..."
}

run_postgres() {
    echo ""
    echo "Running application with PostgreSQL..."
    echo "Make sure PostgreSQL is running first!"
    echo ""
    export SPRING_PROFILES_ACTIVE=postgresql
    mvn spring-boot:run -Dspring-boot.run.profiles=postgresql
    read -p "Press Enter to continue..."
}

run_h2() {
    echo ""
    echo "Running application with H2 database..."
    mvn spring-boot:run
    read -p "Press Enter to continue..."
}

stop_postgres() {
    echo ""
    echo "Stopping PostgreSQL container..."
    docker-compose down
    echo ""
    echo "PostgreSQL stopped successfully!"
    echo ""
    read -p "Press Enter to continue..."
}

while true; do
    clear
    echo "========================================"
    echo "Bank Application - PostgreSQL Setup"
    echo "========================================"
    echo ""
    show_menu
    read -p "Enter your choice (1-7): " choice
    
    case $choice in
        1) start_postgres ;;
        2) start_pgadmin ;;
        3) create_db ;;
        4) run_postgres ;;
        5) run_h2 ;;
        6) stop_postgres ;;
        7) echo "Exiting..."; exit 0 ;;
        *) echo "Invalid option. Please try again."; sleep 2 ;;
    esac
done
