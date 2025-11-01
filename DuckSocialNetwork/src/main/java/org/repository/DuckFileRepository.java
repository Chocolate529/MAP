package org.repository;


import org.domain.Duck;
import org.domain.validators.Validator;
import org.utils.enums.DuckTypes;

import java.util.List;

public class DuckFileRepository extends EntityFileRepository<Long, Duck>{
    public DuckFileRepository(String fileName, Validator<Duck> validator) {
        super(fileName, validator);
    }

    @Override
    public Duck extractEntity(List<String> attributes) {
        Long id = Long.parseLong(attributes.get(0));
        String username = attributes.get(1);
        String password = attributes.get(2);
        String email =  attributes.get(3);
        DuckTypes type = DuckTypes.valueOf(attributes.get(4));
        Double speed = Double.parseDouble(attributes.get(5));
        Double rezistance = Double.parseDouble(attributes.get(6));
        var duck =  new Duck(username, password, email, type, speed, rezistance);
        duck.setId(id);
        return duck;
    }

    @Override
    protected String createEntityAsString(Duck d) {
        return String.join(",",
                String.valueOf(d.getId()),
                d.getUsername(),
                d.getPassword(),
                d.getEmail(),
                String.valueOf(d.getDuckType()),
                String.valueOf(d.getSpeed()),
                String.valueOf(d.getRezistance())
        );
    }
}
