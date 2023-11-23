import java.util.ArrayList;
import java.util.HashMap;

class Epic extends Task{
    private ArrayList<Integer> subtaskIDList;

    public Epic(String taskName) {
        this.taskName = taskName;
        this.subtaskIDList = new ArrayList<>();
        this.status = "NEW";
        this.taskID = 0;
    }

    public Epic(Epic epic) {
        this.taskName = epic.taskName;
        this.status = epic.status;
        this.taskID = epic.taskID;
    }

    public ArrayList<Integer> getSubtaskIDList() {
        return subtaskIDList;
    }

    public void setSubtaskIDList(Integer subtaskID) {
        this.subtaskIDList.add(subtaskID);
    }

    public void checkEpicStatus(HashMap<Integer, Subtask> subtaskHashMap){
        if (subtaskIDList.isEmpty()){
            this.status = "NEW";
            return;
        }

        for (Integer subtaskID: subtaskIDList){
            if (!subtaskHashMap.get(subtaskID).getStatus().equals("NEW")) {
                this.status = "IN_PROGRESS";
                break;
            }
        }

        for (Integer subtaskID: subtaskIDList){
            if (!subtaskHashMap.get(subtaskID).getStatus().equals("DONE")) {
                return;
            }
        }

        this.status = "DONE";
    }

    public void deleteAllSubtaskID(){
        this.subtaskIDList.clear();
    }

    public void deleteSubtaskID(int subtaskID){
        this.subtaskIDList.remove(subtaskIDList.indexOf(subtaskID));
    }


    public String toString(HashMap<Integer, Subtask> subtaskHashMap) {
        String outString = "Epic{" +
                " taskName='" + taskName + '\'' +
                ", taskID=" + taskID +
                ", status='" + status + '\'' + '\n';
        for (Integer subtaskID: subtaskIDList){
            outString = outString + subtaskHashMap.get(subtaskID).toString() + "\n";
        }

        outString += "}";

        return outString;
    }
}
