# 🐞 Eureka Service URL Not Overriding (Case-Sensitive Map Binding Issue)

## 📌 Summary

In a microservices setup using **Spring Boot**, **Spring Cloud Config Server**, and **Netflix Eureka**, the Eureka server URL was not overridden correctly despite being set via environment variables and config server.

The service continued to use:

```
http://localhost:8761/eureka
```

instead of:

```
http://serviceregistry:8761/eureka
```

---

## ⚠️ Root Cause

This issue occurred due to **case-sensitive Map key mismatch** in `EurekaClientConfigBean`.

### 🔹 Internal Behavior

Eureka stores service URLs in:

```java
Map<String, String> serviceUrl
```

Default initialization:

```java
"defaultZone" → "http://localhost:8761/eureka/"
```

Eureka retrieves using:

```java
serviceUrl.get("defaultZone")
```

---

## ❌ Problem Breakdown

### 1. Incorrect Key in Config Repo

```yaml
eureka:
  client:
    service-url:
      default-Zone: http://localhost:8761/eureka
```

👉 Key becomes:

```
"default-Zone"
```

---

### 2. Environment Variable Binding Issue

```bash
EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://serviceregistry:8761/eureka
```

👉 Key becomes:

```
"defaultzone"
```

---

## 🔥 Resulting Map State

| Source           | Key          | Value                        |
| ---------------- | ------------ | ---------------------------- |
| Default (Eureka) | defaultZone  | http://localhost:8761/eureka |
| Config Server    | default-Zone | http://localhost:8761/eureka |
| Environment Var  | defaultzone  | http://serviceregistry:8761  |

👉 Eureka looks for:

```
"defaultZone"
```

👉 So it uses:

```
http://localhost:8761 ❌
```

---

## 🧠 Why This Happens

* Spring Boot uses **relaxed binding** for normal properties
* BUT **Map keys are case-sensitive and not normalized**

### ❗ Key Insight

```
"defaultZone" ≠ "defaultzone" ≠ "default-Zone"
```

---

## ✅ Fix

Update config repository:

```yaml
eureka:
  client:
    serviceUrl:
      defaultZone: ${EUREKA_CLIENT_SERVICEURL_DEFAULTZONE:http://localhost:8761/eureka}
```

---

## ✔️ Why This Fix Works

* Uses correct key: `defaultZone`
* Allows environment variable override
* Provides fallback for local development
* Ensures compatibility across environments (Docker, local)

---

## 🔍 Debugging Steps

### 1. Check effective configuration

```
GET /actuator/env/eureka.client.serviceUrl.defaultZone
```

### 2. Inspect property sources

```
GET /actuator/env
```

### 3. Verify container environment

```bash
docker exec <container> env
```

---

## ⚠️ Secondary Observation

```yaml
spring:
  config:
    import: optional:configserver:http://localhost:9296
```

Combined with:

```bash
SPRING_CONFIG_IMPORT=configserver:http://configserver:9296
```

### Behavior:

* Tries localhost → fails
* Then uses configserver → succeeds

👉 Not harmful, but adds noise to logs

---

## 🧠 Key Learnings

* Map binding in Spring is **strict (case-sensitive)**
* Config Server values can override environment variables
* Always verify **final runtime config using Actuator**
* Avoid hardcoded `localhost` in distributed environments

---

## 🚀 Best Practices

* Always use **camelCase keys** for Map-based properties
* Use environment placeholders with fallback
* Avoid hardcoding service URLs
* Validate configs using Actuator before debugging code

---

## 🏁 Conclusion

This issue highlights a subtle but critical difference between:

* **Property binding**
* **Map key resolution**

Understanding this prevents silent misconfigurations in production systems.
