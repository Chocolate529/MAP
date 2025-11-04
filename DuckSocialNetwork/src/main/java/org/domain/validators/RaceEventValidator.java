package org.domain.validators;

import org.domain.events.RaceEvent;
import org.domain.exceptions.ValidationException;
import org.domain.users.duck.SwimmingDuck;

public class RaceEventValidator implements Validator<RaceEvent> {
    @Override
    public void validate(RaceEvent entity) throws ValidationException {
        if (entity.getDistances() == null || entity.getDistances().isEmpty()) {
            throw new ValidationException("Distances cannot be null or empty");
        }

        boolean allPositive = entity.getDistances()
                .stream()
                .allMatch(distance -> distance != null && distance > 0);

        if (!allPositive) {
            throw new ValidationException("All distances must be positive");
        }
    }
}
