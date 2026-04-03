# BankApi

A simple bank account management REST API built with Spring Boot.

## Requirements

- **Java 24** or higher
- **Maven 3.9+**

## How to Build

```bash
./mvnw clean package
```

## How to Run Tests

```bash
./mvnw test
```

## How to Run the Application

```bash
./mvnw spring-boot:run
```

## Migration Notes

- Upgraded from Spring Boot 2.7.18 to **Spring Boot 3.4.4** to support Java 24.
- Java version target updated from 8 to **24**.
- Spring Boot 3.x uses Jakarta EE 9+ namespaces (`jakarta.*`) instead of `javax.*`; no source changes were required as the codebase did not depend on the old `javax.*` APIs.
