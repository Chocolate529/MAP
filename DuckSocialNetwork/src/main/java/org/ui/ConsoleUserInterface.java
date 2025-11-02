package org.ui;

import org.domain.dtos.DuckData;
import org.domain.dtos.PersonData;
import org.domain.users.UserFactory;
import org.domain.users.duck.Duck;
import org.domain.users.duck.flock.Flock;
import org.domain.users.relationships.Friendship;
import org.domain.users.person.Person;
import org.domain.users.User;
import org.service.FlockService;
import org.service.FriendshipService;
import org.service.UsersService;
import org.utils.enums.DuckTypes;
import org.utils.enums.PersonTypes;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ConsoleUserInterface implements UserInterface {
    private final UsersService usersService;
    private final FriendshipService friendshipService;
    private final Scanner scanner = new Scanner(System.in);
    private final UserFactory userFactory;
    private final FlockService flockService;

    public ConsoleUserInterface(UsersService usersService, FriendshipService friendshipService, FlockService flockService) {
        this.usersService = usersService;
        this.friendshipService = friendshipService;
        this.flockService = flockService;
        this.userFactory = new UserFactory();
    }

    public void run() {
        while (true) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Manage Users");
            System.out.println("2. Manage Friendships");
            System.out.println("3. Manage Flocks");
            System.out.println("0. Exit");
            System.out.print("Choose: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> manageUsers();
                case 2 -> manageFriendships();
                case 3 -> manageFlocks();
                case 0 -> {
                    System.out.println("Bye ");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private void manageFlocks(){
        while (true) {
            System.out.println("\n=== FRIENDSHIP MENU ===");
            System.out.println("1. Add Flock");
            System.out.println("2. Remove Flock");
            System.out.println("3. View Ducks from a Flock");
            System.out.println("4. View flocks");
            System.out.println("0. Back");
            System.out.print("Choose: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> addFlockUI();
                case 2 -> removeFlockUI();
                case 3 -> viewDuckFromFlockUI();
                case 4 -> viewFlocksUI();
                case 5 -> addDuckToFlockUI();
                case 6 -> removeDuckFromFlockUI();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private void removeDuckFromFlockUI(){

    }

    private void addDuckToFlockUI(){

    }

    private void removeFlockUI(){
        try {
            System.out.println("Enter Flock ID: ");
            Long flockId = Long.parseLong(scanner.nextLine());

            var flock = flockService.delete(flockId);
            System.out.println("Flock has been deleted!" + flock);
        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void addFlockUI(){
        try{
            System.out.println("Enter flock name: ");
            String flockName = scanner.nextLine();

            flockService.save(new Flock<>(flockName));
            System.out.println("Flock has been added!" + flockName);
        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }

    }

    private void viewDuckFromFlockUI(){
        try{
            System.out.println("Enter flock ID: ");
            Long flockId = Long.parseLong(scanner.nextLine());

            var flock = flockService.findOne(flockId);
            if(flock == null){
                System.out.println("Flock with ID: " + flockId + " not found!");
            }else {
                var ducks = flock.getMembers();
                if(ducks.isEmpty()){
                    System.out.println("Flock has no members!");
                } else {
                    ducks.forEach(System.out::println);
                }
            }

        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewFlocksUI(){
        var flocks =  flockService.findAll();
        if(flocks == null){
            System.out.println("No Flocks found!");
        } else {
            flocks.forEach(System.out::println);
        }
    }

    private void manageFriendships() {
        while (true) {
            System.out.println("\n=== FRIENDSHIP MENU ===");
            System.out.println("1. Add Friendship");
            System.out.println("2. Remove Friendship");
            System.out.println("3. View Friends of a User");
            System.out.println("4. View all Friendships");
            System.out.println("5. Number of Communities");
            System.out.println("6. Most Sociable Community");
            System.out.println("0. Back");
            System.out.print("Choose: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> addFriendshipUI();
                case 2 -> removeFriendshipUI();
                case 3 -> viewFriendsUI();
                case 4 -> ViewAllFriendships();
                case 5 -> showNumberOfCommunities();
                case 6 -> showMostSociableCommunity();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private void addFriendshipUI() {
        try {
            // First user
            System.out.println("First user id: "); Long user1Id = Long.parseLong(scanner.nextLine());
            System.out.println("Second user id: "); Long user2Id = Long.parseLong(scanner.nextLine());

            User user1 = usersService.findOne(user1Id);
            User user2 = usersService.findOne(user2Id);

            Friendship friendship = new Friendship(user1, user2);

            friendshipService.save(friendship);
            System.out.println("Friendship added between " + user1.getUsername() + " and " + user2.getUsername());

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void removeFriendshipUI() {
        try {
            System.out.println("Enter friendship id: ");
            Long friendshipId = Long.parseLong(scanner.nextLine());

            var deletedFrienship = friendshipService.delete(friendshipId);
            System.out.println("Friendship removed between " + deletedFrienship.getUser1().getUsername() +
                    " and " + deletedFrienship.getUser2().getUsername());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewFriendsUI() {
        try {
            System.out.println("Enter user id: ");
            Long userId = Long.parseLong(scanner.nextLine());


            var friends = friendshipService.getFriendsOfUser(userId);
            if (friends.isEmpty()) {
                System.out.println("No friends found.");
            } else {
                int nrOfFriends = friends.size();
                System.out.println("Number of friends: " + nrOfFriends);
                friends.forEach(friendship -> {
                    System.out.println(friendship.getUsername());
                });
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void ViewAllFriendships(){
        try {
            var friendships = friendshipService.findAll();
            System.out.println("===Friendships===");
            friendships.forEach(System.out::println);
        } catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }

    }

    private void showNumberOfCommunities() {
        try {
            int count = friendshipService.countFriendCommunities();
            System.out.println("Number of communities: " + count);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void showMostSociableCommunity() {
        try {
            List<User> community = friendshipService.findMostSociableNetwork();
            if (community.isEmpty()) {
                System.out.println("No community found.");
            } else {
                System.out.println("Most sociable community (" + community.size() + " members):");
                for (User u : community) {
                    System.out.println("- " + u);
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void manageUsers() {
        while (true) {
            System.out.println("\n=== USER MENU ===");
            System.out.println("1. Add User");
            System.out.println("2. Remove User");
            System.out.println("3. View All Users");
            System.out.println("4. Find User by ID");
            System.out.println("5. Update User");
            System.out.println("0. Back");
            System.out.print("Choose: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> addUser();       // Add Person or Duck
                case 2 -> removeUser();    // Remove by type and ID
                case 3 -> viewAllUsers();  // Show all users
                case 4 -> findUser();      // Find by type and ID
                case 5 -> updateUser();    // Update by type and ID
                case 0 -> { return; }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private void addUser() {
        System.out.println("Add (1) Person or (2) Duck?");
        int type = Integer.parseInt(scanner.nextLine());

        try {
            System.out.print("Username: "); String username = scanner.nextLine();
            System.out.print("Password: "); String password = scanner.nextLine();
            System.out.print("Email: "); String email = scanner.nextLine();


            if (type == 1) { // Person
                System.out.print("First name: "); String firstName = scanner.nextLine();
                System.out.print("Last name: "); String lastName = scanner.nextLine();
                System.out.print("Occupation: "); String occupation = scanner.nextLine();
                System.out.print("Date of birth (yyyy-mm-dd): "); LocalDate dob = LocalDate.parse(scanner.nextLine());
                System.out.print("Empathy level (0-10): "); double empathy = Double.parseDouble(scanner.nextLine());

                PersonData personData = new PersonData(
                        List.of(username, password, email, firstName, lastName, occupation, dob.toString(), String.valueOf(empathy))
                );

                User person = userFactory.createUser(PersonTypes.DEFAULT, personData);
                usersService.save(person);
                System.out.println("Person added!");

            } else if (type == 2) { // Duck
                System.out.println("Duck type (1=FLYING, 2=SWIMMING, 3=FLYING_AND_SWIMMING): ");
                int typeChoice = Integer.parseInt(scanner.nextLine());
                DuckTypes duckType = switch (typeChoice) {
                    case 1 -> DuckTypes.FLYING;
                    case 2 -> DuckTypes.SWIMMING;
                    default -> DuckTypes.FLYING_AND_SWIMMING;
                };

                System.out.print("Speed: "); Double speed = Double.parseDouble(scanner.nextLine());
                System.out.print("Rezistance: "); Double rezistance = Double.parseDouble(scanner.nextLine());

                DuckData duckData = new DuckData(
                        List.of(username, password, email, String.valueOf(speed), String.valueOf(rezistance))
                );

                User duck = userFactory.createUser(duckType,duckData);
                usersService.save(duck);
                System.out.println("Duck added!");

            } else {
                System.out.println("Invalid type!");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    private void removeUser() {
        try {
            System.out.print("Enter ID to remove: ");
            Long id = Long.parseLong(scanner.nextLine());

            var user = usersService.delete(id);
            System.out.println("Removed user: " + user);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    private void findUser() {
        try {
            System.out.print("Enter ID: ");
            Long id = Long.parseLong(scanner.nextLine());

            var user = usersService.findOne(id);
            if(user == null){
                System.out.println("User not found!");
            } else {
                System.out.println("User: " + user);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    private void updateUser() {
        try {
            System.out.print("Enter ID: ");
            Long id = Long.parseLong(scanner.nextLine());

            User user = usersService.findOne(id);
            if(user == null){
                System.out.println("User not found!");
            } else {
                System.out.println("Selected user: " + user);
            }
            if(user instanceof Person){
                System.out.print("New Username: "); String username = scanner.nextLine();
                System.out.print("New Password: "); String password = scanner.nextLine();
                System.out.print("New Email: "); String email = scanner.nextLine();
                System.out.print("New First name: "); String firstName = scanner.nextLine();
                System.out.print("New Last name: "); String lastName = scanner.nextLine();
                System.out.print("New Occupation: "); String occupation = scanner.nextLine();
                System.out.print("New Date of birth (yyyy-mm-dd): "); LocalDate dob = LocalDate.parse(scanner.nextLine());
                System.out.print("New Empathy level (0-10): "); double empathy = Double.parseDouble(scanner.nextLine());

                user.setUsername(username);
                user.setPassword(password);
                user.setEmail(email);
                ((Person) user).setFirstName(firstName);
                ((Person) user).setLastName(lastName);
                ((Person) user).setOccupation(occupation);
                ((Person) user).setDateOfBirth(dob);
                ((Person) user).setEmpathyLevel(empathy);

                usersService.update(user);
            } else if (user instanceof Duck){
                System.out.print("New Username: "); String username = scanner.nextLine();
                System.out.print("New Password: "); String password = scanner.nextLine();
                System.out.print("New Email: "); String email = scanner.nextLine();
                System.out.println("New Duck type (1=FLYING, 2=SWIMMING, 3=FLYING_AND_SWIMMING): ");

                int typeChoice = Integer.parseInt(scanner.nextLine());
                DuckTypes duckType = switch (typeChoice) {
                    case 1 -> DuckTypes.FLYING;
                    case 2 -> DuckTypes.SWIMMING;
                    default -> DuckTypes.FLYING_AND_SWIMMING;
                };

                System.out.print("New Speed: "); Double speed = Double.parseDouble(scanner.nextLine());
                System.out.print("New Rezistance: "); Double rezistance = Double.parseDouble(scanner.nextLine());

                user.setUsername(username);
                user.setPassword(password);
                user.setEmail(email);
                ((Duck) user).setDuckType(duckType);
                ((Duck) user).setSpeed(speed);
                ((Duck) user).setRezistance(rezistance);
                usersService.update(user);

            }
            System.out.println("Updated user: " + user);
        }catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }


    }

    private void viewAllUsers() {
        System.out.println("=== All Persons ===");
        usersService.findAll().forEach(user -> {
            if (user instanceof Person) {
                System.out.println(user);
            }
        });

        System.out.println("=== All Ducks ===");
        usersService.findAll().forEach(user -> {
            if (user instanceof Duck) {
                System.out.println(user);
            }
        });

    }
}
