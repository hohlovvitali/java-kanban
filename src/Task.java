class Task {
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

    protected TaskStatus getStatus(){
        return status;
    }
    protected void setStatus(TaskStatus status) {
        if (status == TaskStatus.NEW || status == TaskStatus.IN_PROGRESS || status == TaskStatus.DONE) {
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
                "}\n";
    }
}
