# BankApi

A simple bank account management REST API built with Spring Boot.

## Requirements

- **Java 17** or higher

## How to Build

```bash
./gradlew build
```

## How to Run Tests

```bash
./gradlew test
```

## How to Run the Application

```bash
./gradlew bootRun
```

## Migration Notes

- Upgraded from Spring Boot 2.7.18 to **Spring Boot 3.4.4** to support Java 17+.
- Java version target updated from 8 to **17**.
- Spring Boot 3.x uses Jakarta EE 9+ namespaces (`jakarta.*`) instead of `javax.*`; no source changes were required as the codebase did not depend on the old `javax.*` APIs.
- Build system migrated from Maven to **Gradle 8.13**.
