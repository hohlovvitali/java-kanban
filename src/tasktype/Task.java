package tasktype;

public class Task {
    protected String taskName;
    protected String taskDescription;
    protected int taskID;
    protected TaskStatus status;

    protected TaskType taskType;

    public Task(){
    }

    public Task(Task task) {
        this.taskName = task.taskName;
        this.taskDescription = task.taskDescription;
        this.taskID = task.taskID;
        this.status = task.status;
        this.taskType = task.taskType;
    }

    public Task(String taskName, String taskDescription) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.status = TaskStatus.NEW;
        this.taskType = TaskType.TASK;
    }

    public Task(int taskID, TaskType taskType, String taskName, TaskStatus taskStatus, String taskDescription) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskID = taskID;
        this.status = taskStatus;
        this.taskType = taskType;
    }

    protected TaskStatus getStatus(){
        return status;
    }

    public void setStatus(TaskStatus status) {
        if (status == TaskStatus.NEW || status == TaskStatus.IN_PROGRESS || status == TaskStatus.DONE) {
            this.status = status;
            return;
        }
        System.out.println("Некорректный статус");
    }

    public int getTaskID(){
        return taskID;
    }

    public TaskType getTaskType() {
        return taskType;
    }
    public void setTaskID(int taskID){
        this.taskID = taskID;
    }

    @Override
    public String toString() {
        return taskID + "," + taskType + "," + taskName + "," + status + "," + taskDescription;
    }
}
