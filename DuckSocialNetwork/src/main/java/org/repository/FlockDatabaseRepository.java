package org.repository;

import org.database.DatabaseConnection;
import org.domain.exceptions.RepositoryException;
import org.domain.users.duck.Duck;
import org.domain.users.duck.flock.Flock;
import org.domain.validators.Validator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Database repository for Flock entities
 */
public class FlockDatabaseRepository extends EntityRepository<Long, Flock<Duck>> {
    private final DuckDatabaseRepository duckRepository;
    
    public FlockDatabaseRepository(Validator<Flock<Duck>> validator, DuckDatabaseRepository duckRepository) {
        super(validator);
        this.duckRepository = duckRepository;
        loadData();
    }
    
    private void loadData() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM flocks");
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Flock<Duck> flock = extractFlockFromResultSet(rs);
                super.save(flock);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error loading flocks from database", e);
        }
    }
    
    private Flock<Duck> extractFlockFromResultSet(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String name = rs.getString("flock_name");
        
        Flock<Duck> flock = new Flock<>(name);
        flock.setId(id);
        
        // Load members
        List<Duck> members = loadFlockMembers(id);
        members.forEach(flock::addMember);
        
        return flock;
    }
    
    private List<Duck> loadFlockMembers(Long flockId) {
        List<Duck> members = new ArrayList<>();
        String sql = "SELECT duck_id FROM flock_members WHERE flock_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, flockId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Long duckId = rs.getLong("duck_id");
                Duck duck = duckRepository.findOne(duckId);
                if (duck != null) {
                    members.add(duck);
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error loading flock members from database", e);
        }
        
        return members;
    }
    
    @Override
    public Flock<Duck> save(Flock<Duck> entity) {
        Flock<Duck> result = super.save(entity);
        if (result == null) {
            saveToDatabase(entity);
        }
        return result;
    }
    
    private void saveToDatabase(Flock<Duck> flock) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Insert flock
            String sql = "INSERT INTO flocks (id, flock_name) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, flock.getId());
                stmt.setString(2, flock.getFlockName());
                stmt.executeUpdate();
            }
            
            // Insert members
            saveFlockMembers(conn, flock);
            
        } catch (SQLException e) {
            throw new RepositoryException("Error saving flock to database", e);
        }
    }
    
    private void saveFlockMembers(Connection conn, Flock<Duck> flock) throws SQLException {
        String sql = "INSERT INTO flock_members (flock_id, duck_id) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (Duck member : flock.getMembers()) {
                stmt.setLong(1, flock.getId());
                stmt.setLong(2, member.getId());
                stmt.executeUpdate();
            }
        }
    }
    
    @Override
    public Flock<Duck> delete(Long id) {
        Flock<Duck> removed = super.delete(id);
        if (removed != null) {
            deleteFromDatabase(id);
        }
        return removed;
    }
    
    private void deleteFromDatabase(Long id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Delete members first
            String deleteMembersSql = "DELETE FROM flock_members WHERE flock_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteMembersSql)) {
                stmt.setLong(1, id);
                stmt.executeUpdate();
            }
            
            // Delete flock
            String deleteFlockSql = "DELETE FROM flocks WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteFlockSql)) {
                stmt.setLong(1, id);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error deleting flock from database", e);
        }
    }
    
    @Override
    public Flock<Duck> update(Flock<Duck> entity) {
        Flock<Duck> result = super.update(entity);
        if (result == null) {
            updateInDatabase(entity);
        }
        return result;
    }
    
    private void updateInDatabase(Flock<Duck> flock) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Update flock
            String sql = "UPDATE flocks SET flock_name = ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, flock.getFlockName());
                stmt.setLong(2, flock.getId());
                stmt.executeUpdate();
            }
            
            // Delete old members
            String deleteSql = "DELETE FROM flock_members WHERE flock_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteSql)) {
                stmt.setLong(1, flock.getId());
                stmt.executeUpdate();
            }
            
            // Insert new members
            saveFlockMembers(conn, flock);
            
        } catch (SQLException e) {
            throw new RepositoryException("Error updating flock in database", e);
        }
    }
}
