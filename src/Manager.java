import java.util.ArrayList;
import java.util.HashMap;

class Manager {
    private HashMap<Integer, Task> taskHashMap;
    private HashMap<Integer, Subtask> subtaskHashMap;
    private HashMap<Integer, Epic> epicHashMap;
    private int idCreator = 0;

    Manager(){
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
        if (epicHashMap.containsKey(epicID)){
            return epicHashMap.get(epicID);
        }

        return null;
    }

    public Task getTaskObjectByID(int taskID){
        if (taskHashMap.containsKey(taskID)){
            return taskHashMap.get(taskID);
        }

        return null;
    }

    public Subtask getSubtaskObjectByID(int subtaskID){
        if (subtaskHashMap.containsKey(subtaskID)){
            return subtaskHashMap.get(subtaskID);
        }

        return null;
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


    public String taskHashMapToString() {
        if (taskHashMap.isEmpty())
            return "Пусто";

        String outString = "Manager{" +
                "taskHashMap=" + '\n';

        for (Task task: taskHashMap.values()){
            outString = outString + task.toString() + "\n";
        }

        outString += "}";

        return outString;
    }

    public String subtaskHashMapToString() {
        if (subtaskHashMap.isEmpty())
            return "Пусто";

        String outString = "Manager{" +
                "subtaskHashMap=" + '\n';

        for (Subtask subtask: subtaskHashMap.values()){
            outString = outString + subtask.toString() + "\n";
        }

        outString += "}";

        return outString;
    }

    public String epicHashMapToString() {
        if (epicHashMap.isEmpty())
            return "Пусто";

        String outString = "Manager{" +
                "\nepicHashMap=\n";

        for (Epic epic: epicHashMap.values()){
            outString = outString + epic.toString(subtaskHashMap) + "\n";
        }

        outString += "}";

        return outString;
    }

    public HashMap<Integer, Task> getTaskHashMap() {
        return taskHashMap;
    }

    public HashMap<Integer, Subtask> getSubtaskHashMap() {
        return subtaskHashMap;
    }

    public HashMap<Integer, Epic> getEpicHashMap() {
        return epicHashMap;
    }
}
