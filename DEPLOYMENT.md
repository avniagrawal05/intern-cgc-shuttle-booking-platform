# Hospital Management System - Production Deployment Guide

## Overview

This guide covers the production deployment of the Hospital Management System (HMS) with Docker, monitoring, and scaling configurations.

## Architecture

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│     Nginx       │────▶│  HMS Application │────▶│  MySQL Database │
│  (Load Balancer)│     │   (Spring Boot)  │     │   (Primary)     │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                               │
                               ▼
                        ┌─────────────────┐
                        │  Redis Cache    │
                        └─────────────────┘
                               │
                               ▼
                        ┌─────────────────┐
                        │  Prometheus/    │
                        │    Grafana      │
                        └─────────────────┘
```

## Quick Start

### Prerequisites

- Docker 20.10+
- Docker Compose 2.0+
- 4GB+ RAM available

### Local Development

```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f app

# Stop all services
docker-compose down

# Stop and remove volumes (clean slate)
docker-compose down -v
```

### Access Points

| Service | URL | Credentials |
|---------|-----|-------------|
| Application | http://localhost:8080/hms | - |
| Swagger UI | http://localhost:8080/hms/swagger-ui.html | - |
| Actuator Health | http://localhost:8080/hms/actuator/health | - |
| Prometheus | http://localhost:9090 | - |
| Grafana | http://localhost:3000 | admin/admin |

## Production Configuration

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `SPRING_DATASOURCE_URL` | MySQL connection URL | jdbc:mysql://mysql:3306/hms_db |
| `SPRING_DATASOURCE_USERNAME` | Database username | hms_user |
| `SPRING_DATASOURCE_PASSWORD` | Database password | hms_password |
| `SPRING_DATA_REDIS_HOST` | Redis host | redis |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | Hibernate DDL mode | update |
| `LOGGING_LEVEL_COM_HMS` | Application log level | INFO |

### Security Considerations

1. **Change default passwords** in docker-compose.yml
2. **Use secrets management** for production (Docker Swarm/Kubernetes secrets)
3. **Enable SSL/TLS** for all external connections
4. **Configure firewall rules** to restrict access

### Database Migrations

For production, use Flyway or Liquibase instead of `ddl-auto: update`:

```yaml
# application-prod.yml
spring:
  jpa:
    hibernate:
      ddl-auto: validate
  flyway:
    enabled: true
    locations: classpath:db/migration
```

## Monitoring

### Health Checks

- **Liveness**: `/hms/actuator/health/liveness`
- **Readiness**: `/hms/actuator/health/readiness`

### Key Metrics

| Metric | Description |
|--------|-------------|
| `jvm_memory_used_bytes` | JVM memory usage |
| `http_server_requests_seconds` | HTTP request duration |
| `cache_hits_total` | Redis cache hits |
| `cache_misses_total` | Redis cache misses |

### Alerts (Grafana)

- High response time (> 2s)
- Error rate > 5%
- Memory usage > 80%
- Database connection pool exhaustion

## Scaling

### Horizontal Scaling

```yaml
# docker-compose.scale.yml
services:
  app:
    deploy:
      replicas: 3
    environment:
      - SPRING_PROFILES_ACTIVE=prod
```

### Load Balancer (Nginx)

```nginx
upstream hms_backend {
    least_conn;
    server app1:8080;
    server app2:8080;
    server app3:8080;
}

server {
    listen 80;
    location / {
        proxy_pass http://hms_backend;
    }
}
```

## Backup & Recovery

### Database Backup

```bash
# Automated backup script
#!/bin/bash
BACKUP_DIR="/backups/mysql"
DATE=$(date +%Y%m%d_%H%M%S)
docker exec hms-mysql mysqldump -u root -proot hms_db > $BACKUP_DIR/hms_backup_$DATE.sql
```

### Redis Persistence

Redis is configured with AOF persistence for data durability.

## Troubleshooting

### Common Issues

1. **Database connection refused**
   - Check MySQL container health: `docker-compose ps`
   - Verify environment variables

2. **Redis connection errors**
   - Ensure Redis container is running
   - Check network connectivity

3. **Out of memory**
   - Increase Docker memory limit
   - Adjust JVM heap size: `-Xmx512m -Xms256m`

### Logs

```bash
# Application logs
docker-compose logs -f app

# Database logs
docker-compose logs -f mysql

# All services
docker-compose logs -f
```

## CI/CD Pipeline

### GitHub Actions Example

```yaml
name: Build and Deploy

on:
  push:
    branches: [main]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 21
        uses: actions/setup-java@v3
        with:
          java-version: '21'
          distribution: 'temurin'
      - name: Build with Maven
        run: mvn clean package -DskipTests
      - name: Build Docker image
        run: docker build -t hms:${{ github.sha }} .
      - name: Push to registry
        run: |
          echo ${{ secrets.DOCKER_PASSWORD }} | docker login -u ${{ secrets.DOCKER_USERNAME }} --password-stdin
          docker push hms:${{ github.sha }}
```

## Support

For issues and feature requests, please create an issue in the project repository.
