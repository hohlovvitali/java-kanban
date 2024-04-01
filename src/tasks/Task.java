package tasks;

public class Task {
    protected String taskName;
    protected String taskDescription;
    protected int taskID;
    protected TaskStatus status;

    public Task(){
    }

    public Task(Task task) {
        this.taskName = task.taskName;
        this.taskDescription = task.taskDescription;
        this.taskID = task.taskID;
        this.status = task.status;
    }

    public Task(String taskName, String taskDescription) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.status = TaskStatus.NEW;
    }

    public Task(int taskID, String taskName, TaskStatus taskStatus, String taskDescription) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskID = taskID;
        this.status = taskStatus;
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
        if (this.getClass() == Epic.class){
            return TaskType.EPIC;
        } else if (this.getClass() == Subtask.class) {
            return TaskType.SUBTASK;
        } else {
            return TaskType.TASK;
        }
    }
    public void setTaskID(int taskID){
        this.taskID = taskID;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return this.taskName.equals(task.taskName) && this.taskDescription.equals(task.taskDescription) &&
                this.taskID == task.getTaskID() && this.status == task.status;
    }

    @Override
    public String toString() {
        return taskID + "," + this.getTaskType() + "," + taskName + "," + status + "," + taskDescription;
    }

    public TaskStatus getStatus(){
        return status;
    }
}
