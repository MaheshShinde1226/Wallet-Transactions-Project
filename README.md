# Wallet Transactions Service

A high-performance wallet service implemented with Spring Boot 3 and Java 17, supporting concurrent balance updates with transactional safety.
The service exposes REST APIs to deposit, withdraw, and retrieve wallet balances, designed to operate correctly under high load (1000 RPS per wallet).

# 🚀 Tech Stack
- Java 17
- Spring Boot 3
- Spring Data JPA
- PostgreSQL
- Liquibase (database migrations)
- Docker & Docker Compose
- JUnit 5
- Testcontainers

# 📌 Functional Requirements
- Deposit and withdraw money from wallets
- Retrieve wallet balance
- Handle invalid requests gracefully (no 5XX errors)
- Ensure consistency under concurrent access
- Externalized configuration (no rebuild required)

# 📡 REST API
Change Wallet Balance
POST /api/v1/wallet
{
  "walletId": "11111111-1111-1111-1111-111111111111",
  "operationType": "DEPOSIT",
  "amount": 1000
}

# operationType
DEPOSIT
WITHDRAW

# Get Wallet Balance
GET /api/v1/wallets/{walletId}
Response
{
  "walletId": "11111111-1111-1111-1111-111111111111",
  "balance": 5000
}

# ⚠️ Error Handling
- Scenario	HTTP Status	Error Code
- Wallet not found	404	WALLET_NOT_FOUND
- Insufficient funds	400	INSUFFICIENT_FUNDS
- Invalid JSON / validation	400	INVALID_REQUEST
- Business errors never return 500.

# 🧠 Concurrency Strategy
- Uses optimistic locking via JPA @Version
- All balance updates are transactional
- Prevents lost updates under heavy concurrency
- Suitable for 1000 RPS per wallet

# 🗄 Database Schema
CREATE TABLE wallets (
    id UUID PRIMARY KEY,
    balance BIGINT NOT NULL,
    version BIGINT NOT NULL
);


Managed exclusively by Liquibase
Hibernate DDL auto-generation disabled

# 🐳 Running with Docker
# Prerequisites
- Docker
- Docker Compose

# Start Application & Database
  docker compose up --build

# Services:
Application: http://localhost:8080
PostgreSQL: localhost:5432

# ⚙️ Configuration
All settings are externalized using environment variables:
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD

# 🧪 Testing
- Integration Tests
- Real PostgreSQL using Testcontainers
- Liquibase migrations executed during tests
- REST endpoints tested end-to-end
- Includes concurrent update tests

# Run tests:
./mvnw test

# 📂 Project Structure
wallet-service/
├── src/main/java
│   └── com.payment.transactions
│       ├── controller
│       ├── service
│       ├── repository
│       ├── entity
│       └── exception
├── src/main/resources
│   └── db/changelog
├── src/test/java
│   └── WalletIntegrationTest
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md

# 🛠 Build Locally (Without Docker)
./mvnw clean package
./mvnw spring-boot:run

# PostgreSQL must be running on:
jdbc:postgresql://localhost:5432/wallet

# ✅ Key Design Decisions
- Optimistic locking instead of synchronized blocks
- No in-memory database (H2 avoided)
- Liquibase as single source of schema truth
- Testcontainers for realistic testing
- Stateless application, scalable horizontally

