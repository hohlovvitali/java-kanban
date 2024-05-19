package tasks;

import java.time.Instant;

public class Subtask extends Task {
    private int epicID;

    public Subtask(String taskName, String taskDescription, int epicID) {
        super(taskName, taskDescription);
        this.epicID = epicID;
    }


    public Subtask(int taskID, String taskName, TaskStatus taskStatus, String taskDescription, Instant startTime, Instant endTime, int epicID) {
        super(taskID, taskName, taskStatus, taskDescription,startTime, endTime);
        this.epicID = epicID;
    }

    public Subtask(int taskID, String taskName, TaskStatus taskStatus, String taskDescription, int epicID) {
        super(taskID, taskName, taskStatus, taskDescription);
        this.epicID = epicID;
    }

    public int getEpicID() {
        return epicID;
    }

    public void setEpicID(int epicID) {
        this.epicID = epicID;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;
        Subtask task = (Subtask) o;
        return this.taskName.equals(task.taskName) && this.taskDescription.equals(task.taskDescription) &&
                this.taskID == task.getTaskID() && this.status == task.status && this.epicID == task.getEpicID();
    }

    @Override
    public String toString() {
        return super.toString() + "," + epicID;
    }
}
