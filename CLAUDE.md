# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Kindergarten is a Spring Boot 3.4 application written in Kotlin 2.0, using Supabase (PostgreSQL) as its database. It includes Ktor HTTP client for external API calls.

## Build & Development Commands

```bash
# Build
./gradlew build

# Run the application
./gradlew bootRun

# Run all tests
./gradlew test

# Run a single test class
./gradlew test --tests "com.sotti.kindergarten.SomeTest"

# Run a single test method
./gradlew test --tests "com.sotti.kindergarten.SomeTest.methodName"

# Lint (ktlint)
./gradlew ktlintCheck

# Auto-fix lint issues
./gradlew ktlintFormat

# Clean build
./gradlew clean build
```

## Tech Stack

- **Language**: Kotlin 2.0.21, JVM 17
- **Framework**: Spring Boot 3.4.2 (Web, JPA, Validation)
- **Database**: PostgreSQL via Supabase, Hibernate with `ddl-auto: validate`
- **HTTP Client**: Ktor 3.0.3 (CIO engine, Jackson serialization)
- **Linting**: ktlint 1.5.0 via `org.jlleitschuh.gradle.ktlint` plugin
- **Testing**: JUnit 5, Spring Boot Test
- **Build**: Gradle 8.12 (Kotlin DSL)

## Architecture

- Base package: `com.sotti.kindergarten`
- Standard Spring Boot layered architecture
- JPA entities use `allOpen` plugin for `@Entity`, `@MappedSuperclass`, `@Embeddable`
- Database schema is externally managed (Hibernate validates only, does not generate DDL)

## Environment Setup

Copy `.env.example` to `.env` and fill in Supabase database credentials. Environment variables used:
- `SUPABASE_HOST`, `SUPABASE_PORT`, `SUPABASE_DATABASE`, `SUPABASE_USER`, `SUPABASE_PASSWORD`

## Testing

Tests use `@ActiveProfiles("local")`. Ensure a local/test database profile is configured when adding integration tests.
