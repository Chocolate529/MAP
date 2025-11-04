package org.domain.events;

import org.domain.Observer;
import org.domain.users.duck.SwimmingDuck;

import java.util.ArrayList;
import java.util.List;

public class RaceEvent extends Event<SwimmingDuck> {

    Double maxTime;
    List<Integer> distances;
    String name;

    public RaceEvent(List<SwimmingDuck> subscribers) {
        super(subscribers);
        distances = new ArrayList<>();
        maxTime = 0.0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RaceEvent(String name){
        super(new ArrayList<>());
        distances = new ArrayList<>();
        this.name = name;
        maxTime = 0.0;
    }

    @Override
    public String toString() {
        return super.toString()+"RaceEvent{" +
                "maxTime=" + maxTime +
                ", name='" + name + '\'' +
                '}';
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
