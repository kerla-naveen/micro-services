# Micro Services Platform
A Spring Boot + Spring Cloud based microservices project for a food-ordering style domain.  
The current repository includes:
- API Gateway
- Service Registry (Eureka)
- Config Server (Spring Cloud Config)
- Order Service
- Payment Service
- Product Service
  It also uses supporting infrastructure such as RabbitMQ, Redis, and MySQL.
---
## Table of Contents
- [Architecture Overview](#architecture-overview)
- [Services](#services)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Configuration](#configuration)
- [Run with Docker Compose](#run-with-docker-compose)
- [Run Services Locally (Maven)](#run-services-locally-maven)
- [API Routing](#api-routing)
- [Service Communication](#service-communication)
- [Observability and Resilience](#observability-and-resilience)
- [Build Docker Images (Jib)](#build-docker-images-jib)
- [Testing](#testing)
- [Known Gaps / Notes](#known-gaps--notes)
- [Contributing](#contributing)
---
## Architecture Overview
This project follows a typical Spring Cloud microservices architecture:
1. **Service Registry** (`serviceRegistry`) starts first and exposes Eureka.
2. **Config Server** (`config-server`) fetches service configuration from a Git repository.
3. **Business services** register with Eureka and fetch config from Config Server.
4. **Cloud Gateway** receives external requests and routes them to target services through service discovery.
5. **Order Service** communicates with **Payment Service** and **Product Service** using OpenFeign.
6. **RabbitMQ** supports Spring Cloud Bus based config refresh.
7. **Redis** is used in gateway rate limiting.
8. **MySQL** stores service data (separate DB per service).
---
## Services
### 1) Service Registry
- Module: `services/serviceRegistry`
- Purpose: Eureka server for service discovery
- Default Port: `8761`
### 2) Config Server
- Module: `services/config-server`
- Purpose: Centralized externalized configuration
- Default Port: `9296`
- Config source: Git repo (`spring-app-config`)
### 3) Cloud Gateway
- Module: `services/CloudGateway`
- Purpose: API routing, filters, resilience, rate limiting
- Default Port: `9090`
### 4) Order Service
- Module: `services/OrderService`
- Purpose: Order management and orchestration
- Default Port: `8082`
- DB: `order_db` (MySQL)
- Calls: Product + Payment via OpenFeign
### 5) Payment Service
- Module: `services/paymentService`
- Purpose: Payment processing
- Default Port: `8081`
- DB: `payment_db` (MySQL)
### 6) Product Service
- Module: `services/productservice`
- Purpose: Product catalog and inventory updates
- Default Port: `8080`
- DB: `product_db` (MySQL)
---
## Tech Stack
- **Java 17**
- **Spring Boot**
- **Spring Cloud**
    - Gateway
    - Eureka
    - Config Server
    - OpenFeign
    - Resilience4j
    - Spring Cloud Bus (AMQP)
- **MySQL** (JPA/Hibernate)
- **RabbitMQ**
- **Redis**
- **Maven**
- **Docker / Docker Compose**
- **Jib** for container image builds
- **Swagger/OpenAPI** via springdoc
---
## Project Structure
```text
micro-services/
├── services/
│   ├── CloudGateway/
│   ├── config-server/
│   ├── serviceRegistry/
│   ├── OrderService/
│   ├── paymentService/
│   └── productservice/
├── docker-compose/
│   ├── docker-compose.yaml
│   └── common-config.yaml
├── docs/
│   ├── dev-logs/
│   ├── infrastructure/
│   └── services/
├── project-document.md
└── README.md
```

# Prerequisites

Install the following:

- Java 17
- Maven
- Docker + Docker Compose
- MySQL (if not using a containerized setup)
- RabbitMQ (if not using a containerized setup)
- Redis (for gateway rate limiter)

---

# Configuration

## Config Server

Config Server is enabled and points to a Git-backed configuration repository.

Key settings (in `services/config-server/src/main/resources/application.yaml`):

- `spring.cloud.config.server.git.uri`
- `spring.profiles.active=git`
- RabbitMQ host / port / username / password
- Eureka registration

---

## Business Services

Services import configuration from Config Server using:

```
spring.config.import
```

Most services define local defaults for:

- datasource URL / username / password
- RabbitMQ configuration
- service name
- actuator exposure

---

# Run with Docker Compose

Compose file path:

```
docker-compose/docker-compose.yaml
```

From repository root:

```bash
docker compose -f docker-compose/docker-compose.yaml up -d
```

To stop:

```bash
docker compose -f docker-compose/docker-compose.yaml down
```

---

# Services Started via Docker Compose

- serviceregistry
- rabbit
- configserver
- orderservice
- cloudgateway
- redis

---

# Run Services Locally (Maven)

Each service is a standalone Maven project.

Run in separate terminals:

```bash
cd services/serviceRegistry && mvn spring-boot:run
cd services/config-server && mvn spring-boot:run
cd services/productservice && mvn spring-boot:run
cd services/paymentService && mvn spring-boot:run
cd services/OrderService && mvn spring-boot:run
cd services/CloudGateway && mvn spring-boot:run
```

---

# Suggested Startup Order

1. serviceRegistry
2. rabbitmq + redis + mysql
3. config-server
4. productservice + paymentService + OrderService
5. CloudGateway

---

# API Routing

Gateway routes requests to services using Eureka load-balanced URIs.

Examples:

```
/foodies/order/**    -> ORDER-SERVICE
/foodies/payment/**  -> PAYMENT-SERVICE
/foodies/product/**  -> PRODUCT-SERVICE
```

There is also route configuration in the gateway `application.yaml`:

```
/order/**
/payment/**
/product/**
```

---

# Service Communication

Order Service uses OpenFeign clients to call:

- Product Service
- Payment Service

Payment client has a fallback:

```
PaymentFallBack
```

Gateway fallback endpoint:

```
/orderServiceFallBack
```

---

# Observability and Resilience

- Actuator endpoints enabled in services
- Resilience4j circuit breakers configured in gateway and service clients
- Distributed tracing enabled (Brave / Zipkin reporter)
- Redis-based rate limiting configured in the gateway

---

# Build Docker Images (Jib)

Each service includes `jib-maven-plugin` configuration.

Build a local Docker image:

```bash
cd services/<service-name>
mvn compile jib:dockerBuild
```

Build and push an image:

```bash
cd services/<service-name>
mvn compile jib:build
```

---

# Testing

Run tests per service:

```bash
cd services/<service-name>
mvn test
```

---

# Known Gaps / Notes

- Docker Compose may not include all business services for full end-to-end startup.
- MySQL database initialization and schema setup should be verified for first-time setup.
- Config Server depends on the external Git repository availability.
- Environment secrets should not be hardcoded; use environment variables or secure configuration management.

---

# Contributing

Please read `CONTRIBUTING.md` (or `contribution.md`) for:

- branching workflow
- coding standards
- PR checklist
- local validation steps

If contribution guidelines are missing, create one and include:

- setup steps
- service startup order
- testing expectations
- PR format
- review checklist

---

# Quick Useful Endpoints

Eureka Dashboard
```
http://localhost:8761
```

Config Server
```
http://localhost:9296
```

Gateway Swagger UI
```
http://localhost:9090/swagger-ui.html
```

RabbitMQ Management
```
http://localhost:15672
```

Login:
```
guest / guest
```