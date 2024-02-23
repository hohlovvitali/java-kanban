package tasktype;

public class Subtask extends Task {
    private int epicID;

    public Subtask(String taskName, String taskDescription, int epicID) {
        super(taskName, taskDescription);
        this.epicID = epicID;
        this.taskType = TaskType.SUBTASK;
    }

    public Subtask(Subtask subtask) {
        super(subtask);
        this.epicID = subtask.epicID;
    }

    public Subtask(int taskID, TaskType taskType, String taskName, TaskStatus taskStatus, String taskDescription, int epicID) {
        super(taskID, taskType, taskName, taskStatus, taskDescription);
        this.epicID = epicID;
    }

    public int getEpicID() {
        return epicID;
    }

    public void setEpicID(int epicID) {
        this.epicID = epicID;
    }

    @Override
    public String toString() {
        return super.toString() + "," + epicID;
    }
}
