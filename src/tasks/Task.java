package tasks;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Task {
    protected String taskName;
    protected String taskDescription;
    protected int taskID;
    protected TaskStatus status;
    protected Instant startTime;
    protected Duration duration;


    public Task(String taskName, String taskDescription) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.status = TaskStatus.NEW;
        this.startTime = null;
        this.duration = Duration.ofMinutes(0);
    }

    public Task(int taskID, String taskName, TaskStatus taskStatus, String taskDescription, Instant startTime, String duration) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskID = taskID;
        this.status = taskStatus;
        this.startTime = startTime;
        this.duration = Duration.parse(duration);
    }

    public Task(int taskID, String taskName, TaskStatus taskStatus, String taskDescription, Instant startTime, Instant endTime) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskID = taskID;
        this.status = taskStatus;
        this.startTime = startTime;
        this.duration = Duration.ofMinutes(Duration.between(startTime, endTime).toMinutes());
    }

    public Task(int taskID, String taskName, TaskStatus taskStatus, String taskDescription) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskID = taskID;
        this.status = taskStatus;
        this.startTime = null;
        this.duration = Duration.ofMinutes(0);
    }

    public Task(String taskName, TaskStatus taskStatus, String taskDescription, Instant instant, Duration duration) {
        this.taskName = taskName;
        this.status = taskStatus;
        this.taskDescription = taskDescription;
        this.startTime = instant;
        this.duration = duration;
    }

    public Task() {

    }

    public void setStatus(TaskStatus status) {
        if (status == TaskStatus.NEW || status == TaskStatus.IN_PROGRESS || status == TaskStatus.DONE) {
            this.status = status;
        }
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

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = Duration.ofMinutes(duration.toMinutes());
    }

    public Instant getEndTime(){
        if (startTime == null){
            return null;
        }

        return startTime.plusSeconds(duration.toSeconds());
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;
        Task task = (Task) o;
        boolean nullStartTimeTrue = this.startTime == null && task.startTime == null;
        boolean nullDurationTrue = this.duration == null && task.duration == null;
        return this.taskName.equals(task.taskName) && this.taskDescription.equals(task.taskDescription) &&
                this.taskID == task.getTaskID() && this.status == task.status &&
                (nullStartTimeTrue || this.startTime.equals(task.startTime)) &&
                (nullDurationTrue || this.duration.equals(task.duration));
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        String output = taskID + "," + this.getTaskType() + "," + taskName + "," + status + "," + taskDescription;
        if (startTime != null){
            output += "," + LocalDateTime.ofInstant(startTime, ZoneOffset.UTC).format(formatter);
        }

//        if (duration != Duration.ofMinutes(0) && duration != null){
//            output += "," + LocalDateTime.ofInstant(getEndTime(), ZoneOffset.UTC).format(formatter);
//        }
        if (duration != Duration.ofMinutes(0) && duration != null){
            output += "," + duration;
        }

        return output;
    }

    public TaskStatus getStatus(){
        return status;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
