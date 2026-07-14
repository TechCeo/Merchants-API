# Merchant API

Merchant API is a Spring Boot REST service for managing merchants and their payment transactions. The project is being modernized as a clean, production-minded Java backend portfolio application with simple architecture, explicit configuration, and database migrations.

## Current Stack

- Java 21
- Spring Boot 3.5
- Spring Web
- Spring Data JPA / Hibernate
- Oracle JDBC for local and production-style runtime
- Flyway for SQL database migrations
- H2 for test execution
- Springdoc OpenAPI / Swagger UI
- Docker Compose with Oracle Free
- Maven Wrapper

## API Surface

```text
POST /api/v1/merchants
GET  /api/v1/merchants/{merchantId}

POST /api/v1/transactions
GET  /api/v1/transactions
GET  /api/v1/merchants/{merchantId}/transactions
```

Transaction creation requires an `Idempotency-Key` header. Reusing a key returns `409 Conflict`.

Swagger UI is available at:

```text
http://localhost:8080/swagger-ui.html
```

## Configuration

Runtime database settings are externalized through environment variables. Local defaults are provided for convenience.

```text
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
```

Default local values:

```text
jdbc:oracle:thin:@localhost:1521:orcl
system
oracle
```

The application uses Flyway migrations from profile-specific folders:

```text
src/main/resources/db/migration/oracle
src/main/resources/db/migration/h2
```

JPA schema generation is disabled for safety. Hibernate validates the schema created by Flyway.

## Profiles

The default profile targets Oracle using externalized datasource settings.

The `test` profile uses an in-memory H2 database in Oracle compatibility mode so tests can run without a local Oracle instance.

## Build And Test

```powershell
.\mvnw.cmd clean compile
.\mvnw.cmd clean test
```

## Docker Compose

Start the API with Oracle Free:

```powershell
docker compose up --build
```

The API is exposed on `localhost:8080`, and Oracle is exposed on `localhost:1521`.

## Local Run

Set database credentials if your Oracle setup differs from the defaults:

```powershell
$env:SPRING_DATASOURCE_URL="jdbc:oracle:thin:@localhost:1521:orcl"
$env:SPRING_DATASOURCE_USERNAME="system"
$env:SPRING_DATASOURCE_PASSWORD="oracle"
.\mvnw.cmd spring-boot:run
```

## Architecture Decisions

Architecture rationale is documented in:

```text
docs/adr/0001-architecture-and-modernization.md
```
