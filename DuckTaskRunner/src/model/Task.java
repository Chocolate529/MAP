package model;

import java.util.Objects;

public abstract class Task implements Comparable<Task>{
    private Integer priority;

    public Integer getPriority() {
        return priority;
    }

    @Override
    public int compareTo(Task o) {
        return Integer.compare(priority, o.priority);
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    private String taskId;
    private String description;

    public Task(String taskId, String description, int priority) {
        this.taskId = taskId;
        this.description = description;
        this.priority = priority;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public abstract void execute();

    @Override
    public String toString() {
        return "id="+taskId+" | description="+description;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(taskId, task.taskId) && Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, description);
    }
}
