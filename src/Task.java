import java.util.ArrayList;

class Task {
    protected String taskName;
    protected ArrayList<String> taskDescription;
    protected int taskID;
    protected String status;

    public Task(){
    }

    public Task(Task task) {
        this.taskName = task.taskName;
        this.taskDescription = task.taskDescription;
        this.taskID = task.taskID;
        this.status = task.status;
    }

    public Task(String taskName, ArrayList<String> taskDescription) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskID = 0;
        this.status = "NEW";
    }

    protected String getStatus(){
        return status;
    }
    protected void setStatus(String status) {
        if (status.equals("NEW") || status.equals("IN_PROGRESS") || status.equals("DONE")) {
            this.status = status;
            return;
        }
        System.out.println("Некорректный статус");
    }

    protected int getTaskID(){
        return taskID;
    }

    protected void setTaskID(int taskID){
        this.taskID = taskID;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskName='" + taskName + '\'' +
                ",\ntaskDescription=" + taskDescription +
                ",\ntaskID=" + taskID +
                ", status='" + status + '\'' +
                '}';
    }
}
