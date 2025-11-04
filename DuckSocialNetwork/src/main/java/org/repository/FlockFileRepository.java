package org.repository;

import org.domain.users.duck.Duck;
import org.domain.users.duck.flock.Flock;
import org.domain.validators.Validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FlockFileRepository extends EntityFileRepository<Long, Flock<Duck>> {

    private final DuckFileRepository duckFileRepository;


    public FlockFileRepository(String fileName, Validator<Flock<Duck>> validator, DuckFileRepository duckFileRepository) {
        super(fileName, validator,false);
        this.duckFileRepository = duckFileRepository;
        loadData();
    }

    @Override
    public Flock<Duck> extractEntity(List<String> attributes) {
        Long id = Long.parseLong(attributes.get(0));
        String name = attributes.get(1);


        List<Duck> members = new ArrayList<>();
        if (attributes.size() > 2 && !attributes.get(2).isBlank()) {
            members = Arrays.stream(attributes.get(2).split("\\|"))
                    .filter(s -> !s.isBlank()) // skip empty strings
                    .map(Long::parseLong)
                    .map(duckFileRepository::findOne)
                    .filter(Objects::nonNull)
                    .toList();
        }

        var flock = new Flock<>(name);
        flock.setId(id);
        members.forEach(flock::addMember);

        return flock;
    }

    @Override
    protected String createEntityAsString(Flock<Duck> f) {

        String membersString = f.getMembers().stream()
                .map(d -> d.getId().toString())
                .collect(Collectors.joining("|"));

        return String.join(",",
                String.valueOf(f.getId()),
                f.getFlockName(),
                membersString
        );
    }
}
