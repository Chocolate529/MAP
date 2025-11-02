package org.domain.users.person;

import org.domain.Factory;
import org.domain.dtos.DuckData;
import org.domain.dtos.PersonData;
import org.domain.users.duck.Duck;
import org.domain.users.duck.FlyingAndSwimmingDuck;
import org.domain.users.duck.FlyingDuck;
import org.domain.users.duck.SwimmingDuck;
import org.utils.enums.DuckTypes;
import org.utils.enums.PersonTypes;

import static org.utils.enums.PersonTypes.DEFAULT;

public class PersonFactory implements Factory<Person, PersonTypes, PersonData> {
    public PersonFactory() {
    }

    @Override
    public Person create(PersonTypes personType, PersonData personData) {
        switch (personType) {
            case DEFAULT -> {
                return new Person(personData);
            }
            default -> {
                return null;
            }
        }
    }
}
