# 🏥 Hospital CMS – Backend

Spring Boot REST API for the **Smart Clinic Management System** built for SmartCare Solutions.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.2 |
| Auth | Spring Security + JWT (jjwt 0.12) |
| Relational DB | MySQL 8 (via Spring Data JPA) |
| Document DB | MongoDB 7 (Prescriptions) |
| Build | Maven 3.9 |
| Container | Docker + Docker Compose |
| CI/CD | GitHub Actions |

---

## Quick Start

### Option A – Docker Compose (recommended)

```bash
git clone https://github.com/your-org/hospital-cms-backend.git
cd hospital-cms-backend
docker compose up --build
```

The app starts on **http://localhost:8080**.  
Sample data (admin, 6 doctors, 3 patients, appointments) is auto-seeded.

### Option B – Local (requires MySQL + MongoDB running)

```bash
# Edit src/main/resources/application.properties with your DB credentials
./mvnw spring-boot:run
```

---

## Default Credentials (seeded)

| Role | Email | Password |
|---|---|---|
| Admin | admin@hospital.com | admin123 |
| Doctor | dr.adams@example.com | password |
| Patient | john.smith@example.com | password |

---

## API Reference

All endpoints are prefixed with `/api`.  
Protected endpoints require `Authorization: Bearer <token>` header.

### Auth

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | `/api/auth/register` | Public | Register new user |
| POST | `/api/auth/login` | Public | Login, receive JWT |

**Register body example:**
```json
{
  "fullName": "John Smith",
  "email": "john@example.com",
  "password": "secret123",
  "role": "PATIENT",
  "dateOfBirth": "1990-05-15",
  "phone": "+1234567890"
}
```

**Login body example:**
```json
{ "email": "admin@hospital.com", "password": "admin123" }
```

**Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGc...",
    "type": "Bearer",
    "id": 1,
    "fullName": "Admin User",
    "email": "admin@hospital.com",
    "role": "ADMIN"
  }
}
```

---

### Doctors

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| GET | `/api/doctors` | Public | List all active doctors |
| GET | `/api/doctors?search=adams` | Public | Search by name |
| GET | `/api/doctors?specialty=Cardiologist` | Public | Filter by specialty |
| GET | `/api/doctors/{id}` | Public | Get doctor by ID |
| PATCH | `/api/doctors/{id}` | ADMIN/DOCTOR | Update doctor profile |
| DELETE | `/api/doctors/{id}` | ADMIN | Deactivate doctor |

---

### Patients

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| GET | `/api/patients` | ADMIN/DOCTOR | List all patients |
| GET | `/api/patients/{id}` | Any auth | Get patient by ID |
| PATCH | `/api/patients/{id}` | ADMIN/PATIENT | Update patient profile |
| DELETE | `/api/patients/{id}` | ADMIN | Delete patient |

---

### Appointments

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | `/api/appointments` | ADMIN/PATIENT | Book appointment |
| GET | `/api/appointments` | ADMIN | List all appointments |
| GET | `/api/appointments/{id}` | Any auth | Get appointment |
| GET | `/api/appointments/patient/{id}` | ADMIN/PATIENT | Patient's appointments |
| GET | `/api/appointments/doctor/{id}` | ADMIN/DOCTOR | Doctor's appointments |
| PATCH | `/api/appointments/{id}` | Any auth | Update appointment |
| PATCH | `/api/appointments/{id}/cancel` | Any auth | Cancel appointment |
| DELETE | `/api/appointments/{id}` | ADMIN/DOCTOR | Delete appointment |

**Book appointment body:**
```json
{
  "patientId": 1,
  "doctorId": 1,
  "appointmentDate": "2025-06-01",
  "timeSlot": "09:00-10:00",
  "notes": "Follow-up visit"
}
```

---

### Prescriptions (MongoDB)

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | `/api/prescriptions` | ADMIN/DOCTOR | Create prescription |
| GET | `/api/prescriptions/appointment/{id}` | Any auth | Get by appointment |
| GET | `/api/prescriptions/patient/{id}` | Any auth | Patient's prescriptions |
| GET | `/api/prescriptions/doctor/{id}` | ADMIN/DOCTOR | Doctor's prescriptions |
| PUT | `/api/prescriptions/{id}` | ADMIN/DOCTOR | Update prescription |
| DELETE | `/api/prescriptions/{id}` | ADMIN | Delete prescription |

**Create prescription body:**
```json
{
  "appointmentId": 1,
  "medicineNames": "Vitamin C tablets",
  "dosageInstructions": "Twice a day",
  "additionalNotes": "Take with food",
  "medicines": [
    {
      "name": "Vitamin C",
      "dosage": "500mg",
      "frequency": "Twice a day",
      "durationDays": 14
    }
  ]
}
```

---

### Admin Dashboard

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| GET | `/api/admin/dashboard` | ADMIN | Aggregate stats |

---

## Running Tests

```bash
./mvnw test -Dspring.profiles.active=test
```

Tests use an H2 in-memory database (no MySQL or MongoDB required).

---

## Project Structure

```
src/
├── main/java/com/smartcare/cms/
│   ├── config/          # SecurityConfig, DataSeeder
│   ├── controller/      # REST controllers (Auth, Doctor, Patient, Appointment, Prescription, Admin)
│   ├── dto/             # Request/Response DTOs
│   ├── exception/       # Custom exceptions + GlobalExceptionHandler
│   ├── model/           # JPA entities (User, Doctor, Patient, Appointment) + Mongo (Prescription)
│   ├── repository/      # Spring Data JPA + Mongo repositories
│   ├── security/        # JwtTokenProvider, JwtAuthenticationFilter, UserDetailsServiceImpl
│   └── service/         # Business logic (AuthService, DoctorService, etc.)
└── test/                # Integration tests (H2 profile)
```

---

## Environment Variables

| Variable | Default | Description |
|---|---|---|
| `SPRING_DATASOURCE_URL` | (see properties) | MySQL JDBC URL |
| `SPRING_DATASOURCE_USERNAME` | root | MySQL username |
| `SPRING_DATASOURCE_PASSWORD` | password | MySQL password |
| `SPRING_DATA_MONGODB_URI` | mongodb://localhost:27017/... | MongoDB connection |
| `APP_JWT_SECRET` | (base64 key) | JWT signing secret |
| `APP_JWT_EXPIRATION_MS` | 86400000 (24h) | JWT TTL in ms |
| `APP_CORS_ALLOWED_ORIGINS` | localhost:3000,5500 | Allowed CORS origins |

---

## Connecting the Frontend

Update the HTML frontend to call the API. Example:

```javascript
// Login
const res = await fetch('http://localhost:8080/api/auth/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ email, password })
});
const { data } = await res.json();
localStorage.setItem('token', data.token);

// Fetch doctors (public)
const doctors = await fetch('http://localhost:8080/api/doctors').then(r => r.json());

// Book appointment (authenticated)
await fetch('http://localhost:8080/api/appointments', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${localStorage.getItem('token')}`
  },
  body: JSON.stringify({ patientId, doctorId, appointmentDate, timeSlot })
});
```
