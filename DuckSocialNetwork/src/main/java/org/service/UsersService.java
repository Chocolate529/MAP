package org.service;

import org.domain.Duck;
import org.domain.Person;
import org.domain.User;
import org.domain.exceptions.ServiceException;
import org.repository.DuckFileRepository;
import org.repository.PersonFileRepository;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;


public class UsersService implements Service<Long, User> {
    private final DuckFileRepository ducksService;
    private final PersonFileRepository personsService;

    public UsersService(DuckFileRepository ducksService, PersonFileRepository personsService) {
        this.ducksService = ducksService;
        this.personsService = personsService;
    }


    public User findOne(Long id) {
        User user = ducksService.findOne(id);
        if(user == null){
            user = personsService.findOne(id);
        }
        return user;
    }

    public Iterable<User> findAll() {
        return Stream.concat(StreamSupport.stream(ducksService.findAll().spliterator(),false),
                            StreamSupport.stream(personsService.findAll().spliterator(),false)).toList();
    }

    public User save(User entity) {
        //TODO user factory based on given UserTypes
        if(entity instanceof Duck) return ducksService.save((Duck) entity);
        if(entity instanceof Person) return  personsService.save((Person) entity);
        throw new ServiceException("Invalid user type");
    }

    public User delete(Long id) {
        User user = ducksService.findOne(id);
        if(user != null) {
            return ducksService.delete(id);
        } else {
            user = personsService.findOne(id);
            if(user != null) {
                return personsService.delete(id);
            } else {
                throw new ServiceException("No user found with id " + id);
            }
        }
    }

    public User update(User entity) {
        User user = ducksService.findOne(entity.getId());
        if(user != null) {
            return ducksService.update((Duck) entity);
        } else {
            user = personsService.findOne(entity.getId());
            if(user != null) {
                return personsService.update((Person) entity);
            } else {
                throw new ServiceException("No user found with id " + entity.getId());
            }
        }
    }

}
