# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

### Build and Run
```bash
# Build the project
./gradlew build

# Run the application
./gradlew run

# Clean build artifacts
./gradlew clean
```

### Code Generation
This project uses code generation to create multiple DAO and entity classes for testing large-scale builds:
```bash
# Generate all DAO, entity, and SQL files (creates 200 classes by default)
./gradlew generateAll

# Generate only DAO classes
./gradlew generateDAOs

# Generate only entity classes
./gradlew generateEntities

# Generate only SQL files
./gradlew generateSqlFiles

# Remove all generated files
./gradlew removeGeneratedFiles
```

### Code Formatting
```bash
# Format code using Spotless (Google Java Format)
./gradlew spotlessApply

# Check code formatting
./gradlew spotlessCheck
```

### Testing
```bash
# Run tests (JUnit 5)
./gradlew test
```

## Architecture

This is a Doma framework test project designed to test build performance with large numbers of classes. Key components:

1. **Doma Framework**: A compile-time ORM framework for Java that generates implementation classes from DAO interfaces
   - Uses annotation processing to generate DAO implementations
   - SQL files are linked to DAO methods by naming convention

2. **Generated Classes Structure**:
   - `com.example.dao.Employee{1-200}Dao` - DAO interfaces for database operations
   - `com.example.entity.Employee{1-200}` - Entity classes with associations
   - `com.example.entity.Department{1-200}` - Department entities
   - SQL files in `src/main/resources/META-INF/com/example/dao/Employee{X}Dao/`

3. **Base Classes**:
   - `Employee` and `EmployeeDao` - Base templates showing the pattern
   - `Department` - Related entity with one-to-many relationship
   - `Main` - Entry point that demonstrates basic usage with H2 in-memory database

4. **Domain Types**:
   - `Name` - Custom domain type for name fields
   - `URLConverter` - Converts between URL and String for database storage

The project uses H2 in-memory database for testing and includes SLF4J/Logback for logging. The generation size (default 200) can be modified in `build.gradle.kts` by changing the `generationSize` variable.