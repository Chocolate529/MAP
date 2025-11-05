package org.repository;

import org.database.DatabaseConnection;
import org.domain.dtos.DuckData;
import org.domain.exceptions.RepositoryException;
import org.domain.users.duck.Duck;
import org.domain.users.duck.DuckFactory;
import org.domain.validators.Validator;
import org.utils.enums.DuckTypes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Database repository for Duck entities
 */
public class DuckDatabaseRepository extends EntityRepository<Long, Duck> {
    private final DuckFactory duckFactory;
    
    public DuckDatabaseRepository(Validator<Duck> validator) {
        super(validator);
        this.duckFactory = new DuckFactory();
        loadData();
    }
    
    private void loadData() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM ducks");
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Duck duck = extractDuckFromResultSet(rs);
                super.save(duck);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error loading ducks from database", e);
        }
    }
    
    private Duck extractDuckFromResultSet(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String username = rs.getString("username");
        String password = rs.getString("password");
        String email = rs.getString("email");
        DuckTypes type = DuckTypes.valueOf(rs.getString("duck_type"));
        Double speed = rs.getDouble("speed");
        Double rezistance = rs.getDouble("rezistance");
        
        List<String> dataAttributes = List.of(
            username, password, email,
            String.valueOf(speed), String.valueOf(rezistance)
        );
        
        DuckData data = new DuckData(dataAttributes);
        Duck duck = duckFactory.create(type, data);
        duck.setId(id);
        return duck;
    }
    
    @Override
    public Duck save(Duck entity) {
        Duck result = super.save(entity);
        if (result == null) {
            saveToDatabase(entity);
        }
        return result;
    }
    
    private void saveToDatabase(Duck duck) {
        String sql = """
            INSERT INTO ducks (id, username, password, email, duck_type, speed, rezistance)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, duck.getId());
            stmt.setString(2, duck.getUsername());
            stmt.setString(3, duck.getPassword());
            stmt.setString(4, duck.getEmail());
            stmt.setString(5, duck.getDuckType().name());
            stmt.setDouble(6, duck.getSpeed());
            stmt.setDouble(7, duck.getRezistance());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Error saving duck to database", e);
        }
    }
    
    @Override
    public Duck delete(Long id) {
        Duck removed = super.delete(id);
        if (removed != null) {
            deleteFromDatabase(id);
        }
        return removed;
    }
    
    private void deleteFromDatabase(Long id) {
        String sql = "DELETE FROM ducks WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Error deleting duck from database", e);
        }
    }
    
    @Override
    public Duck update(Duck entity) {
        Duck result = super.update(entity);
        if (result == null) {
            updateInDatabase(entity);
        }
        return result;
    }
    
    private void updateInDatabase(Duck duck) {
        String sql = """
            UPDATE ducks 
            SET username = ?, password = ?, email = ?, duck_type = ?, speed = ?, rezistance = ?
            WHERE id = ?
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, duck.getUsername());
            stmt.setString(2, duck.getPassword());
            stmt.setString(3, duck.getEmail());
            stmt.setString(4, duck.getDuckType().name());
            stmt.setDouble(5, duck.getSpeed());
            stmt.setDouble(6, duck.getRezistance());
            stmt.setLong(7, duck.getId());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Error updating duck in database", e);
        }
    }
}
