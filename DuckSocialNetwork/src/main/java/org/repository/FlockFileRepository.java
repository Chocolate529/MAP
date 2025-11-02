package org.repository;

import org.domain.users.duck.Duck;
import org.domain.users.duck.flock.Flock;
import org.domain.validators.Validator;

import java.util.List;
import java.util.stream.Collectors;

public class FlockFileRepository extends EntityFileRepository<Long, Flock<Duck>> {

    private final DuckFileRepository duckFileRepository;


    public FlockFileRepository(String fileName, Validator<Flock<Duck>> validator, DuckFileRepository duckFileRepository) {
        super(fileName, validator);
        this.duckFileRepository = duckFileRepository;
    }

    @Override
    public Flock<Duck> extractEntity(List<String> attributes) {
        Long id = Long.parseLong(attributes.get(0));
        String name = attributes.get(1);
        List<Long> members = attributes.stream().mapToLong(Long::parseLong).boxed().toList();

        var flock = new Flock<>(name);
        flock.setId(id);
        members.forEach(member -> {
            flock.addMember(duckFileRepository.findOne(member));
        });

        return flock;
    }

    @Override
    protected String createEntityAsString(Flock<Duck> f) {
        return String.join(",",
                String.valueOf(f.getId()),
                f.getFlockName(),
                f.getMembers().stream().map(duck -> String.valueOf(duck.getId())).collect(Collectors.joining(","))
        );
    }
}
