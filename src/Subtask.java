import java.util.ArrayList;

class Subtask extends Task{
    private int epicID;

    public Subtask(String taskName, ArrayList<String> taskDescription, int epicID) {
        super(taskName, taskDescription);
        this.epicID = epicID;
    }

    public Subtask(Subtask subtask) {
        this.taskName = subtask.taskName;
        this.taskDescription = subtask.taskDescription;
        this.taskID = subtask.taskID;
        this.epicID = subtask.epicID;
        this.status = subtask.status;
    }
    public int getEpicID() {
        return epicID;
    }

    public void setEpicID(int epicID) {
        this.epicID = epicID;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicID=" + epicID +
                ", taskName='" + taskName + '\'' +
                ",\ntaskDescription=" + taskDescription +
                ",\ntaskID=" + taskID +
                ", status='" + status + '\'' +
                '}';
    }
}
