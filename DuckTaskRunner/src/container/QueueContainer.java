package container;

import model.Task;

import java.util.List;

public class QueueContainer implements Container {

    @Override
    public Task remove() {
        return null;
    }

    @Override
    public void add(Task task) {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public List<Task> getTasks() {
        return List.of();
    }
}
