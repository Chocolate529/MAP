package org.domain.validators;

import org.domain.exceptions.ValidationException;
import org.domain.users.duck.Duck;
import org.domain.users.duck.flock.Flock;

public class FlockValidator implements Validator<Flock<Duck>> {
    @Override
    public void validate(Flock<Duck> entity) throws ValidationException {
        if (entity.getFlockName() == null || entity.getFlockName().trim().isEmpty()) {
            throw new ValidationException("Flock name cannot be empty.\n");
        }
    }
}
