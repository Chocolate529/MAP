package reader;

import domain.Duck;
import domain.Lane;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NatatieFileReader implements InputReader {
    private final String fileName;
    private Integer nrOfDucks;
    private Integer nrOfLanes;
    private ArrayList<Duck> ducks;
    private ArrayList<Lane> lanes;

    public NatatieFileReader(String fileName) {
        this.fileName = fileName;
        read();
    }

    public Integer getNrOfDucks() {
        return nrOfDucks;
    }

    public Integer getNrOfLanes() {
        return nrOfLanes;
    }

    public ArrayList<Duck> getDucks() {
        return ducks;
    }

    public ArrayList<Lane> getLanes() {
        return lanes;
    }

    @Override
    public void read() {
        File file = new File(fileName);
        try {
            Scanner scanner = new Scanner(file);

            nrOfDucks = scanner.nextInt();
            nrOfLanes = scanner.nextInt();

            Double[] speeds = new Double[nrOfDucks];
            for (int i = 0; i < nrOfDucks; i++) {
                speeds[i] = scanner.nextDouble();
            }
            Integer[] rezistances = new Integer[nrOfDucks];
            for (int i = 0; i < nrOfDucks; i++) {
                rezistances[i] = scanner.nextInt();
            }

            ducks = new ArrayList<>();
            for (int i = 0; i < nrOfDucks; i++) {
                ducks.add(new Duck(rezistances[i], speeds[i], i));
            }

            lanes = new ArrayList<>();
            for (int i = 0; i < nrOfLanes; i++) {
                lanes.add(new Lane(scanner.nextInt()));
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
