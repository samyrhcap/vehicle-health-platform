# Vehicle Health Platform

## Overview

Vehicle Health Platform is a microservices-based system designed to collect telemetry data from connected vehicles, evaluate vehicle health, detect anomalies, and provide actionable diagnostic information.

The platform consists of two independent Spring Boot microservices working together to process vehicle telemetry and generate vehicle health insights.

---

## Business Context

Connected vehicles continuously generate telemetry data that can be used to monitor their health and detect potential issues before they become critical.

This platform enables:

- Collection of vehicle telemetry data
- Automated vehicle health analysis
- Early anomaly detection
- Health score calculation
- Alert generation
- Integration with external recall providers
- Exposure of vehicle diagnostics through REST APIs

---

# Solution Architecture

```text
                          Connected Vehicle
                                   │
                                   │ Telemetry Data
                                   ▼

┌─────────────────────────────────────────────┐
│              Telemetry Service              │
├─────────────────────────────────────────────┤
│                                             │
│ Responsibilities                            │
│ • Receive telemetry data                    │
│ • Validate incoming requests                │
│ • Persist telemetry information             │
│ • Trigger vehicle analysis                  │
│                                             │
└───────────────────┬─────────────────────────┘
                    │
                    │ OpenFeign
                    │ POST /internal/analyze
                    ▼

┌─────────────────────────────────────────────┐
│          Vehicle Analysis Service           │
├─────────────────────────────────────────────┤
│                                             │
│ Responsibilities                            │
│ • Process telemetry data                    │
│ • Calculate health scores                   │
│ • Determine vehicle health status           │
│ • Generate alerts                           │
│ • Persist analysis reports                  │
│ • Expose analysis APIs                      │
│                                             │
└───────────────────┬─────────────────────────┘
                    │
                    │ OpenFeign
                    ▼

          ┌───────────────────────┐
          │ Recall Provider API   │
          │      (WireMock)       │
          └───────────────────────┘

                    │
                    ▼

          ┌───────────────────────┐
          │      PostgreSQL       │
          └───────────────────────┘
```

---

# Microservices

## 1. Telemetry Service

### Responsibilities

- Receive telemetry data from connected vehicles
- Validate incoming payloads
- Persist telemetry information
- Trigger vehicle analysis requests
- Handle communication errors with downstream services

### Main Endpoint

```http
POST /api/v1/telemetry
```

### Example Request

```json
{
  "vin": "VF1RFA12345678901",
  "mileage": 150000,
  "batteryLevel": 15,
  "fuelLevel": 5,
  "tirePressure": 1.7,
  "engineTemperature": 110,
  "errorCodes": [
    "P001",
    "P002"
  ]
}
```

### Swagger

```text
http://localhost:8081/swagger-ui/index.html
```

### Service Documentation

See:

```text
telemetry-service/README.md
```

---

## 2. Vehicle Analysis Service

### Responsibilities

- Process telemetry information
- Retrieve recall information
- Calculate health scores
- Determine vehicle health status
- Generate alerts
- Persist health reports
- Expose vehicle diagnostics

### Internal Endpoint

```http
POST /internal/analyze
```

### Public Endpoints

```http
GET /api/v1/vehicles/{vin}/health-report

GET /api/v1/vehicles/{vin}/alerts
```

### Swagger

```text
http://localhost:8082/swagger-ui/index.html
```

### Service Documentation

See:

```text
vehicle-analysis-service/README.md
```

---

# Functional Workflow

```text
1. Vehicle sends telemetry data

2. Telemetry Service
   ├─ Validates request
   ├─ Persists telemetry
   └─ Triggers analysis

3. Vehicle Analysis Service
   ├─ Retrieves recall information
   ├─ Calculates health score
   ├─ Determines vehicle status
   ├─ Generates alerts
   └─ Persists results

4. Client retrieves:
   ├─ Health Report
   └─ Alerts
```

---

# Health Score Model

Initial score:

```text
100
```

Penalties applied:

| Condition | Penalty |
|------------|----------|
| Mileage > 100,000 km | -10 |
| Mileage > 200,000 km | -20 |
| Battery level < 20% | -20 |
| Fuel level < 10% | -5 |
| Tire pressure < 1.8 bar | -10 |
| Engine temperature > 100°C | -15 |
| Each diagnostic error code | -5 |
| Active recall campaign | -10 |

Final score:

```text
max(score, 0)
```

---

# Vehicle Health Status

| Score | Status |
|---------|----------|
| 80 - 100 | EXCELLENT |
| 60 - 79 | GOOD |
| 40 - 59 | WARNING |
| < 40 | CRITICAL |

---

# Alert Types

The platform can generate the following alerts:

```text
LOW_BATTERY

LOW_FUEL

LOW_TIRE_PRESSURE

ENGINE_OVERHEATING

ERROR_CODES_DETECTED

VEHICLE_RECALL
```

---

# Technology Stack

## Backend

- Java 21
- Spring Boot 3
- Spring Data JPA
- Spring Validation
- Spring Cloud OpenFeign

## Database

- PostgreSQL

## API Documentation

- OpenAPI 3
- Swagger UI

## Testing

- JUnit 5
- Mockito
- MockMvc
- Spring Boot Test
- Integration Testing

## External Provider Simulation

- WireMock

## Monitoring

- Spring Boot Actuator

---

# Repository Structure

```text
vehicle-health-platform
│
├── docker-compose.yml
│
├── postgres
│   ├── init.sql
│   └── init-databases.sh
│
├── wiremock
│   └── mappings
│       └── recall-response.json
│
├── telemetry-service
│   ├── Dockerfile
│   ├── .dockerignore
│   ├── pom.xml
│   ├── src
│   └── README.md
│
├── vehicle-analysis-service
│   ├── Dockerfile
│   ├── .dockerignore
│   ├── pom.xml
│   ├── src
│   └── README.md
│
├── .gitignore
│
└── README.md
```
# Running the Platform with Docker

## Prerequisites

The platform is fully containerized and requires only:

- Docker Desktop (Windows/macOS)
- Docker Engine + Docker Compose (Linux)

No installation of Java, Maven, PostgreSQL, or WireMock is required.

---

## Start the Platform

Clone the repository:

```bash
git clone https://github.com/samyrhcap/vehicle-health-platform.git

cd vehicle-health-platform
```

Build and start the platform:

```bash
docker compose up --build -d
```

Docker automatically:

- Builds both Spring Boot applications
- Starts PostgreSQL
- Creates application databases
- Starts WireMock
- Starts Vehicle Analysis Service
- Starts Telemetry Service

---

## Containers

The platform starts the following containers:

| Container | Purpose |
|------------|----------|
| postgres | PostgreSQL Database Server |
| db-init | Database bootstrap container |
| recall-provider | WireMock Recall Provider |
| vehicle-analysis-service | Vehicle Analysis API |
| telemetry-service | Telemetry API |

The `db-init` container executes once and exits successfully after ensuring all required databases exist.

This behavior is expected.

---

## Database Initialization

The platform automatically creates and validates the required PostgreSQL databases.

Databases:

```text
vehicle_telemetry_db
vehicle_analysis_db
```

Initialization workflow:

```text
PostgreSQL
      ↓
db-init
      ↓
Create databases if missing
      ↓
Vehicle Analysis Service
      ↓
Telemetry Service
```

No manual SQL execution is required.

Existing PostgreSQL data is preserved across restarts.

---

## Verify Running Containers

```bash
docker ps
```

Expected running containers:

```text
postgres
recall-provider
vehicle-analysis-service
telemetry-service
```

View all containers:

```bash
docker ps -a
```

Expected completed container:

```text
db-init
```

---

## Access the Platform

### Telemetry Service

Swagger UI:

```text
http://localhost:8081/swagger-ui/index.html
```

Health Endpoint:

```http
GET http://localhost:8081/actuator/health
```

---

### Vehicle Analysis Service

Swagger UI:

```text
http://localhost:8082/swagger-ui/index.html
```

Health Endpoint:

```http
GET http://localhost:8082/actuator/health
```

---

### WireMock Recall Provider

```text
http://localhost:9999
```

# API Documentation

## Telemetry Service

Swagger UI:

```text
http://localhost:8081/swagger-ui/index.html
```

OpenAPI Documentation:

```text
http://localhost:8081/v3/api-docs
```

---

## Vehicle Analysis Service

Swagger UI:

```text
http://localhost:8082/swagger-ui/index.html
```

OpenAPI Documentation:

```text
http://localhost:8082/v3/api-docs
```

---

# Monitoring

## Telemetry Service

```http
GET /actuator/health

GET /actuator/info
```

---

## Vehicle Analysis Service

```http
GET /actuator/health

GET /actuator/info
```

---

# Build & Deployment

## Local Development

Build Telemetry Service:

```bash
cd telemetry-service

mvn clean package
```

Build Vehicle Analysis Service:

```bash
cd ../vehicle-analysis-service

mvn clean package
```

---

## Docker Deployment

Build and start all services:

```bash
docker compose up --build -d
```

---

## Stop Platform

```bash
docker compose down
```

---

## Reset Database State

```bash
docker compose down -v
```

> Use this command only when you intentionally want to reset PostgreSQL data.

---

## View Logs

```bash
docker compose logs -f
```

Specific services:

```bash
docker logs postgres

docker logs db-init

docker logs vehicle-analysis-service

docker logs telemetry-service
```

# Testing

The platform includes:

### Unit Tests

- Controller layer
- Service layer
- Health score calculation
- Alert generation
- Exception handling

### Integration Tests

- Telemetry persistence
- Analysis persistence
- Service communication
- API contract validation

### External Provider Simulation

- WireMock recall provider

---

# Author

**Sami Rhalim**

Associate Software Engineer

Vehicle Health Platform Project
