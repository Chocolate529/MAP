package writer;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class NatatieFileWriter implements OutputWriter {
    private final  String fileName;
    private Double minTime;
    public NatatieFileWriter(String fileName, Double minTime) {
        this.fileName = fileName;
        this.minTime =  minTime;
    }

    public void setMinTime(Double minTime) {
        this.minTime = minTime;
    }

    @Override
    public void write() {
        try  {
            PrintWriter out = new PrintWriter(fileName);
            out.print(minTime);
            out.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
