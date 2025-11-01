// java
package org.repository;

import org.domain.Person;
import org.domain.validators.Validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PersonFileRepository extends EntityFileRepository<Long, Person> {

    public PersonFileRepository(String fileName, Validator<Person> validator) {
        super(fileName, validator);
    }

    @Override
    public Person extractEntity(List<String> attributes) {
        Long id = Long.parseLong(attributes.get(0));
        String username = attributes.get(1);
        String password = attributes.get(2);
        String email = attributes.get(3);
        String firstName = attributes.get(4);
        String lastName = attributes.get(5);
        String occupation = attributes.get(6);
        LocalDate dateOfBirth = LocalDate.parse(attributes.get(7), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        Double empathyLevel = Double.parseDouble(attributes.get(8));

        var person =  new Person(username, password, email, firstName, lastName, occupation, dateOfBirth, empathyLevel);
        person.setId(id);
        return person;
    }

    @Override
    protected String createEntityAsString(Person p) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.join(",",
                String.valueOf(p.getId()),
                p.getUsername(),
                p.getPassword(),
                p.getEmail(),
                p.getFirstName(),
                p.getLastName(),
                p.getOccupation(),
                p.getDateOfBirth().format(formatter),
                String.valueOf(p.getEmpathyLevel())
        );
    }
}
