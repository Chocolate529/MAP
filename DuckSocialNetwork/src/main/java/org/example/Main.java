package org.example;

import org.database.DatabaseConnection;
import org.domain.validators.*;
import org.repository.*;
import org.service.*;
import org.service.utils.LongIdGenerator;
import org.ui.ConsoleUserInterface;
import org.utils.Constants;

import java.sql.SQLException;
import java.util.Objects;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    
    private static final boolean USE_DATABASE = true; // Set to false to use file storage
    
    public static void main(String[] args) {
        if (USE_DATABASE) {
            runWithDatabase();
        } else {
            runWithFiles();
        }
    }
    
    private static void runWithDatabase() {
        System.out.println("Starting DuckSocialNetwork with Database Persistence...");
        
        try {
            // Initialize database schema
            DatabaseConnection.initializeSchema();
            System.out.println("Database schema initialized successfully.");
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            return;
        }
        
        //validators
        var personValidator = new PersonValidator(Constants.EMAIL_REGEX);
        var duckValidator = new DuckValidator(Constants.EMAIL_REGEX);
        var friendshipValidator = new FriendshipValidator();
        var flockValidator = new FlockValidator();
        var raceEventValidator = new RaceEventValidator();
        
        //repos
        PersonDatabaseRepository personRepo = null;
        DuckDatabaseRepository duckRepo = null;
        FriendshipDatabaseRepository friendshipRepo = null;
        FlockDatabaseRepository flockRepo = null;
        RaceEventDatabaseRepository raceEventRepo = null;
        
        try {
            personRepo = new PersonDatabaseRepository(personValidator);
            duckRepo = new DuckDatabaseRepository(duckValidator);
            friendshipRepo = new FriendshipDatabaseRepository(friendshipValidator, duckRepo, personRepo);
            flockRepo = new FlockDatabaseRepository(flockValidator, duckRepo);
            raceEventRepo = new RaceEventDatabaseRepository(raceEventValidator, duckRepo);
        } catch (Exception e) {
            System.out.println("Error loading repositories: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        
        Long maxUsersId = Math.max(
                EntityFileRepository.getMaxId(duckRepo.findAll()),
                EntityFileRepository.getMaxId(personRepo.findAll())
        );
        
        Long maxFriendshipId = EntityFileRepository.getMaxId(friendshipRepo.findAll());
        Long maxFlockId = EntityFileRepository.getMaxId(flockRepo.findAll());
        Long maxEventId = EntityFileRepository.getMaxId(raceEventRepo.findAll());
        
        //id generator
        var usersIdGenerator = new LongIdGenerator(Objects.requireNonNullElse(maxUsersId, 0L) + 1);
        var friendshipIdGenerator = new LongIdGenerator(Objects.requireNonNullElse(maxFriendshipId, 0L) + 1);
        var flockIdGenerator = new LongIdGenerator(Objects.requireNonNullElse(maxFlockId, 0L) + 1);
        var eventIdGenerator = new LongIdGenerator(Objects.requireNonNullElse(maxEventId, 0L) + 1);
        
        //service
        var duckService = new DucksService(duckValidator, duckRepo, usersIdGenerator);
        var personService = new PersonsService(personValidator, personRepo, usersIdGenerator);
        var friendshipService = new FriendshipService(friendshipValidator, friendshipRepo, friendshipIdGenerator);
        var flockService = new FlockService(flockValidator, flockRepo, flockIdGenerator, duckService);
        var raceEventService = new RaceEventService(raceEventValidator, raceEventRepo, eventIdGenerator, duckService);
        
        var usersService = new UsersService(duckService, personService, friendshipService);
        
        friendshipService.setUsersService(usersService);
        duckService.setFlockService(flockService);
        
        //ui
        var app = new ConsoleUserInterface(usersService, friendshipService, flockService, raceEventService);
        app.run();
        
        // Close database connection on exit
        DatabaseConnection.close();
    }
    
    private static void runWithFiles() {
        System.out.println("Starting DuckSocialNetwork with File Persistence...");
        
        //validators
        var personValidator = new PersonValidator(Constants.EMAIL_REGEX);
        var duckValidator = new DuckValidator(Constants.EMAIL_REGEX);
        var friendshipValidator = new FriendshipValidator();
        var flockValidator = new FlockValidator();
        var raceEventValidator = new RaceEventValidator();

        //repos
        DuckFileRepository duckRepo = null;
        PersonFileRepository personRepo = null;
        FriendshipFileRepository friendshipRepo = null;
        FlockFileRepository flockRepo = null;
        RaceEventFileRepository raceEventRepo = null;
        try {
            personRepo = new PersonFileRepository(Constants.PERSON_INPUT_FILE, personValidator);
            duckRepo = new DuckFileRepository(Constants.DUCK_INPUT_FILE, duckValidator);

            friendshipRepo = new FriendshipFileRepository(Constants.FRIENDSHIPS_INPUT_FILE, friendshipValidator,
                    duckRepo, personRepo);

            flockRepo = new FlockFileRepository(Constants.FLOCKS_INPUT_FILE, flockValidator, duckRepo);
            raceEventRepo = new RaceEventFileRepository(Constants.RACE_EVENTS_INPUT_FILE, raceEventValidator, duckRepo);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        Long maxUsersId = Math.max(
                EntityFileRepository.getMaxId(duckRepo.findAll()),
                EntityFileRepository.getMaxId(personRepo.findAll())
        );

        Long maxFriendshipId = EntityFileRepository.getMaxId(friendshipRepo.findAll());
        Long maxFlockId = EntityFileRepository.getMaxId(flockRepo.findAll());
        Long  maxEventId = EntityFileRepository.getMaxId(raceEventRepo.findAll());

        //id generator
        var usersIdGenerator = new LongIdGenerator(Objects.requireNonNullElse(maxUsersId, 0L) + 1);
        var friendshipIdGenerator = new LongIdGenerator(Objects.requireNonNullElse(maxFriendshipId, 0L) + 1);
        var flockIdGenerator = new LongIdGenerator(Objects.requireNonNullElse(maxFlockId, 0L) + 1);
        var eventIdGenerator = new LongIdGenerator(Objects.requireNonNullElse(maxEventId, 0L) + 1);

        //service
        var duckService = new DucksService(duckValidator, duckRepo, usersIdGenerator);
        var personService = new PersonsService(personValidator, personRepo, usersIdGenerator);
        var friendshipService = new FriendshipService(friendshipValidator, friendshipRepo, friendshipIdGenerator);
        var flockService = new FlockService(flockValidator, flockRepo, flockIdGenerator, duckService);
        var raceEventService = new RaceEventService(raceEventValidator, raceEventRepo, eventIdGenerator, duckService);

        var usersService = new UsersService(duckService, personService,friendshipService);


        friendshipService.setUsersService(usersService);
        duckService.setFlockService(flockService);

        //ui
        var app = new ConsoleUserInterface(usersService, friendshipService, flockService, raceEventService);
        app.run();
    }
}
