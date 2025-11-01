package org.service;

import org.domain.Friendship;
import org.domain.validators.Validator;
import org.repository.Repository;
import org.service.utils.IdGenerator;

public class FriendshipService extends EntityService<Long, Friendship>{

    private UsersService usersService;

    public FriendshipService(Validator<Friendship> validator, Repository<Long, Friendship> repository, IdGenerator<Long> idGenerator, UsersService usersService) {
        super(validator, repository, idGenerator);
        this.usersService = usersService;
    }
    //TODO implement CRUD for friendships between users, most sociable friendship network, biggest friendship network

}
