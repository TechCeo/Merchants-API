# ADR 0001: Architecture And Modernization Direction

## Status

Accepted

## Context

The Merchant API began as a small Spring Boot CRUD-style service using Java 8, Spring Boot 2.x, hardcoded database credentials, Hibernate schema mutation, loosely typed responses, and RPC-style endpoints.

The modernization goal is to make the service robust while preserving a KISS design: predictable architecture, explicit configuration, strong API contracts, production-safe persistence, and useful tests without unnecessary distributed infrastructure.

## Decision

We will modernize the service around the following choices:

- Java 21 as the runtime and language baseline.
- Spring Boot 3.5 for the application framework.
- Feature-oriented packages under `com.portfolio.merchantapi`.
- Spring MVC REST endpoints with request validation at the HTTP boundary.
- Spring `ProblemDetail` responses for RFC 7807-style error payloads.
- Flyway plain-SQL migrations for schema ownership.
- Oracle as the production-style database target.
- H2 in Oracle compatibility mode for fast test execution.
- Database-backed idempotency for transaction creation.
- OpenAPI/Swagger UI generated from code annotations.
- Docker Compose for local orchestration with Oracle Free.

## Rationale

Java 21 provides a modern LTS baseline with strong ecosystem support and clean compatibility with Spring Boot 3.x.

Spring Boot 3.5 keeps the project current while remaining familiar to enterprise Java teams. It also aligns the codebase with Jakarta EE namespaces and modern Spring testing support.

Flyway was chosen over Hibernate schema mutation because schema changes should be reviewed, versioned, repeatable, and safe. Plain SQL migrations are easy to inspect and align with the KISS philosophy.

`ProblemDetail` was chosen for errors because it is built into Spring 6, follows RFC 7807, and avoids custom error envelope sprawl.

Database-backed idempotency was chosen for transaction creation because duplicate payment-like writes are a real business risk. A dedicated table and primary key constraint provide durable duplicate detection without adding Redis or another distributed cache.

OpenAPI annotations were chosen so API documentation stays close to the controller and DTO code.

## Deliberate Non-Goals

We are not adding Redis, message queues, Kubernetes manifests, or distributed tracing in this phase.

Caching is intentionally skipped. The current system does not have evidence of read pressure or expensive repeated lookups that justify cache invalidation complexity. The priority is correctness, maintainability, and clear business invariants.

## Consequences

The application is now more production-like while remaining approachable:

- Bad requests are rejected before service logic.
- Missing resources and duplicate transaction attempts return consistent HTTP errors.
- Database schema is owned by migrations.
- Tests can run without a local Oracle instance.
- Local infrastructure can be started through Docker Compose.

The tradeoff is that idempotency records are persisted until explicitly cleaned up. The `created_at` index exists to support a future scheduled cleanup or administrative purge if retention requirements are defined.
