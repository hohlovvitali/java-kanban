package manager.taskmanager;

import manager.historymanager.HistoryManager;
import manager.Managers;
import manager.historymanager.InMemoryHistoryManager;
import manager.managerexception.ManagerSaveException;
import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Task> taskHashMap;
    protected final HashMap<Integer, Subtask> subtaskHashMap;
    protected final HashMap<Integer, Epic> epicHashMap;
    protected final ArrayList<Integer> tasksIDList;
    protected final HistoryManager taskMemory = Managers.getDefaultHistory();
    private int idCreator = 0;

    public InMemoryTaskManager(){
        taskHashMap = new HashMap<>();
        subtaskHashMap = new HashMap<>();
        epicHashMap = new HashMap<>();
        tasksIDList = new ArrayList<>();
    }
    @Override
    public Task getTask(int taskID){
        if (taskHashMap.containsKey(taskID)){
            return taskHashMap.get(taskID);
        } else if (epicHashMap.containsKey(taskID)) {
            return epicHashMap.get(taskID);
        } else {
            return subtaskHashMap.get(taskID);
        }
    }

    @Override
    public void addTask(Task task) throws ManagerSaveException {
        idCreator++;
        task.setTaskID(idCreator);
        taskHashMap.put(idCreator, task);
        tasksIDList.add(idCreator);
    }

    @Override
    public void addSubtask(Subtask subtask) throws ManagerSaveException {
        idCreator++;
        subtask.setTaskID(idCreator);
        subtaskHashMap.put(idCreator, subtask);
        epicHashMap.get(subtask.getEpicID()).setSubtaskIDList(idCreator);
        epicHashMap.get(subtask.getEpicID()).checkEpicStatus(subtaskHashMap);
        tasksIDList.add(idCreator);
    }

    @Override
    public void addEpic(Epic epic) throws ManagerSaveException {
        idCreator++;
        epic.setTaskID(idCreator);
        epicHashMap.put(idCreator, epic);
        tasksIDList.add(idCreator);
    }

    @Override
    public void updateTask(Task task) throws ManagerSaveException {
        taskHashMap.put(task.getTaskID(), task);
    }

    @Override
    public void updateSubtask(Subtask subtask) throws ManagerSaveException {
        subtaskHashMap.put(subtask.getTaskID(), subtask);
        epicHashMap.get(subtask.getEpicID()).checkEpicStatus(subtaskHashMap);
    }

    @Override
    public void updateEpic(Epic epic) throws ManagerSaveException {
        epicHashMap.put(epic.getTaskID(), epic);
    }

    @Override
    public Epic getEpicObjectByID(int epicID) throws ManagerSaveException {
        if (epicHashMap.containsKey(epicID)){
            taskMemory.add(epicHashMap.get(epicID));
        }
        return epicHashMap.get(epicID);
    }

    @Override
    public Task getTaskObjectByID(int taskID) throws ManagerSaveException {
        if (taskHashMap.containsKey(taskID)){
            taskMemory.add(taskHashMap.get(taskID));
        }

        return taskHashMap.get(taskID);
    }

    @Override
    public Subtask getSubtaskObjectByID(int subtaskID) throws ManagerSaveException {
        if (subtaskHashMap.containsKey(subtaskID)){
            taskMemory.add(subtaskHashMap.get(subtaskID));
        }

        return subtaskHashMap.get(subtaskID);
    }

    public Subtask getSubtaskObjectByIDNotMemory(int subtaskID){
        return subtaskHashMap.get(subtaskID);
    }

    @Override
    public void deleteAllTasks(){
        for (Task task: taskHashMap.values()){
            taskMemory.remove(task.getTaskID());
        }

        taskHashMap.clear();
    }

    @Override
    public void deleteAllSubtasks(){
        for (Subtask subtask: subtaskHashMap.values()){
            taskMemory.remove(subtask.getTaskID());
        }

        subtaskHashMap.clear();
        for (Epic epic: epicHashMap.values()){
            epic.deleteAllSubtaskID();
        }
    }

    @Override
    public void deleteAllEpics() throws ManagerSaveException {
        this.deleteAllSubtasks();

        ArrayList<Epic> epicArrayList = this.getAllEpics();
        for (Epic epic: epicArrayList){
            this.deleteEpicById(epic.getTaskID());
        }

        epicHashMap.clear();
    }

    @Override
    public void deleteTaskById(int taskID) throws ManagerSaveException {
        taskMemory.remove(taskID);
        taskHashMap.remove(taskID);
        tasksIDList.remove(Integer.valueOf(taskID));
    }

    @Override
    public void deleteSubtaskById(int taskID) throws ManagerSaveException {
        taskMemory.remove(taskID);
        epicHashMap.get(subtaskHashMap.get(taskID).getEpicID()).deleteSubtaskID(taskID);
        epicHashMap.get(subtaskHashMap.get(taskID).getEpicID()).checkEpicStatus(subtaskHashMap);
        subtaskHashMap.remove(taskID);
        tasksIDList.remove(Integer.valueOf(taskID));
    }

    @Override
    public void deleteEpicById(int taskID) throws ManagerSaveException {
        if (!epicHashMap.containsKey(taskID)){
            return;
        }
        ArrayList<Integer> subtaskIDList = new ArrayList<>(epicHashMap.get(taskID).getSubtaskIDList());
        for (Integer subtaskID: subtaskIDList){
            this.deleteSubtaskById(subtaskID);
        }

        taskMemory.remove(Integer.valueOf(taskID));
        epicHashMap.remove(Integer.valueOf(taskID));
        tasksIDList.remove(Integer.valueOf(taskID));
    }

    @Override
    public ArrayList<Task> getAllTasks(){
        return new ArrayList<>(taskHashMap.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks(){
        return new ArrayList<>(subtaskHashMap.values());
    }

    @Override
    public ArrayList<Epic> getAllEpics(){
        return new ArrayList<>(epicHashMap.values());
    }

    @Override
    public List<Task> getHistory(){
        return taskMemory.getTasks();
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;
        InMemoryTaskManager tasksManager = (InMemoryTaskManager) o;
        return this.epicHashMap.equals(tasksManager.epicHashMap) &&
                this.taskHashMap.equals(tasksManager.taskHashMap) &&
                this.subtaskHashMap.equals(tasksManager.subtaskHashMap) &&
                this.getHistory().equals(tasksManager.getHistory());
    }

}

