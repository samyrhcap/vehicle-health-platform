# Vehicle Analysis Service

## Introduction

Vehicle Analysis Service is responsible for processing vehicle telemetry data, evaluating vehicle health, generating alerts, and exposing analysis results through REST APIs.

The service receives telemetry information from the Telemetry Service, enriches analysis data with recall information from an external provider, calculates a vehicle health score, generates alerts when anomalies are detected, and persists analysis results.

---

## Main Responsibilities

- Process vehicle telemetry data
- Calculate a vehicle health score
- Determine vehicle health status
- Generate alerts based on detected anomalies
- Retrieve recall information from an external provider
- Persist health reports
- Persist generated alerts
- Expose health reports through REST APIs
- Expose vehicle alerts through REST APIs

---

# Architecture

```text
Telemetry Service
        │
        │ POST /internal/analyze
        ▼
┌─────────────────────────────────────────┐
│        Vehicle Analysis Service         │
├─────────────────────────────────────────┤
│                                         │
│  Controllers Layer                      │
│  ├─ InternalAnalysisController          │
│  └─ VehicleAnalysisController           │
│                                         │
│  Service Layer                          │
│  ├─ AnalysisService                     │
│  ├─ HealthScoreService                  │
│  └─ AlertService                        │
│                                         │
│  Helper Layer                           │
│  ├─ AnalysisHelper                      │
│  ├─ HealthScoreHelper                   │
│  └─ AlertRuleHelper                     │
│                                         │
│  Mapping Layer                          │
│  └─ AnalysisMapper                      │
│                                         │
│  Persistence Layer                      │
│  ├─ HealthReportRepository              │
│  └─ AlertRepository                     │
│                                         │
└──────────────┬──────────────────────────┘
               │
               ▼
        PostgreSQL Database

               │
               │ OpenFeign
               ▼

┌─────────────────────────────┐
│    Recall Provider API      │
│       (WireMock)            │
└─────────────────────────────┘
```

---

## Technology Stack

- Java 21
- Spring Boot 3
- Spring Data JPA
- Spring Validation
- Spring Cloud OpenFeign
- PostgreSQL
- Spring Actuator
- OpenAPI / Swagger
- WireMock
- JUnit 5
- Mockito
- MockMvc

---

## Functional Flow

1. Telemetry Service sends telemetry information.
2. Vehicle Analysis Service receives the analysis request.
3. Recall information is retrieved from the external provider.
4. Health score penalties are applied.
5. Vehicle health status is determined.
6. Alerts are generated if anomalies are detected.
7. Health report and alerts are persisted.
8. Health information becomes available through public APIs.

---

# APIs

## Internal API

### Analyze Vehicle

Used internally by Telemetry Service.

#### Endpoint

```http
POST /internal/analyze
```

#### Request

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

#### Response

```http
202 Accepted
```

---

## Public APIs

### Get Health Report

Returns the latest available vehicle health report.

#### Endpoint

```http
GET /api/v1/vehicles/{vin}/health-report
```

#### Success Response

```http
200 OK
```

```json
{
  "vin": "VF1RFA12345678901",
  "healthScore": 20,
  "status": "CRITICAL",
  "analyzedAt": "2026-11-05T10:30:00"
}
```

---

### Get Vehicle Alerts

Returns alerts generated for a vehicle.

#### Endpoint

```http
GET /api/v1/vehicles/{vin}/alerts
```

#### Success Response

```http
200 OK
```

```json
[
  {
    "type": "LOW_BATTERY",
    "message": "LOW_BATTERY"
  },
  {
    "type": "VEHICLE_RECALL",
    "message": "VEHICLE_RECALL"
  }
]
```

---

# Health Score Calculation

Initial score:

```text
100
```

Penalties applied during scoring:

| Condition | Penalty |
|------------|----------|
| Mileage > 100,000 km | -10 |
| Mileage > 200,000 km | -20 |
| Battery level < 20% | -20 |
| Fuel level < 10% | -5 |
| Tire pressure < 1.8 bar | -10 |
| Engine temperature > 100°C | -15 |
| Each error code | -5 |
| Active recall campaign | -10 |

Final score:

```text
max(score, 0)
```

---

# Vehicle Health Status

| Score Range | Status |
|-------------|---------|
| 80 - 100 | EXCELLENT |
| 60 - 79 | GOOD |
| 40 - 59 | WARNING |
| Less than 40 | CRITICAL |

---

# Alert Generation Rules

Alerts are generated when anomalies are detected.

Supported alert types:

| Alert Type |
|------------|
| LOW_BATTERY |
| LOW_FUEL |
| LOW_TIRE_PRESSURE |
| ENGINE_OVERHEATING |
| ERROR_CODES_DETECTED |
| VEHICLE_RECALL |

---

# External Provider Integration

The service communicates with an external recall provider through OpenFeign.

The provider returns recall information associated with a VIN.

### Example Response

```json
{
  "activeRecall": true
}
```

During development and tests the provider is simulated using WireMock.

---

# Error Handling

## Health Report Not Found

```http
404 Not Found
```

```json
{
  "code": "ANL_001",
  "message": "Health report not found"
}
```

---

## Validation Error

```http
400 Bad Request
```

```json
{
  "code": "ANL_003",
  "message": "Validation failed"
}
```

---

## External Provider Unavailable

```http
502 Bad Gateway
```

```json
{
  "code": "ANL_002",
  "message": "Recall provider unavailable"
}
```

---

## Unexpected Error

```http
500 Internal Server Error
```

```json
{
  "code": "ANL_999",
  "message": "Unexpected error"
}
```

---

# Database

## Table: health_report

Stores vehicle analysis results.

| Column | Description |
|----------|-------------|
| id | Technical identifier |
| vin | Vehicle identification number |
| health_score | Calculated health score |
| status | Vehicle health status |
| analyzed_at | Analysis timestamp |

---

## Table: alert

Stores alerts generated during analysis.

| Column | Description |
|----------|-------------|
| id | Technical identifier |
| vin | Vehicle identification number |
| type | Alert type |
| message | Alert description |
| created_at | Alert creation timestamp |

---

# Configuration

### Application Properties

```yaml
server:
  port: 8082
```

### Environment Variables

| Variable | Description |
|-----------|-------------|
| DB_URL | PostgreSQL connection URL |
| DB_USERNAME | Database username |
| DB_PASSWORD | Database password |
| RECALL_PROVIDER_URL | External recall provider URL |

Example:

```bash
DB_URL=jdbc:postgresql://localhost:5432/vehicle_analysis_db
DB_USERNAME=postgres
DB_PASSWORD=postgres

RECALL_PROVIDER_URL=http://localhost:9999
```

---

# Monitoring

### Health Endpoint

```http
GET /actuator/health
```

Example:

```json
{
  "status": "UP"
}
```

---

### Information Endpoint

```http
GET /actuator/info
```

---

# API Documentation

### Swagger UI

```text
http://localhost:8082/swagger-ui/index.html
```

### OpenAPI Specification

```text
http://localhost:8082/v3/api-docs
```

---

# Testing

## Unit Tests

Covered components:

- AnalysisService
- HealthScoreService
- AlertService
- Controllers
- Helper classes
- Exception handling

---

## Integration Tests

Covered scenarios:

- Vehicle analysis workflow
- Health report persistence
- Alert persistence
- Database integration
- API contract validation

---

## WireMock Tests

Covered scenarios:

- Recall provider simulation
- External dependency validation

---

# Build and Run

### Build

```bash
mvn clean package
```

### Run

```bash
mvn spring-boot:run
```

---

# Project Structure

```text
src
├── main
│   ├── java
│   │   └── com.renault.vehicle.analysis
│   │       ├── client
│   │       │   └── RecallProviderClient
│   │       ├── config
│   │       │   └── OpenApiConfig
│   │       ├── dao
│   │       │   ├── entity
│   │       │   │   ├── Alert
│   │       │   │   └── HealthReport
│   │       │   └── repository
│   │       │       ├── AlertRepository
│   │       │       └── HealthReportRepository
│   │       ├── dto
│   │       │   ├── request
│   │       │   │   └── AnalysisRequest
│   │       │   └── response
│   │       │       ├── AlertResponse
│   │       │       ├── ErrorResponse
│   │       │       ├── HealthReportResponse
│   │       │       └── RecallResponse
│   │       ├── enums
│   │       │   ├── AlertType
│   │       │   ├── ErrorCode
│   │       │   └── VehicleHealthStatus
│   │       ├── exception
│   │       │   ├── BusinessException
│   │       │   ├── ExternalProviderException
│   │       │   ├── GlobalExceptionHandler
│   │       │   └── HealthReportNotFoundException
│   │       ├── mapper
│   │       │   └── AnalysisMapper
│   │       ├── service
│   │       │   ├── helpers
│   │       │   │   ├── AlertRuleHelper
│   │       │   │   ├── AnalysisHelper
│   │       │   │   └── HealthScoreHelper
│   │       │   ├── AlertService
│   │       │   ├── AlertServiceImpl
│   │       │   ├── AnalysisService
│   │       │   ├── AnalysisServiceImpl
│   │       │   ├── HealthScoreService
│   │       │   └── HealthScoreServiceImpl
│   │       ├── web
│   │       │   ├── InternalAnalysisController
│   │       │   └── VehicleAnalysisController
│   │       └── VehicleAnalysisServiceApplication
│   │
│   └── resources
│       ├── application.yml
│       ├── static
│       └── templates
│
└── test
    ├── java
    │   └── com.renault.vehicle.analysis
    │       ├── AlertServiceTest
    │       ├── AnalysisServiceIntegrationTest
    │       ├── AnalysisServiceTest
    │       ├── HealthScoreServiceTest
    │       ├── InternalAnalysisControllerTest
    │       ├── VehicleAnalysisControllerTest
    │       └── VehicleAnalysisServiceApplicationTests
    │
    └── resources
        ├── application-test.yml
        └── mappings
            └── recall-response.json
```

---

## Author

**Sami Rhalim**

Associate Software Engineer
