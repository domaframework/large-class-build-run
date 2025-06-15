# Large Class Build Run

A Doma framework test project for benchmarking build performance with large numbers of classes.

## Overview

This project is designed to test the build performance of Doma's annotation processor by automatically generating 200+ DAO interfaces and entity classes.
It includes comprehensive test suites for all generated DAOs to verify the functionality of batch operations and aggregate strategies.

## Requirements

- Java 17 or higher
- Gradle 8.5 or higher

## Setup

```bash
# Clone the repository
git clone https://github.com/domaframework/large-class-build-run.git
cd large-class-build-run

# Build the project
./gradlew build
```

## Usage

### Basic Commands

```bash
# Build the project
./gradlew build

# Run the application
./gradlew run

# Run tests
./gradlew test

# Clean build artifacts
./gradlew clean
```

### Code Generation

Commands to generate large numbers of test classes:

```bash
# Generate all DAO, entity, and SQL files (default: 200 classes)
./gradlew generateAll

# Generate only DAO classes
./gradlew generateDAOs

# Generate only entity classes
./gradlew generateEntities

# Generate only SQL files
./gradlew generateSqlFiles

# Generate test classes for all DAOs
./gradlew generateTests

# Remove all generated files
./gradlew removeGeneratedFiles
```

The number of generated classes can be modified by changing the `generationSize` variable in `build.gradle.kts`.

### Code Formatting

```bash
# Format code using Google Java Format
./gradlew spotlessApply

# Check code formatting
./gradlew spotlessCheck
```

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/example/
│   │       ├── dao/          # Generated DAO interfaces
│   │       ├── entity/       # Generated entity classes
│   │       ├── domain/       # Domain type definitions
│   │       ├── Employee.java # Base entity template
│   │       ├── EmployeeDao.java # Base DAO template
│   │       └── Main.java     # Main class
│   └── resources/
│       └── META-INF/com/example/dao/  # SQL files
└── test/
    └── java/
        └── com/example/
            └── dao/          # Generated DAO test classes
```

## Features

- **Batch Operations**: Support for batch insert, update, and delete operations
- **Aggregate Strategy**: Efficient loading of entity associations (Employee-Department relationships)
- **Performance Testing**: Designed to test annotation processor performance with 200+ entities
- **Comprehensive Test Suite**: Automated test generation for all DAOs

## Technology Stack

- **Doma 3.9.0**: Compile-time ORM framework with aggregate strategy support
- **H2 Database**: In-memory database for testing
- **JUnit 5**: Testing framework
- **SLF4J/Logback**: Logging
- **Spotless**: Code formatter (Google Java Format)

## License

This project is licensed under the Apache License 2.0.