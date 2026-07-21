# Digital Wallet App

A Spring Boot backend service for managing digital wallet operations — balances, transactions, and secure account access — built as part of a backend training program at Eastnets.

This service is one half of a two-service, event-driven system. It emits structured logs to an **Apache Kafka** topic (`wallet-logs`) via a **Log4j2 Kafka Appender**, which are consumed in real time by the companion [Log Alert System](https://github.com/emran-youssef/log_monitoring_alert_system) for classification and alerting.

## Features

- **Wallet Management** — create wallets, check balances, and process transactions via REST endpoints
- **JWT Authentication & Role-Based Access Control (RBAC)** — secured endpoints with Spring Security
- **Relational Data Modeling** — JPA/Hibernate entities with Flyway-managed schema migrations
- **Structured Logging Pipeline** — logs streamed to Kafka in real time via Log4j2's built-in Kafka Appender (no third-party logging library required)
- **Decoupled Architecture** — the only shared contract with the Log Alert System is the Kafka topic name; this service has no direct knowledge of the consumer

## Tech Stack

- Java, Spring Boot
- Spring Data JPA / Hibernate
- Spring Security (JWT-based authentication, RBAC)
- MySQL
- Flyway (schema migrations)
- Apache Kafka (Log4j2 Kafka Appender for log streaming)
- Maven

## Architecture
┌─────────────────────┐        wallet-logs        ┌──────────────────────┐
│  Digital Wallet App  │ ─────────(Kafka topic)───▶ │  Log Alert System     │
│  (this repo)         │                            │  (consumer/classifier)│
└─────────────────────┘                            └──────────────────────┘


Log lines produced by this application (via Log4j2) are published to the `wallet-logs` topic. The Log Alert System consumes them independently — this app has no dependency on or awareness of the consumer.

## Getting Started

### Prerequisites

- Java 17+ (or the JDK version matching your `pom.xml`)
- Maven
- MySQL
- Apache Kafka broker (KRaft mode) running locally

### Kafka Setup

```bash
docker run -d --name kafka-local -p 9092:9092 apache/kafka:3.8.0
```

### Configuration

Update `application.yaml` with your database and Kafka broker settings:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/wallet_db
    username: <your-username>
    password: <your-password>
  flyway:
    enabled: true
```

Ensure `log4j2.xml` is correctly named and on the classpath so the Kafka appender is active.

### Run the App

```bash
mvn clean install
mvn spring-boot:run
```

The application starts on the configured port (default: `8080`).

## API Endpoints

### Users — `/users`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/users/register` | Register a new user |
| POST | `/users/login` | Authenticate a user and receive a JWT |
| GET | `/users/email/{email}` | Get user details by email |
| GET | `/users/{id}` | Get user details by ID |

### Wallets — `/wallets`

| Method | Endpoint | Description |
|---|---|---|
| GET | `/wallets/balance/{walletId}` | Get the balance of a wallet by ID |
| GET | `/wallets/user/{userId}` | Get wallet details by owning user ID |

### Transactions — `/transactions`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/transactions/deposit/{walletId}` | Deposit an amount into a wallet |
| POST | `/transactions/withdraw/{walletId}` | Withdraw an amount from a wallet |
| POST | `/transactions/transfer` | Transfer funds between wallets |
| GET | `/transactions/{transactionId}` | Get transaction details by ID |
| GET | `/transactions/wallet/{walletId}` | Get all transactions for a wallet |

### Transaction History — `/history`

| Method | Endpoint | Description |
|---|---|---|
| GET | `/history/user/{userId}` | Get transaction history for a user |
| GET | `/history/wallet/{walletId}` | Get transaction history for a wallet |
| GET | `/history/wallet/{walletId}/type/{type}` | Get transaction history for a wallet filtered by type |

## Notes

- `ddl-auto` is set to `validate`, not `update` — Flyway owns schema migrations
- DTOs use static factory methods (`toDto()`) rather than `@JsonIgnore` or circular reference annotations for serialization
- Uses Jackson 3 (`tools.jackson.*`), not Jackson 2 — a Spring Boot 4 compatibility distinction

## Related Repository

- [Log Monitoring & Alert System](https://github.com/emran-youssef/log_monitoring_alert_system) — consumes this app's Kafka logs, classifies them, and triggers alerts


