# DuckSocialNetwork - Database Persistence

## Overview

DuckSocialNetwork is a social networking application for managing ducks, persons, friendships, flocks, and race events. The application now supports both **file-based** and **database-based** persistence.

## Features

- User management (Ducks and Persons)
- Friendship management
- Flock management for ducks
- Race event management
- **Database persistence using H2 embedded database**
- Backward compatible file-based storage

## Database Persistence

### Configuration

The application can be configured to use either file-based or database-based persistence by changing the `USE_DATABASE` flag in the `Main.java` file:

```java
private static final boolean USE_DATABASE = true; // Set to false to use file storage
```

- **`true`**: Uses H2 database for persistence (default)
- **`false`**: Uses text files for persistence (legacy mode)

### Database Schema

The H2 database is automatically initialized with the following tables:

- **persons**: Stores person user data
- **ducks**: Stores duck user data
- **friendships**: Stores friendship relationships between users
- **flocks**: Stores flock information
- **flock_members**: Junction table for flock memberships
- **race_events**: Stores race event information
- **race_event_participants**: Junction table for race event participants

### Database Files

The H2 database creates the following files in the project root directory:

- `ducksocialnetwork.mv.db`: Main database file
- `ducksocialnetwork.trace.db`: Database trace file (if logging is enabled)

These files are automatically excluded from version control via `.gitignore`.

### Database Connection

The database connection is managed by the `DatabaseConnection` class:

- **URL**: `jdbc:h2:./ducksocialnetwork;AUTO_SERVER=TRUE`
- **Username**: `sa`
- **Password**: (empty)
- **Connection**: Automatically initialized and closed

## Building and Running

### Prerequisites

- Java 17 or higher
- Gradle (included via wrapper)

### Build

```bash
./gradlew build
```

### Run

```bash
./gradlew run
```

Or run the main class directly:

```bash
java -cp build/libs/DuckSocialNetwork-1.0-SNAPSHOT.jar org.example.Main
```

## Data Migration

### From Files to Database

To migrate from file-based storage to database storage, use the provided migration utility:

```bash
# Build the project first
./gradlew build

# Run the migration utility
java -cp "build/libs/DuckSocialNetwork-1.0-SNAPSHOT.jar:~/.gradle/caches/modules-2/files-2.1/com.h2database/h2/2.2.224/*/h2-2.2.224.jar" org.example.MigrateFileToDatabase
```

The migration utility will:
1. Initialize the database schema
2. Load all data from the text files in `src/main/resources/`
3. Clear any existing data in the database
4. Migrate all entities to the database:
   - Persons
   - Ducks
   - Friendships
   - Flocks
   - Race Events

After successful migration, you can set `USE_DATABASE = true` in `Main.java` to use the database.

### Verification

To verify that the database persistence is working correctly, run:

```bash
java -cp "build/libs/DuckSocialNetwork-1.0-SNAPSHOT.jar:~/.gradle/caches/modules-2/files-2.1/com.h2database/h2/2.2.224/*/h2-2.2.224.jar" org.example.DatabaseVerification
```

This will test all CRUD operations and report the results.

### From Database to Files

The database repositories only read from files during initial load. To export data back to files, you would need to implement a custom export function.

## Repository Classes

### File-based Repositories
- `PersonFileRepository`
- `DuckFileRepository`
- `FriendshipFileRepository`
- `FlockFileRepository`
- `RaceEventFileRepository`

### Database Repositories
- `PersonDatabaseRepository`
- `DuckDatabaseRepository`
- `FriendshipDatabaseRepository`
- `FlockDatabaseRepository`
- `RaceEventDatabaseRepository`

All repositories implement the same `Repository<ID, E>` interface, ensuring consistency across storage methods.

## Utility Classes

### DatabaseVerification
A utility class to verify database persistence functionality. Tests all CRUD operations:
- Create
- Read
- Update
- Delete

Run with:
```bash
java -cp "build/libs/DuckSocialNetwork-1.0-SNAPSHOT.jar:<h2-jar-path>" org.example.DatabaseVerification
```

### MigrateFileToDatabase
A utility class to migrate data from text files to the database. Automatically:
- Initializes database schema
- Loads data from files
- Clears existing database data
- Migrates all entities to database

Run with:
```bash
java -cp "build/libs/DuckSocialNetwork-1.0-SNAPSHOT.jar:<h2-jar-path>" org.example.MigrateFileToDatabase
```

## Dependencies

- **H2 Database**: 2.2.224 - Embedded Java SQL database

## Architecture

The application follows a layered architecture:

1. **Domain Layer**: Entity classes and validators
2. **Repository Layer**: Data persistence (file or database)
3. **Service Layer**: Business logic
4. **UI Layer**: Console-based user interface

## License

This project is for educational purposes.
