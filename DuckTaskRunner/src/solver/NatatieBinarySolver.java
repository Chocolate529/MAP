package solver;

import domain.Duck;
import domain.Lane;
import factory.Strategy;
import model.SwimTask;
import model.Task;
import reader.NatatieFileReader;
import taskrunner.DuckTaskRunner;
import utils.enums.SolvingStrategy;
import writer.NatatieFileWriter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.Math.abs;
import static utils.Constants.ALFA;

public class NatatieBinarySolver extends NatatieTaskSolver{
    private SolvingStrategy strategy;
    private DuckTaskRunner runner;

    private Double minVal = 0.0;
    private Double maxVal;

    private final ArrayList<Duck> ducks;
    private final ArrayList<Lane> lanes;

    private Boolean executed = false;

    public NatatieBinarySolver(String taskId, String description, NatatieFileWriter w, NatatieFileReader r, SolvingStrategy strategy) {
        super(taskId, description, w, r);
        this.strategy = strategy;
        this.ducks = reader.getDucks();
        this.lanes = reader.getLanes();
        this.runner = new DuckTaskRunner(Strategy.SORTED_ARRAY);
    }

    @Override
    public Double solve() {
        if (!executed) {
            throw new RuntimeException("Execute before solving");
        }

        while (Math.abs(maxVal - minVal) > ALFA) {
            Double midVal = (maxVal + minVal) / 2;
            runner.clear();
            if (canFinishRace(midVal))
            {
                maxVal = midVal;
            } else {
                minVal = midVal;
            }
        }

        writer.setMinTime(minVal);
        writer.write();



        return maxVal;
    }

    private Boolean canFinishRace(Double timeLimit) {
        int laneIndex = 0;

        for (Duck duck : ducks) {
            if (laneIndex == lanes.size()) {
                break;
            }

            var lane = lanes.get(laneIndex);
            SwimTask task = new SwimTask(duck.toString()+lane.toString(),"",duck,lane,timeLimit, duck.getRezistena());
            task.execute();

            if (task.hasGoodTime(timeLimit)) {
                runner.addTask(task);
                laneIndex++;
            }
        }

        return laneIndex == lanes.size();
    }

    @Override
    public void execute() {
        Collections.sort(ducks);
        maxVal = findMaxInterval();


        executed = true;
    }

    private Double findMaxInterval(){
        var duck =  findSlowestDuck();
        var lane =  getLongestLane();

        return 2.0*lane.getLenght()/duck.getViteza();
    }

    private Duck findSlowestDuck(){
        Duck slowestDuck = ducks.getFirst();
        for(int i = 1; i < ducks.size(); i++){
            if(slowestDuck.getViteza() > ducks.get(i).getViteza()){
                slowestDuck = ducks.get(i);
            }
        }
        return slowestDuck;
    }

    private Lane getLongestLane(){
        return lanes.getLast();
    }

    public List<Task> getUsedTasks(){
        return runner.getAll();
    }
}
