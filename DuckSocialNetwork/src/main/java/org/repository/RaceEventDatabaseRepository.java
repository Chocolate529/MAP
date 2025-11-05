package org.repository;

import org.database.DatabaseConnection;
import org.domain.events.RaceEvent;
import org.domain.exceptions.RepositoryException;
import org.domain.users.duck.Duck;
import org.domain.users.duck.SwimmingDuck;
import org.domain.validators.Validator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Database repository for RaceEvent entities
 */
public class RaceEventDatabaseRepository extends EntityRepository<Long, RaceEvent> {
    private final DuckDatabaseRepository duckRepository;
    
    public RaceEventDatabaseRepository(Validator<RaceEvent> validator, DuckDatabaseRepository duckRepository) {
        super(validator);
        this.duckRepository = duckRepository;
        loadData();
    }
    
    private void loadData() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM race_events");
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                RaceEvent event = extractRaceEventFromResultSet(rs);
                super.save(event);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error loading race events from database", e);
        }
    }
    
    private RaceEvent extractRaceEventFromResultSet(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String name = rs.getString("name");
        Double maxTime = rs.getDouble("max_time");
        
        // Load participants
        List<SwimmingDuck> participants = loadEventParticipants(id);
        
        RaceEvent event = new RaceEvent(participants, name);
        event.setId(id);
        event.setMaxTime(maxTime);
        
        return event;
    }
    
    private List<SwimmingDuck> loadEventParticipants(Long eventId) {
        List<SwimmingDuck> participants = new ArrayList<>();
        String sql = "SELECT duck_id FROM race_event_participants WHERE event_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, eventId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Long duckId = rs.getLong("duck_id");
                Duck duck = duckRepository.findOne(duckId);
                if (duck instanceof SwimmingDuck) {
                    participants.add((SwimmingDuck) duck);
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error loading event participants from database", e);
        }
        
        return participants;
    }
    
    @Override
    public RaceEvent save(RaceEvent entity) {
        RaceEvent result = super.save(entity);
        if (result == null) {
            saveToDatabase(entity);
        }
        return result;
    }
    
    private void saveToDatabase(RaceEvent event) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Insert event
            String sql = "INSERT INTO race_events (id, name, max_time) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, event.getId());
                stmt.setString(2, event.getName());
                stmt.setDouble(3, event.getMaxTime());
                stmt.executeUpdate();
            }
            
            // Insert participants
            saveEventParticipants(conn, event);
            
        } catch (SQLException e) {
            throw new RepositoryException("Error saving race event to database", e);
        }
    }
    
    private void saveEventParticipants(Connection conn, RaceEvent event) throws SQLException {
        String sql = "INSERT INTO race_event_participants (event_id, duck_id) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (SwimmingDuck participant : event.getSubscribers()) {
                stmt.setLong(1, event.getId());
                stmt.setLong(2, participant.getId());
                stmt.executeUpdate();
            }
        }
    }
    
    @Override
    public RaceEvent delete(Long id) {
        RaceEvent removed = super.delete(id);
        if (removed != null) {
            deleteFromDatabase(id);
        }
        return removed;
    }
    
    private void deleteFromDatabase(Long id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Delete participants first
            String deleteParticipantsSql = "DELETE FROM race_event_participants WHERE event_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteParticipantsSql)) {
                stmt.setLong(1, id);
                stmt.executeUpdate();
            }
            
            // Delete event
            String deleteEventSql = "DELETE FROM race_events WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteEventSql)) {
                stmt.setLong(1, id);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error deleting race event from database", e);
        }
    }
    
    @Override
    public RaceEvent update(RaceEvent entity) {
        RaceEvent result = super.update(entity);
        if (result == null) {
            updateInDatabase(entity);
        }
        return result;
    }
    
    private void updateInDatabase(RaceEvent event) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Update event
            String sql = "UPDATE race_events SET name = ?, max_time = ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, event.getName());
                stmt.setDouble(2, event.getMaxTime());
                stmt.setLong(3, event.getId());
                stmt.executeUpdate();
            }
            
            // Delete old participants
            String deleteSql = "DELETE FROM race_event_participants WHERE event_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteSql)) {
                stmt.setLong(1, event.getId());
                stmt.executeUpdate();
            }
            
            // Insert new participants
            saveEventParticipants(conn, event);
            
        } catch (SQLException e) {
            throw new RepositoryException("Error updating race event in database", e);
        }
    }
}
