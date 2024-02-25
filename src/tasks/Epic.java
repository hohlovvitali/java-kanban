package tasks;

import manager.taskmanager.InMemoryTaskManager;

import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {
    private ArrayList<Integer> subtaskIDList;

    public Epic(String taskName) {
        this.taskName = taskName;
        this.subtaskIDList = new ArrayList<>();
        this.status = TaskStatus.NEW;
        this.taskID = 0;
    }

    public Epic(Epic epic) {
        this.taskName = epic.taskName;
        this.status = epic.status;
        this.taskID = epic.taskID;
    }

    public Epic(int taskID, String taskName, TaskStatus taskStatus, String taskDescription) {
        super(taskID, taskName, taskStatus, taskDescription);
        this.subtaskIDList = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtaskIDList() {
        return subtaskIDList;
    }

    public void setSubtaskIDList(Integer subtaskID) {
        this.subtaskIDList.add(subtaskID);
    }

    public void checkEpicStatus(HashMap<Integer, Subtask> subtaskHashMap){
        if (subtaskIDList.isEmpty()){
            this.status = TaskStatus.NEW;
            return;
        }

        for (Integer subtaskID: subtaskIDList){
            if (subtaskHashMap.get(subtaskID).getStatus() != TaskStatus.NEW) {
                this.status = TaskStatus.IN_PROGRESS;
                break;
            }
        }

        for (Integer subtaskID: subtaskIDList){
            if (subtaskHashMap.get(subtaskID).getStatus() != TaskStatus.DONE) {
                return;
            }
        }

        this.status = TaskStatus.DONE;
    }

    public void deleteAllSubtaskID(){
        this.subtaskIDList.clear();
    }

    public void deleteSubtaskID(int subtaskID){
        this.subtaskIDList.remove(subtaskIDList.indexOf(subtaskID));
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public String toString(InMemoryTaskManager manager) {
        String outString = "task.Epic{" +
                " taskName='" + taskName + '\'' +
                ", taskID=" + taskID +
                ", status='" + status + '\'' + '\n';
        for (Integer subtaskID: subtaskIDList){
            outString = outString + manager.getSubtaskObjectByIDNotMemory(subtaskID).toString();
        }

        outString += "}\n";

        return outString;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;
        Epic task = (Epic) o;
        return this.taskName.equals(task.taskName) && this.taskID == task.getTaskID() && this.status == task.status &&
                this.subtaskIDList.equals(task.getSubtaskIDList());
    }
}
