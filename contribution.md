
## 1) Repo Overview
This repo contains several Spring Boot / Spring Cloud services:
- `services/serviceRegistry` (Eureka Server on `8761`)
- `services/config-server` (Spring Cloud Config Server on `9296`, backed by a Git repo)
- `services/CloudGateway` (API Gateway / routes on `9090`)
- `services/OrderService` (Order APIs; MySQL `order_db`; calls Payment/Product via OpenFeign)
- `services/paymentService` (Payment APIs; MySQL `payment_db`)
- `services/productservice` (Product APIs; MySQL `product_db`)
  Additional infrastructure used:
- RabbitMQ (for Spring Cloud Bus)
- Redis (gateway rate limiter)
> Note: `README.md` describes a broader marketplace platform conceptually, but the actual code in this repo focuses on gateway/registry/config + order/payment/product.
## 2) Prerequisites
You should have:
- Java 17
- Maven
- Docker (optional, for running infrastructure)
- Docker Compose (optional)
- MySQL (required for Order/Payment/Product services)
  Config Server requirements:
- The Config Server is configured to pull config from a Git repository.
- See `services/config-server/src/main/resources/application.yaml` for:
    - `spring.cloud.config.server.git.uri`
    - which Git branch/profile is used
## 3) Recommended Local Development (Maven)
There is no root “single” Maven build file; each service is a standalone Maven project.
Run tests like this (from each service folder):
- `cd services/CloudGateway && mvn test`
- `cd services/serviceRegistry && mvn test`
- `cd services/config-server && mvn test`
- `cd services/OrderService && mvn test`
- `cd services/paymentService && mvn test`
- `cd services/productservice && mvn test`
## 4) Running with Docker Compose
A compose file exists at:
- `docker-compose/docker-compose.yaml`
  It starts (as currently defined):
- `serviceregistry`
- `rabbit`
- `configserver`
- `orderservice`
- `cloudgateway`
- `redis`
  Important:
- `paymentService` and `productservice` containers are NOT included in this compose file as provided.
- MySQL is NOT included in `docker-compose/docker-compose.yaml`.
    - For MySQL you can use the Docker commands from:
        - `docs/dev-logs/commands.md`
## 5) Infrastructure Commands (RabbitMQ / MySQL)
The repo includes helpful container commands in:
- `docs/dev-logs/commands.md`
  Examples provided there:
- RabbitMQ container start
- MySQL container start
- MySQL CLI access via `docker exec ...`
## 6) How Requests Flow (to guide contributions)
When using the gateway, routes are configured in:
- `services/CloudGateway/src/main/resources/application.yaml`
  Routes include:
- `/foodies/order/**` -> `ORDER-SERVICE`
- `/foodies/payment/**` -> `PAYMENT-SERVICE`
- `/foodies/product/**` -> `PRODUCT-SERVICE`
  Resilience/fallback:
- Gateway fallback endpoint is implemented in:
    - `services/CloudGateway/src/main/java/com/DigiClassRoom/CloudGateway/controller/FallBackController.java`
- OrderService calls:
    - Product via OpenFeign (`services/OrderService/.../external/client/ProductService.java`)
    - Payment via OpenFeign with fallback (`services/OrderService/.../external/client/PaymentService.java` and `PaymentFallBack.java`)
      If you change endpoint paths, route predicates, or service names, update gateway routing + any Feign clients accordingly.
## 7) Code Contribution Process
1. Create a new branch from `main`.
2. Keep PR scope small (one goal per PR).
3. Make sure you:
    - run unit tests (`mvn test`) for the affected service(s)
    - update documentation if the change affects setup/behavior
4. Submit a Pull Request with:
    - clear description of what/why
    - how to verify (test steps or run steps)
## 8) Pull Request Checklist
Before opening a PR, confirm:
- The code compiles.
- Relevant tests pass.
- Behavior changes are documented (especially gateway routes, config keys, and service-to-service calls).
- You did not introduce hardcoded environment-specific values that will break Docker/runtime setups.
## 9) Reporting Issues
When reporting a bug, include:
- which service (gateway/config/registry/order/payment/product)
- the request path (example: `/foodies/order/...`)
- relevant logs/stack trace
- environment details (local vs Docker, MySQL/RabbitMQ/Redis status)