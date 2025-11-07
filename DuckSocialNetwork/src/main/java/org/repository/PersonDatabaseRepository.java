package org.repository;

import org.database.DatabaseConnection;
import org.domain.dtos.PersonData;
import org.domain.exceptions.RepositoryException;
import org.domain.users.person.Person;
import org.domain.users.person.PersonFactory;
import org.domain.validators.Validator;
import org.utils.enums.PersonTypes;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Database repository for Person entities
 */
public class PersonDatabaseRepository extends EntityRepository<Long, Person> {
    private final PersonFactory personFactory = new PersonFactory();
    
    public PersonDatabaseRepository(Validator<Person> validator) {
        super(validator);
        loadData();
    }
    
    private void loadData() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM persons");
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Person person = extractPersonFromResultSet(rs);
                super.save(person);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error loading persons from database", e);
        }
    }
    
    private Person extractPersonFromResultSet(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String username = rs.getString("username");
        String password = rs.getString("password");
        String email = rs.getString("email");
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        String occupation = rs.getString("occupation");
        Date dateOfBirth = rs.getDate("date_of_birth");
        Double empathyLevel = rs.getDouble("empathy_level");
        
        List<String> attr = List.of(
            username, password, email, firstName, lastName, 
            occupation, dateOfBirth.toLocalDate().toString(), String.valueOf(empathyLevel)
        );
        
        Person person = personFactory.create(PersonTypes.DEFAULT, new PersonData(attr));
        person.setId(id);
        return person;
    }
    
    @Override
    public Person save(Person entity) {
        Person result = super.save(entity);
        if (result == null) {
            saveToDatabase(entity);
        }
        return result;
    }
    
    private void saveToDatabase(Person person) {
        String sql = """
            INSERT INTO persons (id, username, password, email, first_name, last_name, 
                                occupation, date_of_birth, empathy_level)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, person.getId());
            stmt.setString(2, person.getUsername());
            stmt.setString(3, person.getPassword());
            stmt.setString(4, person.getEmail());
            stmt.setString(5, person.getFirstName());
            stmt.setString(6, person.getLastName());
            stmt.setString(7, person.getOccupation());
            stmt.setDate(8, Date.valueOf(person.getDateOfBirth()));
            stmt.setDouble(9, person.getEmpathyLevel());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Error saving person to database", e);
        }
    }
    
    @Override
    public Person delete(Long id) {
        Person removed = super.delete(id);
        if (removed != null) {
            deleteFromDatabase(id);
        }
        return removed;
    }
    
    private void deleteFromDatabase(Long id) {
        String sql = "DELETE FROM persons WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Error deleting person from database", e);
        }
    }
    
    @Override
    public Person update(Person entity) {
        Person result = super.update(entity);
        if (result == null) {
            updateInDatabase(entity);
        }
        return result;
    }
    
    private void updateInDatabase(Person person) {
        String sql = """
            UPDATE persons 
            SET username = ?, password = ?, email = ?, first_name = ?, last_name = ?,
                occupation = ?, date_of_birth = ?, empathy_level = ?
            WHERE id = ?
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, person.getUsername());
            stmt.setString(2, person.getPassword());
            stmt.setString(3, person.getEmail());
            stmt.setString(4, person.getFirstName());
            stmt.setString(5, person.getLastName());
            stmt.setString(6, person.getOccupation());
            stmt.setDate(7, Date.valueOf(person.getDateOfBirth()));
            stmt.setDouble(8, person.getEmpathyLevel());
            stmt.setLong(9, person.getId());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Error updating person in database", e);
        }
    }
}
