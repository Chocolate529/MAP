package org.database;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseConnectionTest {

    @AfterEach
    void tearDown() {
        DatabaseConnection.close();
    }

    @Test
    void testGetConnection() throws SQLException {
        // Act
        Connection conn = DatabaseConnection.getConnection();
        
        // Assert
        assertNotNull(conn);
        assertFalse(conn.isClosed());
    }

    @Test
    void testGetConnectionReturnsSameInstance() throws SQLException {
        // Act
        Connection conn1 = DatabaseConnection.getConnection();
        Connection conn2 = DatabaseConnection.getConnection();
        
        // Assert
        assertSame(conn1, conn2);
    }

    @Test
    void testInitializeSchema() throws SQLException {
        // Act
        DatabaseConnection.initializeSchema();
        
        // Assert - verify tables were created
        Connection conn = DatabaseConnection.getConnection();
        Statement stmt = conn.createStatement();
        
        // Check if persons table exists
        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'PERSONS'");
        assertTrue(rs.next());
        assertEquals(1, rs.getInt(1));
        
        // Check if ducks table exists
        rs = stmt.executeQuery("SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'DUCKS'");
        assertTrue(rs.next());
        assertEquals(1, rs.getInt(1));
        
        // Check if friendships table exists
        rs = stmt.executeQuery("SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'FRIENDSHIPS'");
        assertTrue(rs.next());
        assertEquals(1, rs.getInt(1));
        
        // Check if flocks table exists
        rs = stmt.executeQuery("SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'FLOCKS'");
        assertTrue(rs.next());
        assertEquals(1, rs.getInt(1));
        
        // Check if race_events table exists
        rs = stmt.executeQuery("SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'RACE_EVENTS'");
        assertTrue(rs.next());
        assertEquals(1, rs.getInt(1));
        
        rs.close();
        stmt.close();
    }

    @Test
    void testCloseConnection() throws SQLException {
        // Arrange
        Connection conn = DatabaseConnection.getConnection();
        assertFalse(conn.isClosed());
        
        // Act
        DatabaseConnection.close();
        
        // Assert
        assertTrue(conn.isClosed());
    }

    @Test
    void testMultipleInitializationsDoNotFail() throws SQLException {
        // Act - initialize schema multiple times
        DatabaseConnection.initializeSchema();
        DatabaseConnection.initializeSchema();
        
        // Assert - should not throw exception due to IF NOT EXISTS
        Connection conn = DatabaseConnection.getConnection();
        assertNotNull(conn);
    }
}
