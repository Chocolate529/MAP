package org.repository;

import org.database.DatabaseConnection;
import org.domain.exceptions.RepositoryException;
import org.domain.users.duck.Duck;
import org.domain.users.duck.FlyingDuck;
import org.domain.validators.DuckValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DuckDatabaseRepositoryTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    @Mock
    private DuckValidator mockValidator;

    private DuckDatabaseRepository repository;

    @BeforeEach
    void setUp() throws SQLException {
        try (MockedStatic<DatabaseConnection> mockedStatic = mockStatic(DatabaseConnection.class)) {
            mockedStatic.when(DatabaseConnection::getConnection).thenReturn(mockConnection);
            
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(false);
            
            repository = new DuckDatabaseRepository(mockValidator);
        }
    }

    @Test
    void testSaveDuckSuccess() throws SQLException {
        // Arrange
        Duck duck = createTestDuck();
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        
        try (MockedStatic<DatabaseConnection> mockedStatic = mockStatic(DatabaseConnection.class)) {
            mockedStatic.when(DatabaseConnection::getConnection).thenReturn(mockConnection);
            
            // Act
            Duck result = repository.save(duck);
            
            // Assert
            assertNull(result);
            verify(mockValidator).validate(duck);
            verify(mockPreparedStatement, atLeastOnce()).executeUpdate();
        }
    }

    @Test
    void testFindOneDuck() throws SQLException {
        // Arrange
        Duck duck = createTestDuck();
        
        try (MockedStatic<DatabaseConnection> mockedStatic = mockStatic(DatabaseConnection.class)) {
            mockedStatic.when(DatabaseConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            
            repository.save(duck);
            
            // Act
            Duck found = repository.findOne(1L);
            
            // Assert
            assertNotNull(found);
            assertEquals(1L, found.getId());
            assertEquals("ducky", found.getUsername());
        }
    }

    @Test
    void testDeleteDuck() throws SQLException {
        // Arrange
        Duck duck = createTestDuck();
        
        try (MockedStatic<DatabaseConnection> mockedStatic = mockStatic(DatabaseConnection.class)) {
            mockedStatic.when(DatabaseConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            
            repository.save(duck);
            
            // Act
            Duck deleted = repository.delete(1L);
            
            // Assert
            assertNotNull(deleted);
            assertNull(repository.findOne(1L));
            verify(mockPreparedStatement, atLeastOnce()).executeUpdate();
        }
    }

    @Test
    void testUpdateDuck() throws SQLException {
        // Arrange
        Duck duck = createTestDuck();
        
        try (MockedStatic<DatabaseConnection> mockedStatic = mockStatic(DatabaseConnection.class)) {
            mockedStatic.when(DatabaseConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            
            repository.save(duck);
            duck.setSpeed(150.0);
            
            // Act
            Duck result = repository.update(duck);
            
            // Assert
            assertNull(result);
            verify(mockValidator, atLeast(2)).validate(duck);
        }
    }

    @Test
    void testSaveNullDuck() {
        // Act & Assert
        assertThrows(RepositoryException.class, () -> repository.save(null));
    }

    @Test
    void testDeleteWithNullId() {
        // Act & Assert
        assertThrows(RepositoryException.class, () -> repository.delete(null));
    }

    private Duck createTestDuck() {
        Duck duck = new FlyingDuck("ducky", "pass123", "duck@example.com", 
                                    org.utils.enums.DuckTypes.FLYING, 100.0, 200.0);
        duck.setId(1L);
        return duck;
    }
}
