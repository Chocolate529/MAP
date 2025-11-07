package org.example;

import org.database.DatabaseConnection;
import org.domain.users.duck.Duck;
import org.domain.users.duck.flock.Flock;
import org.domain.users.person.Person;
import org.domain.users.relationships.Friendship;
import org.domain.events.RaceEvent;
import org.domain.validators.*;
import org.repository.*;
import org.utils.Constants;

import java.sql.SQLException;

/**
 * Utility to migrate data from files to database
 */
public class MigrateFileToDatabase {
    public static void main(String[] args) {
        System.out.println("=== File to Database Migration ===\n");
        
        try {
            // Initialize database
            System.out.println("Step 1: Initializing database schema...");
            DatabaseConnection.initializeSchema();
            System.out.println("   ✓ Database schema initialized\n");
            
            // Validators
            var personValidator = new PersonValidator(Constants.EMAIL_REGEX);
            var duckValidator = new DuckValidator(Constants.EMAIL_REGEX);
            var friendshipValidator = new FriendshipValidator();
            var flockValidator = new FlockValidator();
            var raceEventValidator = new RaceEventValidator();
            
            // Load from files
            System.out.println("Step 2: Loading data from files...");
            PersonFileRepository personFileRepo = new PersonFileRepository(Constants.PERSON_INPUT_FILE, personValidator);
            DuckFileRepository duckFileRepo = new DuckFileRepository(Constants.DUCK_INPUT_FILE, duckValidator);
            FriendshipFileRepository friendshipFileRepo = new FriendshipFileRepository(Constants.FRIENDSHIPS_INPUT_FILE, friendshipValidator, duckFileRepo, personFileRepo);
            FlockFileRepository flockFileRepo = new FlockFileRepository(Constants.FLOCKS_INPUT_FILE, flockValidator, duckFileRepo);
            RaceEventFileRepository raceEventFileRepo = new RaceEventFileRepository(Constants.RACE_EVENTS_INPUT_FILE, raceEventValidator, duckFileRepo);
            System.out.println("   ✓ Data loaded from files\n");
            
            // Create database repositories (empty)
            System.out.println("Step 3: Creating database repositories...");
            PersonDatabaseRepository personDbRepo = new PersonDatabaseRepository(personValidator);
            DuckDatabaseRepository duckDbRepo = new DuckDatabaseRepository(duckValidator);
            
            // Clear existing data from database
            System.out.println("Step 4: Clearing existing database data...");
            for (Person p : personDbRepo.findAll()) {
                personDbRepo.delete(p.getId());
            }
            for (Duck d : duckDbRepo.findAll()) {
                duckDbRepo.delete(d.getId());
            }
            System.out.println("   ✓ Database cleared\n");
            
            // Migrate Persons
            System.out.println("Step 5: Migrating persons...");
            int personCount = 0;
            for (Person person : personFileRepo.findAll()) {
                personDbRepo.save(person);
                personCount++;
            }
            System.out.println("   ✓ Migrated " + personCount + " persons\n");
            
            // Migrate Ducks
            System.out.println("Step 6: Migrating ducks...");
            int duckCount = 0;
            for (Duck duck : duckFileRepo.findAll()) {
                duckDbRepo.save(duck);
                duckCount++;
            }
            System.out.println("   ✓ Migrated " + duckCount + " ducks\n");
            
            // Recreate database repositories with user data
            FriendshipDatabaseRepository friendshipDbRepo = new FriendshipDatabaseRepository(friendshipValidator, duckDbRepo, personDbRepo);
            FlockDatabaseRepository flockDbRepo = new FlockDatabaseRepository(flockValidator, duckDbRepo);
            RaceEventDatabaseRepository raceEventDbRepo = new RaceEventDatabaseRepository(raceEventValidator, duckDbRepo);
            
            // Clear junction tables
            for (Friendship f : friendshipDbRepo.findAll()) {
                friendshipDbRepo.delete(f.getId());
            }
            for (Flock<Duck> fl : flockDbRepo.findAll()) {
                flockDbRepo.delete(fl.getId());
            }
            for (RaceEvent re : raceEventDbRepo.findAll()) {
                raceEventDbRepo.delete(re.getId());
            }
            
            // Migrate Friendships
            System.out.println("Step 7: Migrating friendships...");
            int friendshipCount = 0;
            for (Friendship friendship : friendshipFileRepo.findAll()) {
                // Recreate friendship with database users
                var user1 = friendship.getUser1() instanceof Person ? 
                    personDbRepo.findOne(friendship.getUser1().getId()) : 
                    duckDbRepo.findOne(friendship.getUser1().getId());
                var user2 = friendship.getUser2() instanceof Person ? 
                    personDbRepo.findOne(friendship.getUser2().getId()) : 
                    duckDbRepo.findOne(friendship.getUser2().getId());
                
                if (user1 != null && user2 != null) {
                    Friendship newFriendship = new Friendship(user1, user2);
                    newFriendship.setId(friendship.getId());
                    friendshipDbRepo.save(newFriendship);
                    friendshipCount++;
                }
            }
            System.out.println("   ✓ Migrated " + friendshipCount + " friendships\n");
            
            // Migrate Flocks
            System.out.println("Step 8: Migrating flocks...");
            int flockCount = 0;
            for (Flock<Duck> flock : flockFileRepo.findAll()) {
                Flock<Duck> newFlock = new Flock<>(flock.getFlockName());
                newFlock.setId(flock.getId());
                
                // Add members from database
                for (Duck member : flock.getMembers()) {
                    Duck dbMember = duckDbRepo.findOne(member.getId());
                    if (dbMember != null) {
                        newFlock.addMember(dbMember);
                    }
                }
                
                flockDbRepo.save(newFlock);
                flockCount++;
            }
            System.out.println("   ✓ Migrated " + flockCount + " flocks\n");
            
            // Migrate Race Events
            System.out.println("Step 9: Migrating race events...");
            int eventCount = 0;
            for (RaceEvent event : raceEventFileRepo.findAll()) {
                RaceEvent newEvent = new RaceEvent(event.getName());
                newEvent.setId(event.getId());
                newEvent.setMaxTime(event.getMaxTime());
                newEvent.setDistances(event.getDistances());
                
                // Add participants from database
                event.getSubscribers().forEach(participant -> {
                    Duck dbDuck = duckDbRepo.findOne(participant.getId());
                    if (dbDuck != null && dbDuck instanceof org.domain.users.duck.SwimmingDuck) {
                        newEvent.addObserver((org.domain.users.duck.SwimmingDuck) dbDuck);
                    }
                });
                
                raceEventDbRepo.save(newEvent);
                eventCount++;
            }
            System.out.println("   ✓ Migrated " + eventCount + " race events\n");
            
            System.out.println("=== Migration Complete ===");
            System.out.println("Summary:");
            System.out.println("  - Persons: " + personCount);
            System.out.println("  - Ducks: " + duckCount);
            System.out.println("  - Friendships: " + friendshipCount);
            System.out.println("  - Flocks: " + flockCount);
            System.out.println("  - Race Events: " + eventCount);
            System.out.println("\nData has been successfully migrated to the database!");
            System.out.println("You can now set USE_DATABASE = true in Main.java");
            
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Migration error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.close();
        }
    }
}
