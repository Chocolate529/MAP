package solver;

import model.Task;
import reader.NatatieFileReader;
import writer.NatatieFileWriter;

public abstract class NatatieTaskSolver extends Task implements Solver{
    protected final NatatieFileWriter writer;
    protected final NatatieFileReader reader;

    public NatatieTaskSolver(String taskId, String description, NatatieFileWriter writer, NatatieFileReader reader) {
        super(taskId, description);
        this.writer = writer;
        this.reader = reader;
    }

    public void readInput(){
        reader.read();
    }

    public void writeOutput(){
        writer.write();
    }

    public abstract Double solve();
    public abstract void execute();
}
