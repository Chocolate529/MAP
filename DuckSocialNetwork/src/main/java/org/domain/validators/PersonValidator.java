package org.domain.validators;

import org.domain.users.person.Person;
import org.domain.exceptions.ValidationException;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class PersonValidator implements Validator<Person> {
    //TODO extend generic usre validator
    private Pattern pattern;

    public PersonValidator(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    @Override
    public void validate(Person person) throws ValidationException {
        StringBuilder errors = new StringBuilder();

        if (person == null) {
            throw new ValidationException("Entity cannot be null");
        }

        if (person.getFirstName() == null || person.getFirstName().trim().isEmpty()) {
            errors.append("First name cannot be empty.\n");
        }

        if (person.getLastName() == null || person.getLastName().trim().isEmpty()) {
            errors.append("Last name cannot be empty.\n");
        }

        if (person.getUsername() == null || person.getUsername().trim().isEmpty()) {
            errors.append("Username cannot be empty.\n");
        } else if (person.getUsername().length() < 3) {
            errors.append("Username must have at least 3 characters.\n");
        }

        if (person.getPassword() == null || person.getPassword().length() < 6) {
            errors.append("Password must have at least 6 characters.\n");
        }

        // Validate email
        if (person.getEmail() == null || !pattern.matcher(person.getEmail()).matches())  {
            errors.append("Invalid email format.\n");
        }

        // Validate date of birth
        LocalDate dob = person.getDateOfBirth();
        if (dob == null) {
            errors.append("Date of birth cannot be null.\n");
        } else if (dob.isAfter(LocalDate.now())) {
            errors.append("Date of birth cannot be in the future.\n");
        }

        // Validate empathy level (optional)
        if (person.getEmpathyLevel() != null) {
            if (person.getEmpathyLevel() < 0 || person.getEmpathyLevel() > 10) {
                errors.append("Empathy level must be between 0 and 10.\n");
            }
        }

        // If any errors were found, throw exception
        if (!errors.isEmpty()) {
            throw new ValidationException(errors.toString());
        }
    }
}