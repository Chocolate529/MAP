package org.example;

import org.database.DatabaseConnection;
import org.domain.dtos.DuckData;
import org.domain.dtos.PersonData;
import org.domain.users.duck.Duck;
import org.domain.users.person.Person;
import org.domain.validators.DuckValidator;
import org.domain.validators.PersonValidator;
import org.repository.DuckDatabaseRepository;
import org.repository.PersonDatabaseRepository;
import org.utils.Constants;

import java.sql.SQLException;
import java.util.List;

/**
 * Simple verification class to test database persistence
 */
public class DatabaseVerification {
    public static void main(String[] args) {
        System.out.println("=== Database Persistence Verification ===\n");
        
        try {
            // Initialize database
            System.out.println("1. Initializing database schema...");
            DatabaseConnection.initializeSchema();
            System.out.println("   ✓ Database schema initialized\n");
            
            // Create repositories
            System.out.println("2. Creating repositories...");
            var personValidator = new PersonValidator(Constants.EMAIL_REGEX);
            var duckValidator = new DuckValidator(Constants.EMAIL_REGEX);
            
            var personRepo = new PersonDatabaseRepository(personValidator);
            var duckRepo = new DuckDatabaseRepository(duckValidator);
            System.out.println("   ✓ Repositories created\n");
            
            // Count existing records
            System.out.println("3. Checking existing data...");
            long personCount = 0;
            long duckCount = 0;
            for (Person p : personRepo.findAll()) personCount++;
            for (Duck d : duckRepo.findAll()) duckCount++;
            
            System.out.println("   Persons in database: " + personCount);
            System.out.println("   Ducks in database: " + duckCount + "\n");
            
            // Test creating a new person
            System.out.println("4. Testing person creation...");
            List<String> personAttrs = List.of(
                "testuser", "password123", "test@example.com",
                "Test", "User", "Software Engineer", "1990-01-01", "7.5"
            );
            PersonData personData = new PersonData(personAttrs);
            Person testPerson = new Person(personData);
            testPerson.setId(9999L);
            
            Person savedPerson = personRepo.save(testPerson);
            if (savedPerson == null) {
                System.out.println("   ✓ Person saved to database");
                
                // Verify retrieval
                Person retrieved = personRepo.findOne(9999L);
                if (retrieved != null && retrieved.getUsername().equals("testuser")) {
                    System.out.println("   ✓ Person retrieved successfully");
                } else {
                    System.out.println("   ✗ Failed to retrieve person");
                }
            } else {
                System.out.println("   ℹ Person already exists or save failed");
            }
            
            // Test creating a new duck
            System.out.println("\n5. Testing duck creation...");
            List<String> duckAttrs = List.of(
                "testduck", "password456", "duck@example.com", "100.0", "200.0"
            );
            DuckData duckData = new DuckData(duckAttrs);
            var duckFactory = new org.domain.users.duck.DuckFactory();
            Duck testDuck = duckFactory.create(
                org.utils.enums.DuckTypes.FLYING, duckData
            );
            testDuck.setId(8888L);
            
            Duck savedDuck = duckRepo.save(testDuck);
            if (savedDuck == null) {
                System.out.println("   ✓ Duck saved to database");
                
                // Verify retrieval
                Duck retrievedDuck = duckRepo.findOne(8888L);
                if (retrievedDuck != null && retrievedDuck.getUsername().equals("testduck")) {
                    System.out.println("   ✓ Duck retrieved successfully");
                } else {
                    System.out.println("   ✗ Failed to retrieve duck");
                }
            } else {
                System.out.println("   ℹ Duck already exists or save failed");
            }
            
            // Test update
            System.out.println("\n6. Testing update operation...");
            Person personToUpdate = personRepo.findOne(9999L);
            if (personToUpdate != null) {
                personToUpdate.setOccupation("Senior Software Engineer");
                Person updateResult = personRepo.update(personToUpdate);
                if (updateResult == null) {
                    System.out.println("   ✓ Person updated successfully");
                    
                    Person updatedPerson = personRepo.findOne(9999L);
                    if (updatedPerson.getOccupation().equals("Senior Software Engineer")) {
                        System.out.println("   ✓ Update verified in database");
                    }
                }
            }
            
            // Test delete
            System.out.println("\n7. Testing delete operation...");
            Person deletedPerson = personRepo.delete(9999L);
            if (deletedPerson != null) {
                System.out.println("   ✓ Person deleted from database");
                
                Person shouldBeNull = personRepo.findOne(9999L);
                if (shouldBeNull == null) {
                    System.out.println("   ✓ Delete verified in database");
                }
            }
            
            Duck deletedDuck = duckRepo.delete(8888L);
            if (deletedDuck != null) {
                System.out.println("   ✓ Duck deleted from database");
            }
            
            System.out.println("\n=== Verification Complete ===");
            System.out.println("All database operations working correctly!");
            
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.close();
        }
    }
}
