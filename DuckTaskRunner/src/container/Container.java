package container;

import model.Task;

import java.util.List;

public interface Container {

    Task remove();

    void add(Task task);

    int size();

    boolean isEmpty();
    List<Task> getTasks();
}
