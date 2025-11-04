package org.repository;

import org.domain.Observer;
import org.domain.events.RaceEvent;
import org.domain.users.duck.Duck;
import org.domain.users.duck.SwimmingDuck;
import org.domain.validators.Validator;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class RaceEventFileRepository extends EntityFileRepository<Long, RaceEvent>{

    private DuckFileRepository duckFileRepository;

    public RaceEventFileRepository(String fileName, Validator<RaceEvent> validator, DuckFileRepository duckFileRepository) {
        super(fileName, validator, false);
        this.duckFileRepository = duckFileRepository;
        loadData();
    }

    @Override
    public RaceEvent extractEntity(List<String> attributes) {
        Long id = Long.parseLong(attributes.get(0));
        String name = attributes.get(1);
        Double maxTime = Double.parseDouble(attributes.get(2));



        List<Integer> distances = Optional.ofNullable(attributes.size() > 3 ? attributes.get(3) : null)
                .filter(s -> !s.isBlank())
                .map(s -> Arrays.stream(s.split("\\|"))
                        .filter(str -> !str.isBlank())
                        .map(Integer::parseInt)
                        .toList())
                .orElse(Collections.emptyList());

        List<Long> duckIds = Optional.ofNullable(attributes.size() > 4 ? attributes.get(4) : null)
                .filter(s -> !s.isBlank())
                .map( s-> Arrays.stream(s.split("\\|"))
                        .filter(str -> !str.isBlank())
                        .map(Long::parseLong)
                        .toList())
                .orElse(Collections.emptyList());

        List<SwimmingDuck> ducks  = duckIds.stream()
                .map(duckFileRepository::findOne)
                .filter(Objects::nonNull)
                .map(d -> (SwimmingDuck) d)
                .toList();

        var raceEvent = new RaceEvent(ducks);
        raceEvent.setId(id);
        raceEvent.setMaxTime(maxTime);
        raceEvent.setDistances(distances);
        return raceEvent;
    }

    @Override
    protected String createEntityAsString(RaceEvent entity) {
        String distancesString = entity.getDistances().stream()
                .map(String::valueOf)
                .collect(Collectors.joining("|"));

        String observersString = entity.getSubscribers().stream()
                .map( o -> ((Duck) o).getId().toString())
                .collect(Collectors.joining("|"));

        return String.join(",",
                String.valueOf(entity.getId()),
                entity.getName(),
                String.valueOf(entity.getMaxTime()),
                distancesString,
                observersString);
    }
}
