package org.repository.file.user.duck;


import org.domain.dtos.DuckData;
import org.domain.users.duck.Duck;
import org.domain.users.duck.DuckFactory;
import org.domain.validators.Validator;
import org.repository.file.EntityFileRepository;
import org.utils.enums.DuckTypes;

import java.util.List;

public class DuckFileRepository extends EntityFileRepository<Long, Duck> {
    private final DuckFactory duckFactory;

    public DuckFileRepository(String fileName, Validator<Duck> validator) {
        super(fileName, validator, false);
        duckFactory = new DuckFactory();
        loadData();
    }

    @Override
    public Duck extractEntity(List<String> attributes) {
        Long id = Long.parseLong(attributes.get(0));

        DuckTypes type = DuckTypes.valueOf(attributes.get(4));

        List<String> dataAttributes = List.of(
                attributes.get(1), // username
                attributes.get(2), // password
                attributes.get(3), // email
                attributes.get(5), // speed
                attributes.get(6)  // rezistance
        );

        DuckData data = new DuckData(dataAttributes);
        var duck = duckFactory.create(type, data);

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
