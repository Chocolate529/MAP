package org.repository;

import org.domain.Observer;
import org.domain.events.RaceEvent;
import org.domain.users.duck.Duck;
import org.domain.users.duck.SwimmingDuck;
import org.domain.validators.Validator;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
        Double maxTime = Double.parseDouble(attributes.get(1));

        List<Integer> distances = Arrays.stream(attributes.get(2).split("\\|"))
                .map(Integer::parseInt)
                .toList();

        List<Long> duckIds = Arrays.stream(attributes.get(3).split("\\|"))
                .map(Long::parseLong)
                .toList();

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
                String.valueOf(entity.getMaxTime()),
                distancesString,
                observersString);
    }
}
