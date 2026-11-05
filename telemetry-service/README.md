# Telemetry Service

## Introduction

Telemetry Service is the entry point of the Vehicle Health Platform.

The service receives telemetry data from connected vehicles, validates incoming requests, persists telemetry information in the database, and triggers vehicle health analysis by communicating with the Vehicle Analysis Service.

---

## Main Responsibilities

- Receive telemetry data from connected vehicles
- Validate incoming telemetry payloads
- Persist telemetry information
- Trigger vehicle health analysis
- Handle communication errors with downstream services
- Expose telemetry ingestion APIs
- Provide operational monitoring endpoints

---

## Architecture

```text
Connected Vehicle
        │
        ▼
+--------------------+
| Telemetry Service  |
+--------------------+
        │
        │ OpenFeign
        ▼
+---------------------------+
| Vehicle Analysis Service  |
+---------------------------+
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
- JUnit 5
- Mockito
- MockMvc

---

## Functional Flow

1. A vehicle sends telemetry information.
2. The request is validated.
3. Telemetry data is persisted.
4. A request is sent to the Vehicle Analysis Service.
5. The API responds with **202 Accepted**.

---

## API Documentation

### Submit Telemetry

Stores telemetry information and triggers vehicle analysis.

#### Endpoint

```http
POST /api/v1/telemetry
```

#### Request Body

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

#### Success Response

```http
202 Accepted
```

```json
{
  "vin": "VF1RFA12345678901",
  "status": "ACCEPTED",
  "receivedAt": "2026-11-05T10:30:00"
}
```

---

## Request Validation Rules

| Field | Validation Rule |
|---------|---------|
| vin | Mandatory |
| vin | Exactly 17 characters |
| mileage | Greater than or equal to 0 |
| batteryLevel | Between 0 and 100 |
| fuelLevel | Between 0 and 100 |
| tirePressure | Greater than 0 |
| engineTemperature | Mandatory |

---

## Error Handling

### Validation Failure

```http
400 Bad Request
```

Example:

```json
{
  "code": "TEL_004",
  "message": "Validation failed",
  "status": 400
}
```

---

### Vehicle Analysis Service Unavailable

```http
502 Bad Gateway
```

Example:

```json
{
  "code": "TEL_003",
  "message": "Vehicle analysis service unavailable",
  "status": 502
}
```

---

### Unexpected Error

```http
500 Internal Server Error
```

Example:

```json
{
  "code": "TEL_999",
  "message": "Unexpected error",
  "status": 500
}
```

---

## Database

### Table: telemetry

| Field | Description |
|---------|-------------|
| id | Technical identifier |
| vin | Vehicle identification number |
| mileage | Vehicle mileage |
| battery_level | Battery percentage |
| fuel_level | Fuel percentage |
| tire_pressure | Tire pressure |
| engine_temperature | Engine temperature |
| received_at | Telemetry reception timestamp |

---

## Integration with Vehicle Analysis Service

Telemetry Service communicates with Vehicle Analysis Service using OpenFeign.

### Internal Endpoint Called

```http
POST /internal/analyze
```

### Request Sent

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

---

## Configuration

### Application Properties

```yaml
server:
  port: 8081
```

### Environment Variables

| Variable | Description |
|-----------|-------------|
| DB_URL | PostgreSQL connection URL |
| DB_USERNAME | Database username |
| DB_PASSWORD | Database password |
| ANALYSIS_SERVICE_URL | Vehicle Analysis Service base URL |

Example:

```bash
DB_URL=jdbc:postgresql://localhost:5432/vehicle_telemetry_db
DB_USERNAME=postgres
DB_PASSWORD=postgres
ANALYSIS_SERVICE_URL=http://localhost:8082
```

---

## Monitoring

### Health Check

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

### Application Information

```http
GET /actuator/info
```

---

## API Documentation

### Swagger UI

```text
http://localhost:8081/swagger-ui/index.html
```

### OpenAPI Specification

```text
http://localhost:8081/v3/api-docs
```

---

## Testing

### Unit Tests

Covered components:

- TelemetryService
- TelemetryController
- Feign Client Integration
- Validation Rules
- Exception Handling

### Integration Tests

Covered scenarios:

- Telemetry persistence
- API contract validation
- Database integration
- Analysis service invocation

---

## Build and Run

### Build

```bash
mvn clean package
```

### Run

```bash
mvn spring-boot:run
```

---

## Project Structure

```text
src
├── main
│   ├── java
│   │   └── com.renault.telemetry
│   │       ├── client
│   │       │   └── VehicleAnalysisClient
│   │       ├── config
│   │       │   └── OpenApiConfig
│   │       ├── dao
│   │       │   ├── entity
│   │       │   │   └── Telemetry
│   │       │   └── repository
│   │       │       └── TelemetryRepository
│   │       ├── dto
│   │       │   ├── request
│   │       │   │   ├── AnalysisRequest
│   │       │   │   └── TelemetryRequest
│   │       │   └── response
│   │       │       ├── ErrorResponse
│   │       │       └── TelemetryResponse
│   │       ├── enums
│   │       │   └── ApiErrorCode
│   │       ├── exception
│   │       │   ├── BusinessException
│   │       │   ├── ExternalAnalysisServiceException
│   │       │   ├── GlobalExceptionHandler
│   │       │   └── TelemetryException
│   │       ├── mapper
│   │       │   ├── AnalysisRequestMapper
│   │       │   └── TelemetryMapper
│   │       ├── service
│   │       │   ├── TelemetryService
│   │       │   └── TelemetryServiceImpl
│   │       ├── web
│   │       │   └── TelemetryController
│   │       └── TelemetryServiceApplication
│   │
│   └── resources
│       ├── application.yml
│       ├── static
│       └── templates
│
└── test
    ├── java
    │   └── com.renault.telemetry
    │       ├── TelemetryControllerTest
    │       ├── TelemetryIntegrationTest
    │       ├── TelemetryServiceApplicationTests
    │       └── TelemetryServiceTest
    │
    └── resources
        └── application-test.yml
```

---

---

---

## Author

**Sami Rhalim**

Associate Software Engineer
