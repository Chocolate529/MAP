package org.domain.events;

import org.domain.Observer;
import org.domain.users.duck.SwimmingDuck;

import java.util.ArrayList;
import java.util.List;

public class RaceEvent extends Event<SwimmingDuck> {

    Double maxTime;
    List<Integer> distances;

    public RaceEvent(List<SwimmingDuck> subscribers) {
        super(subscribers);
        distances = new ArrayList<>();
        maxTime = 0.0;
    }

    public RaceEvent(){
        super(new ArrayList<>());
        distances = new ArrayList<>();
        maxTime = 0.0;
    }

    public Double getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(Double maxTime) {
        this.maxTime = maxTime;
    }

    public List<Integer> getDistances() {
        return distances;
    }

    public void setDistances(List<Integer> distances) {
        this.distances = distances;
    }
}
