// java
package org.repository;

import org.domain.dtos.PersonData;
import org.domain.users.person.Person;
import org.domain.users.person.PersonFactory;
import org.domain.validators.Validator;
import org.utils.enums.PersonTypes;

import java.util.List;

import static org.utils.Constants.DATE_FORMAT;

public class PersonFileRepository extends EntityFileRepository<Long, Person> {

    private final PersonFactory personFactory = new PersonFactory();

    public PersonFileRepository(String fileName, Validator<Person> validator) {
        super(fileName, validator,false);
        loadData();
    }

    @Override
    public Person extractEntity(List<String> attributes) {
        Long id = Long.parseLong(attributes.get(0));

        List<String> attr = List.of(
                attributes.get(1),
                attributes.get(2),
                attributes.get(3),
                attributes.get(4),
                attributes.get(5),
                attributes.get(6),
                attributes.get(7),
                attributes.get(8)
        );
        var person = personFactory.create(PersonTypes.DEFAULT, new PersonData(attr));
        person.setId(id);
        return person;
    }

    @Override
    protected String createEntityAsString(Person p) {
        return String.join(",",
                String.valueOf(p.getId()),
                p.getUsername(),
                p.getPassword(),
                p.getEmail(),
                p.getFirstName(),
                p.getLastName(),
                p.getOccupation(),
                p.getDateOfBirth().format(DATE_FORMAT),
                String.valueOf(p.getEmpathyLevel())
        );
    }
}
