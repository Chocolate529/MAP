package org.example;

import org.domain.validators.DuckValidator;
import org.domain.validators.FlockValidator;
import org.domain.validators.FriendshipValidator;
import org.domain.validators.PersonValidator;
import org.repository.*;
import org.service.*;
import org.service.utils.LongIdGenerator;
import org.ui.ConsoleUserInterface;
import org.utils.Constants;

import java.util.Objects;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() {
        //validators
        var personValidator = new PersonValidator(Constants.EMAIL_REGEX);
        var duckValidator = new DuckValidator(Constants.EMAIL_REGEX);
        var friendshipValidator = new FriendshipValidator();
        var flockValidator = new FlockValidator();

        //repos
        DuckFileRepository duckRepo = null;
        PersonFileRepository personRepo = null;
        FriendshipFileRepository friendshipRepo = null;
        FlockFileRepository flockRepo = null;
        try {
            personRepo = new PersonFileRepository(Constants.PERSON_INPUT_FILE, personValidator);
            duckRepo = new DuckFileRepository(Constants.DUCK_INPUT_FILE, duckValidator);

            friendshipRepo = new FriendshipFileRepository(Constants.FRIENDSHIPS_INPUT_FILE, friendshipValidator,
                    duckRepo, personRepo);

            flockRepo = new FlockFileRepository(Constants.FLOCKS_INPUT_FILE, flockValidator, duckRepo);
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

        //id generator
        var usersIdGenerator = new LongIdGenerator(Objects.requireNonNullElse(maxUsersId, 0L) + 1);
        var friendshipIdGenerator = new LongIdGenerator(Objects.requireNonNullElse(maxFriendshipId, 0L) + 1);
        var flockIdGenerator = new LongIdGenerator(Objects.requireNonNullElse(maxFlockId, 0L) + 1);


        //service
        var duckService = new DucksService(duckValidator, duckRepo, usersIdGenerator);
        var personService = new PersonsService(personValidator, personRepo, usersIdGenerator);
        var friendshipService = new FriendshipService(friendshipValidator, friendshipRepo, friendshipIdGenerator);
        var flockService = new FlockService(flockValidator, flockRepo, flockIdGenerator, duckService);

        var usersService = new UsersService(duckService, personService,friendshipService);


        friendshipService.setUsersService(usersService);
        duckService.setFlockService(flockService);

        //ui
        var app = new ConsoleUserInterface(usersService, friendshipService,flockService);
        app.run();
    }
}

