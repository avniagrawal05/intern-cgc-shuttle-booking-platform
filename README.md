# Hospital Management System (HMS)

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.12-brightgreen)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange)](https://openjdk.org/projects/jdk/21/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

A production-scale Hospital Management System built with Spring Boot, featuring JWT authentication, Redis caching, comprehensive monitoring, and Docker containerization.

## Table of Contents

- [Architecture](#architecture)
- [Quick Start](#quick-start)
- [Project Structure](#project-structure)
- [Key Features](#key-features)
- [Configuration](#configuration)
- [API Documentation](#api-documentation)
- [Deployment](#deployment)
- [Monitoring](#monitoring)
- [Testing](#testing)
- [Contributing](#contributing)

## Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        Client Layer                          │
│  (Web Browser / Mobile App / Third-party Integrations)      │
└─────────────────────────┬───────────────────────────────────┘
                          │ HTTP/HTTPS
┌─────────────────────────▼───────────────────────────────────┐
│                      API Gateway                             │
│  • JWT Authentication     • Rate Limiting                   │
│  • Request Validation     • CORS Configuration              │
└─────────────────────────┬───────────────────────────────────┘
                          │
┌─────────────────────────▼───────────────────────────────────┐
│                   Application Layer                          │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐        │
│  │  Controllers │ │   Services   │ │ Repositories │        │
│  └──────────────┘ └──────────────┘ └──────────────┘        │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐        │
│  │     DTOs     │ │   Mappers    │ │   Entities   │        │
│  └──────────────┘ └──────────────┘ └──────────────┘        │
└─────────────────────────┬───────────────────────────────────┘
                          │
        ┌─────────────────┼─────────────────┐
        ▼                 ▼                 ▼
┌──────────────┐  ┌──────────────┐  ┌──────────────┐
│    MySQL     │  │    Redis     │  │  Prometheus  │
│  (Primary    │  │   (Cache &   │  │   (Metrics   │
│   Database)  │  │   Session)   │  │  Collection) │
└──────────────┘  └──────────────┘  └──────┬───────┘
                                           │
                                    ┌──────▼───────┐
                                    │    Grafana   │
                                    │(Visualization)│
                                    └──────────────┘
```

## Quick Start

### Prerequisites

- [JDK 21](https://openjdk.org/projects/jdk/21/) or higher
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [Docker 20.10+](https://docs.docker.com/get-docker/)
- [Docker Compose 2.0+](https://docs.docker.com/compose/install/)

### Run with Docker Compose

```bash
# Clone the repository
git clone <repository-url>
cd com.hms/com.hms

# Start all services
docker-compose up -d

# Wait for services to be healthy (30-60 seconds)
docker-compose ps

# Access the application
curl http://localhost:8080/hms/actuator/health
```

### Run Locally (Development)

```bash
# Start MySQL and Redis
docker-compose up -d mysql redis

# Run the application
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### Access Points

| Service | URL |
|---------|-----|
| Application API | http://localhost:8080/hms |
| Swagger UI | http://localhost:8080/hms/swagger-ui.html |
| Actuator Health | http://localhost:8080/hms/actuator/health |
| Prometheus | http://localhost:9090 |
| Grafana | http://localhost:3000 (admin/admin) |

## Project Structure

```
com.hms/
├── src/
│   ├── main/
│   │   ├── java/com/hms/
│   │   │   ├── [api/v1/controller/](#controllers)          # REST Controllers
│   │   │   ├── [config/](#configuration)                   # App Configuration
│   │   │   │   ├── [interceptor/](#interceptors)           # Logging Interceptor
│   │   │   ├── [constants/](#constants)                    # App Constants
│   │   │   ├── [dto/](#dtos)                               # Data Transfer Objects
│   │   │   ├── [entity/](#entities)                        # JPA Entities
│   │   │   ├── [exception/](#exceptions)                   # Custom Exceptions
│   │   │   ├── [mapper/](#mappers)                         # Entity-DTO Mappers
│   │   │   ├── [repository/](#repositories)                # Data Access Layer
│   │   │   ├── [security/](#security)                      # Security Components
│   │   │   │   ├── [config/](#security-config)             # Security Configuration
│   │   │   │   ├── [jwt/](#jwt)                            # JWT Implementation
│   │   │   │   └── [service/](#security-services)          # Security Services
│   │   │   ├── [service/](#services)                       # Business Logic
│   │   │   │   └── [impl/](#service-impl)                  # Service Implementations
│   │   │   ├── [util/](#utilities)                         # Utility Classes
│   │   │   ├── [validation/](#validation)                  # Validation Groups
│   │   │   └── [Application.java](#main-class)             # Entry Point
│   │   └── resources/
│   │       ├── [application.yml](#app-config)              # Main Configuration
│   │       └── [application-test.yml](#test-config)         # Test Configuration
│   └── test/
│       └── java/com/hms/
│           ├── [unit/](#unit-tests)                        # Unit Tests
│           └── [integration/](#integration-tests)           # Integration Tests
├── [pom.xml](#maven-config)                                 # Maven Configuration
├── [Dockerfile](#dockerfile)                                # Docker Image Build
├── [docker-compose.yml](#docker-compose)                    # Container Orchestration
├── [DEPLOYMENT.md](#deployment-guide)                       # Deployment Guide
├── [database-schema.sql](#database-schema)                  # Database Schema
└── [monitoring/](#monitoring)                               # Monitoring Configs
    ├── [prometheus.yml](#prometheus-config)                 # Prometheus Config
    └── [grafana/](#grafana)                                 # Grafana Dashboards
```

## Key Features

### 🔐 Security
- **JWT Authentication** - Stateless token-based authentication
- **Role-Based Access Control** - ADMIN, DOCTOR, RECEPTIONIST, PATIENT roles
- **Password Encryption** - BCrypt hashing
- **Audit Logging** - Track created/updated by and timestamps

### ⚡ Performance
- **Redis Caching** - Cache patients, doctors, appointments, billings
- **Connection Pooling** - HikariCP for database connections
- **Async Processing** - Thread pool for background tasks
- **Batch Operations** - JPA batch inserts/updates

### 📊 Monitoring
- **Spring Boot Actuator** - Health checks and metrics
- **Prometheus** - Metrics collection
- **Grafana** - Visualization and alerting
- **Request Logging** - Automatic request/response logging with timing

### 🧪 Testing
- **Unit Tests** - JUnit 5 with Mockito
- **Integration Tests** - Spring Boot Test with H2 database
- **Security Tests** - Spring Security Test

## Configuration

### Main Configuration
- **[application.yml](src/main/resources/application.yml)** - Primary configuration file with database, Redis, and actuator settings

### Security Configuration
- **[SecurityConfig.java](src/main/java/com/hms/security/config/SecurityConfig.java)** - Spring Security configuration with JWT
- **[JwtTokenProvider.java](src/main/java/com/hms/security/jwt/JwtTokenProvider.java)** - JWT token generation and validation
- **[CustomUserDetailsService.java](src/main/java/com/hms/security/service/CustomUserDetailsService.java)** - User loading for authentication

### Cache Configuration
- **[RedisConfig.java](src/main/java/com/hms/config/RedisConfig.java)** - Redis cache configuration with TTL settings

### Async Configuration
- **[AsyncConfig.java](src/main/java/com/hms/config/AsyncConfig.java)** - Thread pool configuration for async operations

### Audit Configuration
- **[AuditConfig.java](src/main/java/com/hms/config/AuditConfig.java)** - JPA auditing configuration

## API Documentation

### Authentication Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/api/v1/auth/login` | User login | Public |
| POST | `/api/v1/auth/register` | User registration | Public |
| POST | `/api/v1/auth/refresh` | Refresh token | Authenticated |

### Patient Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/v1/patients` | List all patients (paginated) | ADMIN, RECEPTIONIST |
| GET | `/api/v1/patients/{id}` | Get patient by ID | All authenticated |
| POST | `/api/v1/patients` | Create new patient | ADMIN, RECEPTIONIST |
| PUT | `/api/v1/patients/{id}` | Update patient | ADMIN, RECEPTIONIST |
| DELETE | `/api/v1/patients/{id}` | Delete patient | ADMIN |
| GET | `/api/v1/patients/search` | Search patients by name | All authenticated |

### Doctor Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/v1/doctors` | List all doctors | All authenticated |
| GET | `/api/v1/doctors/{id}` | Get doctor by ID | All authenticated |
| POST | `/api/v1/doctors` | Create new doctor | ADMIN |
| PUT | `/api/v1/doctors/{id}` | Update doctor | ADMIN |
| DELETE | `/api/v1/doctors/{id}` | Delete doctor | ADMIN |

### Appointment Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/v1/appointments` | List all appointments | All authenticated |
| GET | `/api/v1/appointments/{id}` | Get appointment by ID | All authenticated |
| POST | `/api/v1/appointments` | Create new appointment | ADMIN, RECEPTIONIST, PATIENT |
| PUT | `/api/v1/appointments/{id}` | Update appointment | ADMIN, RECEPTIONIST, DOCTOR |
| DELETE | `/api/v1/appointments/{id}` | Delete appointment | ADMIN, RECEPTIONIST |

### Billing Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/v1/billings` | List all billings | ADMIN, RECEPTIONIST, PATIENT |
| GET | `/api/v1/billings/{id}` | Get billing by ID | ADMIN, RECEPTIONIST, PATIENT |
| POST | `/api/v1/billings` | Create new billing | ADMIN, RECEPTIONIST |
| PUT | `/api/v1/billings/{id}` | Update billing | ADMIN, RECEPTIONIST |
| DELETE | `/api/v1/billings/{id}` | Delete billing | ADMIN |

## Deployment

### Docker Deployment

```bash
# Build and start all services
docker-compose up -d --build

# View logs
docker-compose logs -f app

# Scale application instances
docker-compose up -d --scale app=3
```

See [DEPLOYMENT.md](DEPLOYMENT.md) for detailed deployment instructions including:
- Production configuration
- Environment variables
- Database migrations
- Backup and recovery
- CI/CD pipeline setup

## Monitoring

### Health Checks
- **Liveness Probe**: `/hms/actuator/health/liveness`
- **Readiness Probe**: `/hms/actuator/health/readiness`

### Metrics
- JVM metrics (memory, GC, threads)
- HTTP request metrics (count, duration)
- Cache metrics (hits, misses)
- Database connection pool metrics

### Alerts
Configure alerts in Grafana for:
- High response time (> 2s)
- Error rate > 5%
- Memory usage > 80%
- Database connection pool exhaustion

## Testing

### Run Unit Tests
```bash
./mvnw test -Dtest="*Test"
```

### Run Integration Tests
```bash
./mvnw test -Dtest="*IntegrationTest" -Dspring.profiles.active=test
```

### Run All Tests
```bash
./mvnw clean verify
```

### Test Coverage
- **Unit Tests**: Service layer with mocked repositories
- **Integration Tests**: Controller layer with H2 database
- **Security Tests**: Authentication and authorization flows

## Technologies

| Category | Technology |
|----------|------------|
| **Framework** | Spring Boot 3.3.12 |
| **Language** | Java 21 |
| **Database** | MySQL 8.0 |
| **Cache** | Redis 7 |
| **Security** | Spring Security, JWT |
| **Mapping** | MapStruct |
| **Documentation** | OpenAPI 3.0, Swagger UI |
| **Monitoring** | Spring Boot Actuator, Prometheus, Grafana |
| **Container** | Docker, Docker Compose |
| **Testing** | JUnit 5, Mockito, Spring Boot Test |

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Code Style
- Follow existing code patterns
- Use meaningful variable names
- Add JavaDoc for public methods
- Write unit tests for new features

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support

For issues and feature requests, please create an issue in the project repository.

---

**Made with ❤️ for healthcare professionals**
