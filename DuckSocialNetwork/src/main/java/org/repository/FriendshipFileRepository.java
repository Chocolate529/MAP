package org.repository;

import org.domain.Entity;
import org.domain.Friendship;
import org.domain.User;
import org.domain.validators.Validator;

import java.util.List;
import java.util.Map;

public class FriendshipFileRepository extends EntityFileRepository<Long, Friendship> {


    private final DuckFileRepository duckFileRepository;
    private final PersonFileRepository personFileRepository;
    protected Map<Long, User> users;

    public FriendshipFileRepository(String fileName, Validator<Friendship> validator, DuckFileRepository duckFileRepository, PersonFileRepository personFileRepository) {
        super(fileName, validator);
        this.duckFileRepository = duckFileRepository;
        this.personFileRepository = personFileRepository;
        loadData();
        refreshUsers();
    }

    @Override
    public Friendship extractEntity(List<String> attributes) {
        if (attributes.size() < 2) {
            throw new IllegalArgumentException("Invalid friendship data");
        }

        Long id = Long.parseLong(attributes.get(0));
        Long user1Id = Long.parseLong(attributes.get(0));
        Long user2Id = Long.parseLong(attributes.get(1));

        User user1 = users.get(user1Id);
        User user2 = users.get(user2Id);

        if (user1 == null || user2 == null) {
            throw new IllegalArgumentException("User IDs do not exist in the system");
        }

        var friendShip = new Friendship(user1, user2);
        friendShip.setId(id);
        return friendShip;
    }

    @Override
    protected String createEntityAsString(Friendship entity) {
        return entity.getId()+","+entity.getUser1().getId() + "," + entity.getUser2().getId();
    }

    protected void refreshUsers(){
        duckFileRepository.findAll().forEach(d -> {
            users.put(d.getId(), d);
        });
        personFileRepository.findAll().forEach(u -> {
            users.put(u.getId(), u);
        });
    }
}