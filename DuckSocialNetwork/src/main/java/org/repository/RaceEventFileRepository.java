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


        List<Integer> distances;
        if (attributes.size() > 3 && attributes.get(3) != null && !attributes.get(3).isBlank()) {
            distances = Arrays.stream(attributes.get(3).split("\\|"))
                    .filter(str -> !str.isBlank())
                    .map(Integer::parseInt)
                    .collect(Collectors.toCollection(ArrayList::new));
        } else {
            distances = new ArrayList<>();
        }

        List<Long> duckIds ;
        if(attributes.size() > 4 && attributes.get(4) != null && !attributes.get(4).isBlank()) {
            duckIds = Arrays.stream(attributes.get(4).split("\\|"))
                    .filter(str -> !str.isBlank())
                    .map(Long::parseLong)
                    .collect(Collectors.toCollection(ArrayList::new));
        } else{
                duckIds = new ArrayList<>();
        }


        List<SwimmingDuck> ducks  = duckIds.stream()
                .map(duckFileRepository::findOne)
                .filter(Objects::nonNull)
                .map(d -> (SwimmingDuck) d)
                .collect(Collectors.toCollection(ArrayList::new));

        var raceEvent = new RaceEvent(ducks, name);
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
