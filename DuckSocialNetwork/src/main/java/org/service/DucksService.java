package org.service;

import org.domain.exceptions.ServiceException;
import org.domain.users.duck.Duck;
import org.domain.validators.Validator;
import org.repository.Repository;
import org.service.utils.IdGenerator;

public class DucksService extends EntityService<Long, Duck>{

    private FlockService flockService;

    public DucksService(Validator<Duck> validator, Repository<Long, Duck> repository, IdGenerator<Long> idGenerator) {
        super(validator, repository, idGenerator);
    }

    public void setFlockService(FlockService flockService) {
        this.flockService = flockService;
    }

    @Override
    public Duck delete(Long duckId) {
        var flock = flockService.getFlockByDuckId(duckId);
        if(flock != null){
            flockService.removeDuckFromFlock(flock.getId(), duckId);
        }
        return repository.delete(duckId);
    }
}
