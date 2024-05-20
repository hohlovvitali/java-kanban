package tasks;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {
    private ArrayList<Integer> subtaskIDList;
    private Instant endTime;

    public Epic(int taskID, String taskName, String taskDescription) {
        super(taskID, taskName, TaskStatus.NEW, taskDescription);
        this.subtaskIDList = new ArrayList<>();
    }

    public Epic(int taskID, String taskName, TaskStatus taskStatus, String taskDescription) {
        super(taskID, taskName, taskStatus, taskDescription);
        this.endTime = null;
        this.subtaskIDList = new ArrayList<>();
    }

    public Epic(int taskID, String taskName, TaskStatus taskStatus, String taskDescription, Instant startTime, Instant endTime) {
        super(taskID, taskName, taskStatus, taskDescription, startTime, endTime);
        this.endTime = endTime;
        this.subtaskIDList = new ArrayList<>();
    }

    public Epic(String taskName, TaskStatus taskStatus, String taskDescription) {
        super();
        this.taskName = taskName;
        this.status = taskStatus;
        this.taskDescription = taskDescription;
    }

    @Override
    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public ArrayList<Integer> getSubtaskIDList() {
        return subtaskIDList;
    }

    public void setSubtaskIDList(Integer subtaskID) {
        if (this.subtaskIDList == null) {
            this.subtaskIDList = new ArrayList<>();
        }

        this.subtaskIDList.add(subtaskID);
    }

    public void checkEpicStatus(HashMap<Integer, Subtask> subtaskHashMap) {
        if (subtaskIDList.isEmpty()) {
            this.status = TaskStatus.NEW;
            return;
        }

        for (Integer subtaskID : subtaskIDList) {
            if (subtaskHashMap.get(subtaskID).getStatus() != TaskStatus.NEW) {
                this.status = TaskStatus.IN_PROGRESS;
                break;
            }
        }

        for (Integer subtaskID : subtaskIDList) {
            if (subtaskHashMap.get(subtaskID).getStatus() != TaskStatus.DONE) {
                return;
            }
        }

        this.status = TaskStatus.DONE;
    }

    public void deleteAllSubtaskID() {
        this.subtaskIDList.clear();
    }

    public void deleteSubtaskID(int subtaskID) {
        this.subtaskIDList.remove(subtaskIDList.indexOf(subtaskID));
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        String output = taskID + "," + this.getTaskType() + "," + taskName + "," + status + "," + taskDescription;
        if (startTime != null) {
            output += "," + LocalDateTime.ofInstant(startTime, ZoneOffset.UTC).format(formatter);
        }

        if (endTime != null) {
            output += "," + LocalDateTime.ofInstant(endTime, ZoneOffset.UTC).format(formatter);
        }
        return output;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;
        Epic task = (Epic) o;
        boolean nullStartTimeTrue = this.startTime == null && task.startTime == null;
        boolean nullDurationTrue = this.duration == null && task.duration == null;
        boolean nullEndTimeTrue = this.endTime == null && task.endTime == null;
        return this.taskName.equals(task.taskName) && this.taskDescription.equals(task.taskDescription) &&
                this.taskID == task.getTaskID() && this.status == task.status &&
                (nullStartTimeTrue || this.startTime.equals(task.startTime)) &&
                (nullDurationTrue || this.duration.equals(task.duration)) && this.subtaskIDList.equals(task.getSubtaskIDList()) &&
                (nullEndTimeTrue || this.endTime.equals(task.endTime));
    }
}
