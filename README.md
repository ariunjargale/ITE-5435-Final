# Hospital Management Microservices

This project is a Spring Boot microservices application for a Hospital Management System.
It has separate services for patients, doctors, and appointments, plus a Thymeleaf web UI.

## Technologies Used

- Java 17
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Spring Cloud Eureka
- Thymeleaf
- RestTemplate
- Jackson
- MySQL

## Services

| Service | Port | Description |
| --- | ---: | --- |
| `eureka-server` | `8761` | Service registry |
| `web-ui` | `8080` | Thymeleaf user interface |
| `patient-service` | `8081` | Patient CRUD API |
| `doctor-service` | `8082` | Doctor CRUD API |
| `appointment-service` | `8083` | Appointment CRUD API |

## CRUD Functions

Each main service supports basic CRUD operations:

- Create new records
- View all records
- View one record by ID
- Update existing records
- Delete records
- Search records by text fields

### Patient Service

Manages patient information such as name, mobile, email, address, username, and password.

REST base path:

```text
http://localhost:8081/api/patients
```

### Doctor Service

Manages doctor information such as name, mobile, email, address, username, and password.

REST base path:

```text
http://localhost:8082/api/doctors
```

### Appointment Service

Manages appointment information such as appointment number, type, date, description, and doctor ID.

REST base path:

```text
http://localhost:8083/api/appointments
```

## Web UI

The web UI runs on:

```text
http://localhost:8080
```

Main pages:

- `/patients`
- `/doctors`
- `/appointments`

The UI uses Thymeleaf for pages and `RestTemplate` to call the backend services.

## How The Project Works

1. Eureka Server runs as the service registry.
2. Patient, Doctor, Appointment, and Web UI services run on separate ports.
3. The user works through the web UI.
4. The web UI sends requests to the REST services using `RestTemplate`.
5. The backend services save and read data using Spring Data JPA and MySQL.
