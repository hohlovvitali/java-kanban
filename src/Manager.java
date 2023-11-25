import java.util.ArrayList;
import java.util.HashMap;

class Manager {
    private HashMap<Integer, Task> taskHashMap;
    private HashMap<Integer, Subtask> subtaskHashMap;
    private HashMap<Integer, Epic> epicHashMap;
    private int idCreator = 0;

    public Manager(){
        taskHashMap = new HashMap<>();
        subtaskHashMap = new HashMap<>();
        epicHashMap = new HashMap<>();
    }

    public void addTask(Task task){
        idCreator++;
        task.setTaskID(idCreator);
        taskHashMap.put(idCreator, task);
    }

    public void addSubtask(Subtask subtask) {
        idCreator++;
        subtask.setTaskID(idCreator);
        subtaskHashMap.put(idCreator, subtask);
        epicHashMap.get(subtask.getEpicID()).setSubtaskIDList(idCreator);
        epicHashMap.get(subtask.getEpicID()).checkEpicStatus(subtaskHashMap);
    }

    public void addEpic(Epic epic) {
        idCreator++;
        epic.setTaskID(idCreator);
        epicHashMap.put(idCreator, epic);
    }

    public void updateTask(Task task){
        taskHashMap.put(task.getTaskID(), task);
    }

    public void updateSubtask(Subtask subtask){
        taskHashMap.put(subtask.getTaskID(), subtask);
        epicHashMap.get(subtask.getEpicID()).checkEpicStatus(subtaskHashMap);
    }

    public void updateEpic(Epic epic){
        taskHashMap.put(epic.getTaskID(), epic);
    }

    public Epic getEpicObjectByID(int epicID){
        return epicHashMap.get(epicID);
    }

    public Task getTaskObjectByID(int taskID){
        return taskHashMap.get(taskID);
    }

    public Subtask getSubtaskObjectByID(int subtaskID){
        return subtaskHashMap.get(subtaskID);
    }

    public void deleteAllTasks(){
        taskHashMap.clear();
    }

    public void deleteAllSubtasks(){
        subtaskHashMap.clear();
        for (Epic epic: epicHashMap.values()){
            epic.deleteAllSubtaskID();
        }
    }

    public void deleteAllEpics(){
        epicHashMap.clear();
    }

    public void deleteTask(int taskID){
        taskHashMap.remove(taskID);
    }

    public void deleteSubtask(int taskID){
        epicHashMap.get(subtaskHashMap.get(taskID).getEpicID()).deleteSubtaskID(taskID);
        epicHashMap.get(subtaskHashMap.get(taskID).getEpicID()).checkEpicStatus(subtaskHashMap);
        subtaskHashMap.remove(taskID);
    }

    public void deleteEpic(int taskID){
        epicHashMap.remove(taskID);
    }

    public ArrayList<Task> getAllTasks(){
        return new ArrayList<>(taskHashMap.values());
    }

    public ArrayList<Subtask> getAllSubtasks(){
        return new ArrayList<>(subtaskHashMap.values());
    }

    public ArrayList<Epic> getAllEpics(){
        return new ArrayList<>(epicHashMap.values());
    }
}
