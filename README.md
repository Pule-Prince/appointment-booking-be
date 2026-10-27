# Capitec Appointment Booking System - Backend

## Overview

Spring Boot REST API for the Capitec Appointment Booking System. Enables customers to book appointments with consultants across multiple branches with real-time availability management, automated confirmations, and appointment cancellations.

---

## Quick Start

### Clone Repository
```bash
git clone https://github.com/Pule-Prince/appointment-booking-be.git
cd into the directory where you cloned the project
```

### Environment Requirements

**Required:**
- Java 17+
- PostgreSQL 16
- Maven 3.6+
- Docker

---

## Run with Docker

### 1. Using Docker Compose 

```bash
# Build and start all services (backend + database)
docker-compose -f docker-compose.yml up -d

# Stop all containers configured in docker-compose
docker-compose -f docker-compose.yml stop

# Remove all containers configured
docker-compose -f docker-compose.yml down

```
### 2. Using IntelliJ
```bash
- Ensure the booking-postgres and booking-mailhog containers are running and booking-app is stopped

- On Intellij run the BookingApplication configuration startup
```

## Simulated Email Notification

### Access MailHog to view emails

http://localhost:8025/

## API Documentation (Swagger)

### Access Swagger UI

http://localhost:8080/swagger-ui.html

### Key Endpoints

**Appointments:**
- `POST /api/appointments` - Create appointment
- `GET /api/appointments?date=2026-10-27` - Get appointments by date
- `DELETE /api/appointments/{referenceNumber}` - Cancel appointment

**Branches:**
- `GET /api/branches` - List all active branches
- `GET /api/branches/{branchId}/slots?date=2026-10-27` - Get available slots

**Service Catalog:**
- `GET /api/services/categories` - List service categories
- `GET /api/services/categories/{categoryId}/products` - Get products
- `GET /api/services/products/{productId}/service-types` - Get service types
- `GET /api/services/service-types/{serviceTypeId}/documents` - Get required documents

### Example Request

```bash
# Create Appointment
curl -X POST http://localhost:8080/api/appointments \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "acceptTerms": true,
    "serviceTypeId": 1,
    "serviceReason": "New Policy",
    "branchId": 4,
    "appointmentDate": "2026-10-27",
    "timeSlotId": 1
  }'
```

---

## Technology Stack

- **Framework:** Spring Boot 4.1.1
- **Language:** Java 17
- **Database:** PostgreSQL 16
- **ORM:** Hibernate JPA
- **Mailhog:** Simulate email notification
- **Testing:** JUnit 5 + Mockito
- **API Docs:** Swagger/OpenAPI 3.0
- **Build:** Maven

---

---

## Frontend

Separate Angular 19 repository. See Frontend README.md for setup.

## Diagrams
<img width="1187" height="1500" alt="Capitec Appointment Booking - Sequence Diagram" src="https://github.com/user-attachments/assets/272b8a0c-7a92-42ea-9639-568f50bd7084" />

<img width="1432" height="1362" alt="ERD drawio" src="https://github.com/user-attachments/assets/26c04d9e-93f1-4fd7-a808-a73d13cd0a1b" />

# Database

You can use DBeaver to connect to the PostgreSQL and run queries against the tables

DB url: jdbc:postgresql://localhost:5432/booking
DB username: postgres
DB password: postgres
