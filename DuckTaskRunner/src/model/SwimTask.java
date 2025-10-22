package model;

import domain.Duck;
import domain.Lane;

public class SwimTask extends Task{
    Duck duck;
    Lane lane;
    Double timeLimit;
    Double curentTime = 0.0;

    public Double getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Double timeLimit) {
        this.timeLimit = timeLimit;
    }

    public SwimTask(String taskId, String description, Duck duck, Lane lane, Double timeLimit) {
        super(taskId, description);
        this.duck = duck;
        this.lane = lane;
        this.timeLimit = timeLimit;
    }

    public Duck getDuck() {
        return duck;
    }

    public void setDuck(Duck duck) {
        this.duck = duck;
    }

    public Lane getLane() {
        return lane;
    }

    public void setLane(Lane lane) {
        this.lane = lane;
    }

    private Boolean hasBetterTime(Double timeLimit){
        return curentTime <= timeLimit;
    }

    @Override
    public void execute() {
        curentTime = 2*lane.getLenght() / duck.getViteza();
    }
}
