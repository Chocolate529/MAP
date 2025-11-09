package org.service;

import org.domain.users.person.Person;
import org.domain.validators.PersonValidator;
import org.repository.Repository;
import org.service.utils.IdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonsServiceTest {

    @Mock
    private Repository<Long, Person> mockRepository;

    @Mock
    private PersonValidator mockValidator;

    @Mock
    private IdGenerator<Long> mockIdGenerator;

    private PersonsService service;

    @BeforeEach
    void setUp() {
        service = new PersonsService(mockValidator, mockRepository, mockIdGenerator);
    }

    @Test
    void testAddPerson() {
        // Arrange
        Person person = createTestPerson();
        person.setId(null); // Clear ID before save
        when(mockIdGenerator.nextId()).thenReturn(1L);
        when(mockRepository.save(any(Person.class))).thenReturn(person);
        
        // Act
        Person result = service.save(person);
        
        // Assert
        assertNotNull(result);
        verify(mockValidator).validate(person);
        verify(mockRepository).save(person);
    }

    @Test
    void testGetPerson() {
        // Arrange
        Person person = createTestPerson();
        when(mockRepository.findOne(1L)).thenReturn(person);
        
        // Act
        Person result = service.findOne(1L);
        
        // Assert
        assertNotNull(result);
        assertEquals(person, result);
        verify(mockRepository).findOne(1L);
    }

    @Test
    void testGetAllPersons() {
        // Arrange
        Person person1 = createTestPerson();
        Person person2 = createTestPerson();
        person2.setId(2L);
        when(mockRepository.findAll()).thenReturn(Arrays.asList(person1, person2));
        
        // Act
        Iterable<Person> result = service.findAll();
        
        // Assert
        assertNotNull(result);
        verify(mockRepository).findAll();
    }

    @Test
    void testDeletePerson() {
        // Arrange
        Person person = createTestPerson();
        when(mockRepository.delete(1L)).thenReturn(person);
        
        // Act
        Person result = service.delete(1L);
        
        // Assert
        assertNotNull(result);
        assertEquals(person, result);
        verify(mockRepository).delete(1L);
    }

    @Test
    void testUpdatePerson() {
        // Arrange
        Person person = createTestPerson();
        when(mockRepository.update(person)).thenReturn(null);
        
        // Act
        Person result = service.update(person);
        
        // Assert
        assertNull(result);
        verify(mockRepository).update(person);
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
