package org.service;

import org.domain.Duck;
import org.domain.validators.Validator;
import org.repository.Repository;
import org.service.utils.IdGenerator;

public class DucksService extends EntityService<Long, Duck>{
    public DucksService(Validator<Duck> validator, Repository<Long, Duck> repository, IdGenerator<Long> idGenerator) {
        super(validator, repository, idGenerator);
    }
}
