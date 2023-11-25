class Subtask extends Task{
    private int epicID;

    public Subtask(String taskName, String taskDescription, int epicID) {
        super(taskName, taskDescription);
        this.epicID = epicID;
    }

    public Subtask(Subtask subtask) {
        super(subtask);
        this.epicID = subtask.epicID;
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
