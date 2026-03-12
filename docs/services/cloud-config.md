# Cloud Config Server

The **Cloud Config Server** is a centralized configuration management service used in a microservices architecture.  
Instead of storing configuration files inside every microservice, all configuration is stored in one central repository and served dynamically to services.

The configuration server runs as an independent service and provides configuration values to client services during startup or when configuration refresh is triggered.

---

## Why do we need a Cloud Config Server?

In a typical microservices architecture, each service contains its own configuration files such as:

As the number of services grows, managing these configurations becomes difficult.

### Problems

1. **Service restart required for configuration changes**

   When configuration values change (for example database URLs or feature flags), the service must be restarted for the changes to take effect.

2. **Multiple instance management**

   If a service runs multiple instances, every instance must be restarted to apply configuration updates.

3. **No centralized configuration**

   Configuration values are scattered across different services, making management difficult.

4. **Difficult environment management**

   Managing configurations for multiple environments such as:

    - development
    - testing
    - staging
    - production

   becomes complex.

5. **Configuration duplication**

   Many services share similar configuration values, which leads to repeated configuration across services.

---

## Solution: Cloud Config Server

The **Cloud Config Server** solves these problems by centralizing configuration management.

All configuration files are stored in a central Git repository, and services fetch their configuration from the server during startup.

This enables:

- centralized configuration management
- easier updates across services
- environment-based configurations
- consistent configuration across multiple service instances

---

## Architecture Diagram
              Git Repository
           (Configuration Files)
                    |
                    |
                    v
            ---------------------
            |   Config Server   |
            | (Spring Cloud)    |
            ---------------------
                    |
    -----------------------------------------
    |                  |                    |
    v                  v                    v


---

## How It Works

1. Configuration files are stored in a **Git repository**.
2. The **Config Server** connects to this repository.
3. When a microservice starts, it requests its configuration from the Config Server.
4. The Config Server retrieves the configuration from Git.
5. The configuration is returned to the requesting service.

---

## Benefits

Using a Cloud Config Server provides several advantages:

- centralized configuration management
- consistent configuration across services
- easier configuration updates
- environment-specific configuration
- reduced configuration duplication

---

## Decision

Introduce a **Cloud Config Server** in the microservices architecture to provide centralized configuration management.

This improves maintainability, reduces configuration duplication across services, and simplifies configuration updates without modifying each service individually.


# Setting Up a Spring Cloud Config Server

## Problem
How to set up a **Spring Cloud Config Server** to centralize configuration for multiple microservices.

---

## Step 1: Create a Maven Project

Create a **Spring Boot Maven project** using Spring Initializr or an IDE like IntelliJ IDEA.

Add the required dependency for the Config Server.

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-config-server</artifactId>
</dependency>
```
## Step 2: Enable the Config Server

In the main Spring Boot application class, enable the Config Server using the @EnableConfigServer annotation.

## Step 3: Configure `application.yaml`

Add the following configuration to the `application.yaml` file.

```yaml
spring:
  application:
    name: config-server

  profiles:
    active: git

  cloud:
    config:
      server:
        git:
          uri: https://github.com/kerla-naveen/spring-app-config
          clone-on-start: true
          timeout: 5
          default-label: main
          
   ```
### Configuration Explanation

| Property | Description |
|---------|-------------|
| `spring.application.name` | Name of the config server |
| `spring.profiles.active` | Activates Git-based configuration |
| `spring.cloud.config.server.git.uri` | Git repository storing configuration files |
| `clone-on-start` | Clones the repository when the server starts |
| `timeout` | Timeout for Git operations |
| `default-label` | Default Git branch used to fetch configurations |

# Configuration Refresh in Spring Cloud Config

## Problem 001-I: How do services get updated configuration?

When configuration changes in the config repository, client services must refresh their configuration.

Each client service exposes an actuator endpoint that can be used to refresh the configuration.

### Refresh Endpoint

When this endpoint is invoked:
- The service contacts the **Config Server**
- It fetches the latest configuration from the Git repository
- The updated configuration is applied without restarting the service

---

## Problem 001-II: What if there are multiple services?

In a microservices architecture, manually calling the refresh endpoint for every service is not practical.

### Solution: Spring Cloud Bus

Spring Cloud Bus can be used to broadcast configuration updates across multiple services.

It works with a **message broker** such as:
- RabbitMQ
- Kafka

### How it Works

1. One service triggers a refresh event.
2. Spring Cloud Bus publishes this event to the message broker.
3. All subscribed services receive the event.
4. Each service automatically calls the refresh mechanism and updates its configuration.

### Architecture Flow
```Service (Trigger Refresh)
|
v
Spring Cloud Bus
|
v
Message Broker (RabbitMQ / Kafka)
|
v
All Microservices receive refresh event
|
v
Each service refreshes its configuration
```

This removes the need to manually invoke the refresh endpoint for every service.

---

## Problem 001-III: How does the system know when configuration changes?

A service needs to know **when a configuration change occurs in the Git repository**.

Polling the repository continuously using HTTP requests would be inefficient.

### Solution: GitHub Webhooks

GitHub Webhooks notify the system whenever changes occur in the repository.

### How it Works

1. A developer commits changes to the configuration repository.
2. GitHub triggers a webhook event.
3. The webhook sends an HTTP request to the designated service endpoint.
4. That service publishes a refresh event through **Spring Cloud Bus**.
5. All services receive the event and refresh their configuration.

### Event Flow
``` Developer commits config
|
v
GitHub Repository
|
v
GitHub Webhook
|
v
Config Server / Trigger Service
|
v
Spring Cloud Bus
|
v 
All Microservices Refresh Configuration
```


This mechanism ensures that configuration updates are automatically propagated to all services.