package manager.taskmanager;

import manager.historymanager.HistoryManager;
import manager.Managers;
import tasktype.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> taskHashMap;
    private final HashMap<Integer, Subtask> subtaskHashMap;
    private final HashMap<Integer, Epic> epicHashMap;
    private final HistoryManager taskMemory = Managers.getDefaultHistory();
    private int idCreator = 0;

    public InMemoryTaskManager(){
        taskHashMap = new HashMap<>();
        subtaskHashMap = new HashMap<>();
        epicHashMap = new HashMap<>();
    }

    @Override
    public void addTask(Task task){
        idCreator++;
        task.setTaskID(idCreator);
        taskHashMap.put(idCreator, task);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        idCreator++;
        subtask.setTaskID(idCreator);
        subtaskHashMap.put(idCreator, subtask);
        epicHashMap.get(subtask.getEpicID()).setSubtaskIDList(idCreator);
        epicHashMap.get(subtask.getEpicID()).checkEpicStatus(subtaskHashMap);
    }

    @Override
    public void addEpic(Epic epic) {
        idCreator++;
        epic.setTaskID(idCreator);
        epicHashMap.put(idCreator, epic);
    }

    @Override
    public void updateTask(Task task){
        taskHashMap.put(task.getTaskID(), task);
    }

    @Override
    public void updateSubtask(Subtask subtask){
        taskHashMap.put(subtask.getTaskID(), subtask);
        epicHashMap.get(subtask.getEpicID()).checkEpicStatus(subtaskHashMap);
    }

    @Override
    public void updateEpic(Epic epic){
        taskHashMap.put(epic.getTaskID(), epic);
    }

    @Override
    public Epic getEpicObjectByID(int epicID){
        if (epicHashMap.containsKey(epicID)){
            taskMemory.add(epicHashMap.get(epicID));
        }
        return epicHashMap.get(epicID);
    }

    @Override
    public Task getTaskObjectByID(int taskID){
        if (taskHashMap.containsKey(taskID)){
            taskMemory.add(taskHashMap.get(taskID));
        }

        return taskHashMap.get(taskID);
    }

    @Override
    public Subtask getSubtaskObjectByID(int subtaskID){
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
    public void deleteAllEpics(){
        this.deleteAllSubtasks();

        for (Epic epic: epicHashMap.values()){
            this.deleteEpicById(epic.getTaskID());
        }

        epicHashMap.clear();
    }

    @Override
    public void deleteTaskById(int taskID){
        taskMemory.remove(taskID);
        taskHashMap.remove(taskID);
    }

    @Override
    public void deleteSubtaskById(int taskID){
        taskMemory.remove(taskID);
        epicHashMap.get(subtaskHashMap.get(taskID).getEpicID()).deleteSubtaskID(taskID);
        epicHashMap.get(subtaskHashMap.get(taskID).getEpicID()).checkEpicStatus(subtaskHashMap);
        subtaskHashMap.remove(taskID);
    }

    @Override
    public void deleteEpicById(int taskID){
        if (!epicHashMap.containsKey(taskID)){
            return;
        }
        ArrayList<Integer> subtaskIDList = new ArrayList<>(epicHashMap.get(taskID).getSubtaskIDList());
        for (Integer subtaskID: subtaskIDList){
            this.deleteSubtaskById(subtaskID);
        }

        taskMemory.remove(taskID);
        epicHashMap.remove(taskID);
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
}

