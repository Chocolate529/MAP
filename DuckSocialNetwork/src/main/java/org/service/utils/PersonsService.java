package org.service.utils;

import org.domain.Person;
import org.domain.validators.Validator;
import org.repository.Repository;
import org.service.EntityService;

public class PersonsService extends EntityService<Long, Person> {
    public PersonsService(Validator<Person> validator, Repository<Long, Person> repository, IdGenerator<Long> idGenerator) {
        super(validator, repository, idGenerator);
    }
}
