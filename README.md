# MAP - Multi-Application Project

This repository contains multiple Java applications demonstrating different design patterns and persistence mechanisms.

## Projects

### DuckSocialNetwork
A social networking application for managing ducks, persons, friendships, flocks, and race events with **database persistence**.

**New Feature**: Now supports H2 embedded database for data persistence!

#### Quick Start

1. **Build the project:**
   ```bash
   cd DuckSocialNetwork
   ./gradlew build
   ```

2. **Migrate data to database (first time only):**
   ```bash
   java -cp "build/libs/DuckSocialNetwork-1.0-SNAPSHOT.jar:~/.gradle/caches/modules-2/files-2.1/com.h2database/h2/2.2.224/*/h2-2.2.224.jar" org.example.MigrateFileToDatabase
   ```

3. **Run the application:**
   ```bash
   ./gradlew run
   ```

#### Features
- User management (Ducks and Persons)
- Friendship management
- Flock management for ducks
- Race event management
- **Database persistence using H2**
- File-based storage (legacy mode)

See [DuckSocialNetwork/DATABASE_README.md](DuckSocialNetwork/DATABASE_README.md) for detailed documentation.

### DuckTaskRunner
A task management application demonstrating strategy and factory patterns.

## Requirements
- Java 17 or higher
- Gradle (included via wrapper)

## License
Educational purposes only.

