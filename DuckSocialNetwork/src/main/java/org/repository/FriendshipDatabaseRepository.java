package org.repository;

import org.database.DatabaseConnection;
import org.domain.exceptions.RepositoryException;
import org.domain.users.User;
import org.domain.users.relationships.Friendship;
import org.domain.validators.Validator;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Database repository for Friendship entities
 */
public class FriendshipDatabaseRepository extends EntityRepository<Long, Friendship> {
    private final DuckDatabaseRepository duckRepository;
    private final PersonDatabaseRepository personRepository;
    protected Map<Long, User> users;
    
    public FriendshipDatabaseRepository(Validator<Friendship> validator, 
                                       DuckDatabaseRepository duckRepository,
                                       PersonDatabaseRepository personRepository) {
        super(validator);
        this.duckRepository = duckRepository;
        this.personRepository = personRepository;
        initUsers();
        loadData();
    }
    
    protected void initUsers() {
        users = new HashMap<>();
        duckRepository.findAll().forEach(d -> users.put(d.getId(), d));
        personRepository.findAll().forEach(u -> users.put(u.getId(), u));
    }
    
    private void loadData() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM friendships");
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Friendship friendship = extractFriendshipFromResultSet(rs);
                super.save(friendship);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error loading friendships from database", e);
        }
    }
    
    private Friendship extractFriendshipFromResultSet(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        Long user1Id = rs.getLong("user1_id");
        Long user2Id = rs.getLong("user2_id");
        
        User user1 = users.get(user1Id);
        User user2 = users.get(user2Id);
        
        if (user1 == null || user2 == null) {
            throw new IllegalArgumentException("User IDs do not exist in the system");
        }
        
        Friendship friendship = new Friendship(user1, user2);
        friendship.setId(id);
        return friendship;
    }
    
    @Override
    public Friendship save(Friendship entity) {
        Friendship result = super.save(entity);
        if (result == null) {
            saveToDatabase(entity);
        }
        return result;
    }
    
    private void saveToDatabase(Friendship friendship) {
        String sql = "INSERT INTO friendships (id, user1_id, user2_id) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, friendship.getId());
            stmt.setLong(2, friendship.getUser1().getId());
            stmt.setLong(3, friendship.getUser2().getId());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Error saving friendship to database", e);
        }
    }
    
    @Override
    public Friendship delete(Long id) {
        Friendship removed = super.delete(id);
        if (removed != null) {
            deleteFromDatabase(id);
        }
        return removed;
    }
    
    private void deleteFromDatabase(Long id) {
        String sql = "DELETE FROM friendships WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Error deleting friendship from database", e);
        }
    }
    
    @Override
    public Friendship update(Friendship entity) {
        Friendship result = super.update(entity);
        if (result == null) {
            updateInDatabase(entity);
        }
        return result;
    }
    
    private void updateInDatabase(Friendship friendship) {
        String sql = "UPDATE friendships SET user1_id = ?, user2_id = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, friendship.getUser1().getId());
            stmt.setLong(2, friendship.getUser2().getId());
            stmt.setLong(3, friendship.getId());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Error updating friendship in database", e);
        }
    }
}
