package org.repository;

import org.database.DatabaseConnection;
import org.domain.exceptions.RepositoryException;
import org.domain.users.person.Person;
import org.domain.validators.PersonValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonDatabaseRepositoryTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    @Mock
    private PersonValidator mockValidator;

    private PersonDatabaseRepository repository;

    @BeforeEach
    void setUp() throws SQLException {
        // Mock DatabaseConnection.getConnection() to return mockConnection
        try (MockedStatic<DatabaseConnection> mockedStatic = mockStatic(DatabaseConnection.class)) {
            mockedStatic.when(DatabaseConnection::getConnection).thenReturn(mockConnection);
            
            // Mock the initial loadData() query
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(false); // Empty initial load
            
            repository = new PersonDatabaseRepository(mockValidator);
        }
    }

    @Test
    void testSavePersonSuccess() throws SQLException {
        // Arrange
        Person person = createTestPerson();
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        
        try (MockedStatic<DatabaseConnection> mockedStatic = mockStatic(DatabaseConnection.class)) {
            mockedStatic.when(DatabaseConnection::getConnection).thenReturn(mockConnection);
            
            // Act
            Person result = repository.save(person);
            
            // Assert
            assertNull(result); // Null indicates successful save
            verify(mockValidator).validate(person);
            verify(mockPreparedStatement, atLeastOnce()).executeUpdate();
        }
    }

    @Test
    void testSavePersonAlreadyExists() throws SQLException {
        // Arrange
        Person person = createTestPerson();
        
        try (MockedStatic<DatabaseConnection> mockedStatic = mockStatic(DatabaseConnection.class)) {
            mockedStatic.when(DatabaseConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            
            repository.save(person);
            
            // Act - try to save again with same ID
            Person result = repository.save(person);
            
            // Assert
            assertNull(result); // Returns null when already exists
        }
    }

    @Test
    void testFindOneSuccess() throws SQLException {
        // Arrange
        Person person = createTestPerson();
        
        try (MockedStatic<DatabaseConnection> mockedStatic = mockStatic(DatabaseConnection.class)) {
            mockedStatic.when(DatabaseConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            
            repository.save(person);
            
            // Act
            Person found = repository.findOne(1L);
            
            // Assert
            assertNotNull(found);
            assertEquals(1L, found.getId());
            assertEquals("testuser", found.getUsername());
        }
    }

    @Test
    void testFindOneNotFound() {
        // Act
        Person found = repository.findOne(999L);
        
        // Assert
        assertNull(found);
    }

    @Test
    void testFindOneWithNullId() {
        // Act & Assert
        assertThrows(RepositoryException.class, () -> repository.findOne(null));
    }

    @Test
    void testDeleteSuccess() throws SQLException {
        // Arrange
        Person person = createTestPerson();
        
        try (MockedStatic<DatabaseConnection> mockedStatic = mockStatic(DatabaseConnection.class)) {
            mockedStatic.when(DatabaseConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            
            repository.save(person);
            
            // Act
            Person deleted = repository.delete(1L);
            
            // Assert
            assertNotNull(deleted);
            assertEquals(1L, deleted.getId());
            assertNull(repository.findOne(1L));
            verify(mockPreparedStatement, atLeastOnce()).executeUpdate();
        }
    }

    @Test
    void testDeleteNonExistent() {
        // Act
        Person deleted = repository.delete(999L);
        
        // Assert
        assertNull(deleted);
    }

    @Test
    void testUpdateSuccess() throws SQLException {
        // Arrange
        Person person = createTestPerson();
        
        try (MockedStatic<DatabaseConnection> mockedStatic = mockStatic(DatabaseConnection.class)) {
            mockedStatic.when(DatabaseConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            
            repository.save(person);
            
            person.setOccupation("Senior Engineer");
            
            // Act
            Person result = repository.update(person);
            
            // Assert
            assertNull(result); // Null indicates successful update
            verify(mockValidator, atLeast(2)).validate(person);
            verify(mockPreparedStatement, atLeastOnce()).executeUpdate();
        }
    }

    @Test
    void testUpdateNonExistent() {
        // Arrange
        Person person = createTestPerson();
        
        // Act
        Person result = repository.update(person);
        
        // Assert
        assertEquals(person, result); // Returns entity if it doesn't exist
    }

    @Test
    void testFindAll() throws SQLException {
        // Arrange
        Person person1 = createTestPerson();
        Person person2 = createTestPerson();
        person2.setId(2L);
        person2.setUsername("user2");
        person2.setEmail("user2@example.com");
        
        try (MockedStatic<DatabaseConnection> mockedStatic = mockStatic(DatabaseConnection.class)) {
            mockedStatic.when(DatabaseConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            
            repository.save(person1);
            repository.save(person2);
            
            // Act
            Iterable<Person> all = repository.findAll();
            
            // Assert
            assertNotNull(all);
            int count = 0;
            for (Person p : all) {
                count++;
            }
            assertEquals(2, count);
        }
    }

    private Person createTestPerson() {
        Person person = new Person(
            "testuser",
            "password123",
            "test@example.com",
            "Test",
            "User",
            "Engineer",
            LocalDate.of(1990, 1, 1),
            8.5
        );
        person.setId(1L);
        return person;
    }
}
