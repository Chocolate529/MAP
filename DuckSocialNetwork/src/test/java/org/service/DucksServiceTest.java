package org.service;

import org.domain.users.duck.Duck;
import org.domain.users.duck.FlyingDuck;
import org.domain.users.duck.flock.Flock;
import org.domain.validators.DuckValidator;
import org.repository.Repository;
import org.service.utils.IdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DucksServiceTest {

    @Mock
    private Repository<Long, Duck> mockRepository;

    @Mock
    private DuckValidator mockValidator;

    @Mock
    private IdGenerator<Long> mockIdGenerator;

    @Mock
    private FlockService mockFlockService;

    private DucksService service;

    @BeforeEach
    void setUp() {
        service = new DucksService(mockValidator, mockRepository, mockIdGenerator);
        service.setFlockService(mockFlockService);
    }

    @Test
    void testAddDuck() {
        // Arrange
        Duck duck = createTestDuck();
        duck.setId(null); // Clear ID before save
        when(mockIdGenerator.nextId()).thenReturn(1L);
        when(mockRepository.save(any(Duck.class))).thenReturn(duck);
        
        // Act
        Duck result = service.save(duck);
        
        // Assert
        assertNotNull(result);
        verify(mockValidator).validate(duck);
        verify(mockRepository).save(duck);
    }

    @Test
    void testGetDuck() {
        // Arrange
        Duck duck = createTestDuck();
        when(mockRepository.findOne(1L)).thenReturn(duck);
        
        // Act
        Duck result = service.findOne(1L);
        
        // Assert
        assertNotNull(result);
        assertEquals(duck, result);
        verify(mockRepository).findOne(1L);
    }

    @Test
    void testDeleteDuckWithoutFlock() {
        // Arrange
        Duck duck = createTestDuck();
        when(mockFlockService.getFlockByDuckId(1L)).thenReturn(null);
        when(mockRepository.delete(1L)).thenReturn(duck);
        
        // Act
        Duck result = service.delete(1L);
        
        // Assert
        assertNotNull(result);
        assertEquals(duck, result);
        verify(mockFlockService).getFlockByDuckId(1L);
        verify(mockRepository).delete(1L);
    }

    @Test
    void testDeleteDuckWithFlock() {
        // Arrange
        Duck duck = createTestDuck();
        Flock<Duck> flock = new Flock<>("TestFlock");
        flock.setId(10L);
        
        doReturn(flock).when(mockFlockService).getFlockByDuckId(1L);
        when(mockRepository.delete(1L)).thenReturn(duck);
        
        // Act
        Duck result = service.delete(1L);
        
        // Assert
        assertNotNull(result);
        verify(mockFlockService).getFlockByDuckId(1L);
        verify(mockFlockService).removeDuckFromFlock(10L, 1L);
        verify(mockRepository).delete(1L);
    }

    @Test
    void testGetAllDucks() {
        // Arrange
        Duck duck1 = createTestDuck();
        Duck duck2 = createTestDuck();
        duck2.setId(2L);
        when(mockRepository.findAll()).thenReturn(Arrays.asList(duck1, duck2));
        
        // Act
        Iterable<Duck> result = service.findAll();
        
        // Assert
        assertNotNull(result);
        verify(mockRepository).findAll();
    }

    @Test
    void testUpdateDuck() {
        // Arrange
        Duck duck = createTestDuck();
        when(mockRepository.update(duck)).thenReturn(null);
        
        // Act
        Duck result = service.update(duck);
        
        // Assert
        assertNull(result);
        verify(mockRepository).update(duck);
    }

    private Duck createTestDuck() {
        Duck duck = new FlyingDuck("ducky", "pass123", "duck@example.com", 
                                    org.utils.enums.DuckTypes.FLYING, 100.0, 200.0);
        duck.setId(1L);
        return duck;
    }
}
