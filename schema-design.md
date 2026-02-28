# MySQL Schema Design – Smart Clinic Management System

## Overview
The Smart Clinic Management System uses a relational MySQL database to manage
users, appointments, and prescriptions efficiently.

## Tables

### Admin
| Column | Type | Description |
|------|------|------------|
| id | BIGINT (PK) | Unique admin identifier |
| email | VARCHAR | Admin login email |
| password | VARCHAR | Encrypted password |

### Doctor
| Column | Type | Description |
|------|------|------------|
| id | BIGINT (PK) | Unique doctor identifier |
| name | VARCHAR | Doctor name |
| specialization | VARCHAR | Medical specialization |
| email | VARCHAR | Doctor email |

### Patient
| Column | Type | Description |
|------|------|------------|
| id | BIGINT (PK) | Unique patient identifier |
| name | VARCHAR | Patient name |
| email | VARCHAR | Patient email |

### Appointment
| Column | Type | Description |
|------|------|------------|
| id | BIGINT (PK) | Appointment ID |
| appointment_date | DATE | Appointment date |
| appointment_time | VARCHAR | Appointment time slot |
| doctor_id | BIGINT (FK) | References Doctor(id) |
| patient_id | BIGINT (FK) | References Patient(id) |

### Prescription
| Column | Type | Description |
|------|------|------------|
| id | BIGINT (PK) | Prescription ID |
| medicine | VARCHAR | Prescribed medicine |
| instructions | VARCHAR | Dosage instructions |
| appointment_id | BIGINT (FK) | References Appointment(id) |

## Relationships
- One Doctor can have many Appointments
- One Patient can have many Appointments
- One Appointment has one Prescription

## Design Considerations
- Primary and foreign keys enforce data integrity
- Normalized schema avoids redundancy
- Suitable for scalable clinic operations
