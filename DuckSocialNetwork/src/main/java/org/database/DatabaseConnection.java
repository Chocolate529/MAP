package org.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Manages database connections and schema initialization
 */
public class DatabaseConnection {
    private static final String DB_URL = "jdbc:h2:./ducksocialnetwork;AUTO_SERVER=TRUE";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";
    
    private static Connection connection = null;
    
    /**
     * Get a connection to the database
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        }
        return connection;
    }
    
    /**
     * Initialize database schema
     */
    public static void initializeSchema() throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Create Persons table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS persons (
                    id BIGINT PRIMARY KEY,
                    username VARCHAR(255) NOT NULL UNIQUE,
                    password VARCHAR(255) NOT NULL,
                    email VARCHAR(255) NOT NULL,
                    first_name VARCHAR(255),
                    last_name VARCHAR(255),
                    occupation VARCHAR(255),
                    date_of_birth DATE,
                    empathy_level DOUBLE
                )
            """);
            
            // Create Ducks table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS ducks (
                    id BIGINT PRIMARY KEY,
                    username VARCHAR(255) NOT NULL UNIQUE,
                    password VARCHAR(255) NOT NULL,
                    email VARCHAR(255) NOT NULL,
                    duck_type VARCHAR(50) NOT NULL,
                    speed DOUBLE,
                    rezistance DOUBLE
                )
            """);
            
            // Create Friendships table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS friendships (
                    id BIGINT PRIMARY KEY,
                    user1_id BIGINT NOT NULL,
                    user2_id BIGINT NOT NULL
                )
            """);
            
            // Create Flocks table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS flocks (
                    id BIGINT PRIMARY KEY,
                    flock_name VARCHAR(255) NOT NULL
                )
            """);
            
            // Create Flock Members table (junction table)
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS flock_members (
                    flock_id BIGINT NOT NULL,
                    duck_id BIGINT NOT NULL,
                    PRIMARY KEY (flock_id, duck_id)
                )
            """);
            
            // Create Race Events table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS race_events (
                    id BIGINT PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    max_time DOUBLE
                )
            """);
            
            // Create Race Event Participants table (junction table)
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS race_event_participants (
                    event_id BIGINT NOT NULL,
                    duck_id BIGINT NOT NULL,
                    PRIMARY KEY (event_id, duck_id)
                )
            """);
        }
    }
    
    /**
     * Close the database connection
     */
    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
}
