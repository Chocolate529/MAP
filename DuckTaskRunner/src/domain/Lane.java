package domain;

public class Lane {
    private final Integer lenght;

    public Lane(Integer lenght) {
        this.lenght = lenght;
    }

    public Integer getLenght() {
        return lenght;
    }

    @Override
    public String toString() {
        return "Lane{" +
                "lenght=" + lenght +
                '}';
    }
}
