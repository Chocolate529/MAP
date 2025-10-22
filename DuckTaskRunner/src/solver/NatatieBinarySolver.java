package solver;

import domain.Duck;
import domain.Lane;
import reader.NatatieFileReader;
import utils.enums.SolvingStrategy;
import writer.NatatieFileWriter;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.abs;
import static utils.Constants.ALFA;

public class NatatieBinarySolver extends NatatieTaskSolver{
    private SolvingStrategy strategy;

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
    }

    @Override
    public Double solve() {
        if (!executed) {
            throw new RuntimeException("Execute before solving");
        }

        for (Duck duck : ducks) {
            System.out.println(duck.toString());
        }

        while (Math.abs(maxVal - minVal) > ALFA) {
            Double midVal = (maxVal + minVal) / 2;

            if (canFinishRace(midVal))
            {
                maxVal = midVal;
            } else {
                minVal = midVal;
            }
        }

        return maxVal;
    }

    private Boolean canFinishRace(Double timeLimit) {
        int laneIndex = 0;
        Duck lastDuck = null;

        for (Duck duck : ducks) {
            if (laneIndex == lanes.size()) {
                break;
            }

            Lane lane = lanes.get(laneIndex);
            double time = 2.0 * lane.getLenght() / duck.getViteza();


            if (time <= timeLimit ) {
                lastDuck = duck;
                laneIndex++;

            }
        }

        return laneIndex == lanes.size();
    }

    @Override
    public void execute() {
        Collections.sort(ducks);
        maxVal = findMaxInterval();
        lanes.sort((a, b) -> a.getLenght().compareTo(b.getLenght()));
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
}
