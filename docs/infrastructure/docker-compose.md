# Docker Compose

Docker Compose helps run multiple containers together with a single command.
Instead of manually starting each container, it defines all services in one
file and spins them up automatically.

## Why a common-config file?

Many microservices share the same configuration, such as:

- network settings
- environment variables
- resource limits

If we repeat this configuration in every service, the compose file becomes
large and difficult to maintain.

To follow the **DRY (Don't Repeat Yourself)** principle, we extract the
shared configuration into a separate file called `common-config.yaml`.
Services can then import this configuration and reuse it.

## Decision

Create a `docker-compose.yaml` file along with a shared
`common-config.yaml` to standardize:

- container networking
- environment variables
- resource limits

This approach reduces configuration duplication across services and
simplifies starting the entire microservices system.