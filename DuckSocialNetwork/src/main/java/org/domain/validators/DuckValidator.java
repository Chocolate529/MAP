package org.domain.validators;

import org.domain.Duck;
import org.domain.exceptions.ValidationException;

import java.util.regex.Pattern;

public class DuckValidator implements Validator<Duck> {
    //TODO extend generic usre validator
    private final Pattern pattern;

    public DuckValidator(String p) {
        this.pattern = Pattern.compile(p);
    }

    @Override
    public void validate(Duck entity) throws ValidationException {
        StringBuilder errors = new StringBuilder();

        if (entity.getUsername() == null || entity.getUsername().isBlank())
            errors.append("Username cannot be empty.\n");
        if (entity.getPassword() == null || entity.getPassword().length() < 3)
            errors.append("Password must have at least 3 characters.\n");
        if (entity.getEmail() == null || !pattern.matcher(entity.getEmail()).matches())
            errors.append("Email not valid.\n");
        if (entity.getSpeed() == null || entity.getSpeed() <= 0)
            errors.append("Speed must be positive.\n");
        if (entity.getRezistance() == null || entity.getRezistance() <= 0)
            errors.append("Rezistance must be positive.\n");

        if (!errors.isEmpty())
            throw new ValidationException(errors.toString());
    }
}
