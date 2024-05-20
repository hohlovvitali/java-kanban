package manager.taskmanager;

import manager.historymanager.HistoryManager;
import manager.Managers;
import manager.managerexception.ManagerSaveException;
import manager.managerexception.ManagerTaskNotFoundException;
import manager.managerexception.ManagerValidateException;
import tasks.*;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Task> taskHashMap;
    protected final HashMap<Integer, Subtask> subtaskHashMap;
    protected final HashMap<Integer, Epic> epicHashMap;
    protected final ArrayList<Integer> tasksIDList;
    protected final HistoryManager taskMemory = Managers.getDefaultHistory();
    protected final Comparator<Task> taskComparator = (task1, task2) -> {
        if (task1.getStartTime() == null && task2.getStartTime() == null) {
            return Integer.compare(task1.getTaskID(), task2.getTaskID());
        } else if (task2.getStartTime() == null) {
            return -1;
        } else if (task1.getStartTime() == null) {
            return 1;
        }

        return task1.getStartTime().compareTo(task2.getStartTime());
    };
    protected Set<Task> prioritizedTasks = new TreeSet<>(taskComparator);
    private int idCreator = 0;

    public InMemoryTaskManager() {
        taskHashMap = new HashMap<>();
        subtaskHashMap = new HashMap<>();
        epicHashMap = new HashMap<>();
        tasksIDList = new ArrayList<>();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    @Override
    public Task getTask(int taskID) {
        if (taskHashMap.containsKey(taskID)) {
            return taskHashMap.get(taskID);
        } else if (epicHashMap.containsKey(taskID)) {
            return epicHashMap.get(taskID);
        } else {
            return subtaskHashMap.get(taskID);
        }
    }

    @Override
    public void addTask(Task task) throws ManagerSaveException, ManagerValidateException {
        validateTask(task);
        idCreator++;
        task.setTaskID(idCreator);
        taskHashMap.put(idCreator, task);
        prioritizedTasks.add(task);
        tasksIDList.add(idCreator);
    }

    @Override
    public void addSubtask(Subtask subtask) throws ManagerSaveException, ManagerValidateException {
        validateTask(subtask);
        idCreator++;
        subtask.setTaskID(idCreator);
        subtaskHashMap.put(idCreator, subtask);
        prioritizedTasks.add(subtask);
        epicHashMap.get(subtask.getEpicID()).setSubtaskIDList(idCreator);
        epicHashMap.get(subtask.getEpicID()).checkEpicStatus(subtaskHashMap);
        tasksIDList.add(idCreator);
        this.updateTime(epicHashMap.get(subtask.getEpicID()));
    }

    @Override
    public void addEpic(Epic epic) throws ManagerSaveException {
        idCreator++;
        epic.setTaskID(idCreator);
        epicHashMap.put(idCreator, epic);
        tasksIDList.add(idCreator);
    }

    @Override
    public void updateTask(Task task) throws ManagerSaveException, ManagerValidateException {
        validateTask(task);
        taskHashMap.put(task.getTaskID(), task);
    }

    @Override
    public void updateSubtask(Subtask subtask) throws ManagerSaveException, ManagerValidateException {
        validateTask(subtask);
        subtaskHashMap.put(subtask.getTaskID(), subtask);
        epicHashMap.get(subtask.getEpicID()).checkEpicStatus(subtaskHashMap);
        this.updateTime(epicHashMap.get(subtask.getEpicID()));
    }

    @Override
    public void updateEpic(Epic epic) throws ManagerSaveException {
        epicHashMap.put(epic.getTaskID(), epic);
    }

    @Override
    public Epic getEpicObjectByID(int epicID) throws ManagerSaveException, ManagerTaskNotFoundException {
        if (epicHashMap.containsKey(epicID)) {
            taskMemory.add(epicHashMap.get(epicID));
        } else {
            throw new ManagerTaskNotFoundException("Epic not found");
        }

        return epicHashMap.get(epicID);
    }

    @Override
    public Task getTaskObjectByID(int taskID) throws ManagerSaveException, ManagerTaskNotFoundException {
        if (taskHashMap.containsKey(taskID)) {
            taskMemory.add(taskHashMap.get(taskID));
        } else {
            throw new ManagerTaskNotFoundException("Task not found");
        }

        return taskHashMap.get(taskID);
    }

    @Override
    public Subtask getSubtaskObjectByID(int subtaskID) throws ManagerSaveException, ManagerTaskNotFoundException {
        if (subtaskHashMap.containsKey(subtaskID)) {
            taskMemory.add(subtaskHashMap.get(subtaskID));
        } else {
            throw new ManagerTaskNotFoundException("Subtask not found");
        }

        return subtaskHashMap.get(subtaskID);
    }

    @Override
    public void deleteAllTasks() {
        for (Task task : taskHashMap.values()) {
            taskMemory.remove(task.getTaskID());
            removeTaskFromPrioritizedTasks(task.getTaskID());
        }

        taskHashMap.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        for (Subtask subtask : subtaskHashMap.values()) {
            taskMemory.remove(subtask.getTaskID());
            tasksIDList.remove(Integer.valueOf(subtask.getTaskID()));
        }

        subtaskHashMap.clear();
        for (Epic epic : epicHashMap.values()) {
            epic.deleteAllSubtaskID();
        }
    }

    @Override
    public void deleteAllEpics() throws ManagerSaveException, ManagerTaskNotFoundException {
        this.deleteAllSubtasks();

        ArrayList<Epic> epicArrayList = this.getAllEpics();
        for (Epic epic : epicArrayList) {
            this.deleteEpicById(epic.getTaskID());
        }

        epicHashMap.clear();
    }

    @Override
    public void deleteTaskById(int taskID) throws ManagerSaveException, ManagerTaskNotFoundException {
        if (!taskHashMap.containsKey(taskID)) {
            throw new ManagerTaskNotFoundException("Task not Found");
        }

        taskMemory.remove(taskID);
        taskHashMap.remove(taskID);
        tasksIDList.remove(Integer.valueOf(taskID));
        removeTaskFromPrioritizedTasks(taskID);
    }

    @Override
    public void deleteSubtaskById(int taskID) throws ManagerSaveException, ManagerTaskNotFoundException {
        if (!subtaskHashMap.containsKey(taskID)) {
            throw new ManagerTaskNotFoundException("Subtask not Found");
        }

        taskMemory.remove(taskID);
        epicHashMap.get(subtaskHashMap.get(taskID).getEpicID()).deleteSubtaskID(taskID);
        epicHashMap.get(subtaskHashMap.get(taskID).getEpicID()).checkEpicStatus(subtaskHashMap);
        this.updateTime(epicHashMap.get(subtaskHashMap.get(taskID).getEpicID()));
        subtaskHashMap.remove(taskID);
        tasksIDList.remove(Integer.valueOf(taskID));
        removeTaskFromPrioritizedTasks(taskID);
    }

    @Override
    public void deleteEpicById(int taskID) throws ManagerSaveException, ManagerTaskNotFoundException {
        if (!epicHashMap.containsKey(taskID)) {
            throw new ManagerTaskNotFoundException("Epic not Found");
        }

        if (epicHashMap.get(taskID).getSubtaskIDList() != null) {
            ArrayList<Integer> subtaskIDList = new ArrayList<>(epicHashMap.get(taskID).getSubtaskIDList());
            for (Integer subtaskID : subtaskIDList) {
                this.deleteSubtaskById(subtaskID);
            }
        }

        taskMemory.remove(taskID);
        epicHashMap.remove(taskID);
        tasksIDList.remove(Integer.valueOf(taskID));
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        for (Task task : taskHashMap.values()) {
            taskMemory.add(task);
        }

        return new ArrayList<>(taskHashMap.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        for (Subtask subtask : subtaskHashMap.values()) {
            taskMemory.add(subtask);
        }

        return new ArrayList<>(subtaskHashMap.values());
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        for (Epic epic : epicHashMap.values()) {
            taskMemory.add(epic);
        }

        return new ArrayList<>(epicHashMap.values());
    }

    @Override
    public List<Task> getHistory() {
        return taskMemory.getTasks();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;
        InMemoryTaskManager tasksManager = (InMemoryTaskManager) o;
        return this.epicHashMap.equals(tasksManager.epicHashMap) &&
                this.taskHashMap.equals(tasksManager.taskHashMap) &&
                this.subtaskHashMap.equals(tasksManager.subtaskHashMap) &&
                this.getHistory().equals(tasksManager.getHistory());
    }

    public Subtask getSubtaskObjectByIDNotMemory(int subtaskID) {
        return subtaskHashMap.get(subtaskID);
    }

    private void updateTime(Epic epic) throws ManagerSaveException {
        ArrayList<Integer> subtasksID = epic.getSubtaskIDList();

        if (subtasksID.isEmpty()) {
            epic.setStartTime(null);
            epic.setEndTime(null);
            epic.setDuration(Duration.ofMinutes(0));
            this.updateEpic(epic);
            return;
        }

        Instant minStartTime = subtaskHashMap.get(subtasksID.get(0)).getStartTime();
        Instant maxEndTime = subtaskHashMap.get((subtasksID.get(0))).getEndTime();

        for (int i = 1; i < subtasksID.size(); i++) {
            Subtask subtask = subtaskHashMap.get((subtasksID.get(i)));

            if (minStartTime == null) {
                minStartTime = subtask.getStartTime();
                maxEndTime = subtask.getEndTime();
            } else {
                if (subtask.getStartTime() != null) {
                    if (subtask.getStartTime().isBefore(minStartTime)) {
                        minStartTime = subtask.getStartTime();
                    }

                    if (subtask.getEndTime().isAfter(maxEndTime)) {
                        maxEndTime = subtask.getEndTime();
                    }
                }
            }
        }

        epic.setEndTime(maxEndTime);
        epic.setStartTime(minStartTime);
        if (maxEndTime != null && minStartTime != null) {
            epic.setDuration(Duration.ofMinutes(Duration.between(minStartTime, maxEndTime).toMinutes()));
        }

        this.updateEpic(epic);
    }


    private void validateTask(Task taskCheck) throws ManagerValidateException {
        List<Task> tasksPriority = getPrioritizedTasks();

        for (Task task : tasksPriority) {
            if (taskCheck.getTaskID() != task.getTaskID() && taskCheck.getStartTime() != null && task.getStartTime() != null) {
                if (taskCheck.getStartTime().isBefore(task.getEndTime()) && taskCheck.getStartTime().isAfter(task.getStartTime())
                        || taskCheck.getEndTime().isBefore(task.getEndTime()) && taskCheck.getEndTime().isAfter(task.getStartTime())) {
                    throw new ManagerValidateException("Task " + taskCheck.getTaskID() + " and task " +
                            task.getTaskID() + " overlap");
                }

                if (taskCheck.getStartTime().equals(task.getStartTime()) || taskCheck.getEndTime().equals(task.getEndTime())) {
                    throw new ManagerValidateException("Task " + taskCheck.getTaskID() + " and task " +
                            task.getTaskID() + " overlap");
                }
            }
        }
    }


    private void removeTaskFromPrioritizedTasks(int taskID) {
        for (Task task : prioritizedTasks) {
            if (task.getTaskID() == taskID) {
                prioritizedTasks.remove(task);
                return;
            }
        }
    }
}

