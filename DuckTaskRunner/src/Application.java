import domain.Duck;
import domain.Lane;
import factory.Strategy;
import model.MessageTask;
import reader.InputReader;
import reader.NatatieFileReader;
import runner.DelayTaskRunner;
import runner.PrinterTaskRuner;
import solver.NatatieBinarySolver;
import solver.Solver;
import taskrunner.StrategyTaskRunner;
import writer.NatatieFileWriter;
import writer.OutputWriter;

import javax.sound.midi.Soundbank;
import java.time.LocalDateTime;

import static utils.enums.SolvingStrategy.BINARY_SEARCH_STRATEGY;

public class Application {

    public static void main(String[] args) {
        var reader = new NatatieFileReader(args[0]);
        var writer = new NatatieFileWriter(args[1],0.0);
        var natatieSolver = new NatatieBinarySolver("1","Finding min time",writer,reader, BINARY_SEARCH_STRATEGY);

        natatieSolver.execute();
        var minVal = natatieSolver.solve();
        System.out.println("The minimum value is " + minVal);

        var usedTasks = natatieSolver.getUsedTasks();
        for(var task : usedTasks){
            System.out.println(task);
        }
    }

}
