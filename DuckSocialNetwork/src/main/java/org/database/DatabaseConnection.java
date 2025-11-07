package org.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

/**
 * Manages database connections and schema initialization
 */
public class DatabaseConnection {
    private static final String DB_URL = "jdbc:h2:./ducksocialnetwork;AUTO_SERVER=TRUE";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";
    private static final String SCHEMA_FILE = "/db/schema.sql";
    
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
     * Initialize database schema from SQL file
     */
    public static void initializeSchema() throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Read SQL schema from file
            String sql = readSchemaFile();
            
            // Split by semicolon and execute each statement
            String[] statements = sql.split(";");
            for (String statement : statements) {
                String trimmed = statement.trim();
                // Skip empty statements and comment-only lines
                if (!trimmed.isEmpty()) {
                    // Remove comment lines but keep the SQL
                    String cleanSql = removeComments(trimmed);
                    if (!cleanSql.isEmpty()) {
                        stmt.execute(cleanSql);
                    }
                }
            }
        } catch (IOException e) {
            throw new SQLException("Failed to read schema file: " + e.getMessage(), e);
        }
    }
    
    /**
     * Remove SQL comments from a statement
     */
    private static String removeComments(String sql) {
        StringBuilder result = new StringBuilder();
        String[] lines = sql.split("\n");
        for (String line : lines) {
            String trimmedLine = line.trim();
            // Skip lines that start with --
            if (!trimmedLine.startsWith("--")) {
                // Remove inline comments
                int commentIndex = line.indexOf("--");
                if (commentIndex >= 0) {
                    result.append(line, 0, commentIndex).append("\n");
                } else {
                    result.append(line).append("\n");
                }
            }
        }
        return result.toString().trim();
    }
    
    /**
     * Read the schema SQL file from resources
     */
    private static String readSchemaFile() throws IOException {
        InputStream inputStream = DatabaseConnection.class.getResourceAsStream(SCHEMA_FILE);
        if (inputStream == null) {
            throw new IOException("Schema file not found: " + SCHEMA_FILE);
        }
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines().collect(Collectors.joining("\n"));
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
