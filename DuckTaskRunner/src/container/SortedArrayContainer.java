package container;

import container.Container;
import model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortedArrayContainer implements Container {
    private final List<Task> tasks;

    public SortedArrayContainer() {
        this.tasks = new ArrayList<>();
    }

    @Override
    public void add(Task task) {
        tasks.add(task);
        Collections.sort(tasks);
    }

    @Override
    public Task remove() {
        if (tasks.isEmpty()) {
            return null;
        }

        return tasks.removeFirst();
    }

    public Task get(int i) {
        return tasks.get(i);
    }

    public Task remove(int i) {
        return tasks.remove(i);
    }

    @Override
    public int size() {
        return tasks.size();
    }

    @Override
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    @Override
    public String toString() {
        return tasks.toString();
    }
}

