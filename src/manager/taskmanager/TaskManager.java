package manager.taskmanager;

import manager.managerexception.ManagerSaveException;
import manager.managerexception.ManagerTaskNotFoundException;
import manager.managerexception.ManagerValidateException;
import tasks.*;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    void addTask(Task task) throws ManagerSaveException, ManagerValidateException;

    void addSubtask(Subtask subtask) throws ManagerSaveException, ManagerValidateException;

    void addEpic(Epic epic) throws ManagerSaveException;

    void updateTask(Task task) throws ManagerSaveException, ManagerValidateException;

    void updateSubtask(Subtask subtask) throws ManagerSaveException, ManagerValidateException;

    void updateEpic(Epic epic) throws ManagerSaveException;

    Task getTask(int taskID);

    Epic getEpicObjectByID(int epicID) throws ManagerSaveException, ManagerTaskNotFoundException;

    Task getTaskObjectByID(int taskID) throws ManagerSaveException, ManagerTaskNotFoundException;

    Subtask getSubtaskObjectByID(int subtaskID) throws ManagerSaveException, ManagerTaskNotFoundException;

    void deleteAllTasks();

    void deleteAllSubtasks();

    void deleteAllEpics() throws ManagerSaveException, ManagerTaskNotFoundException;

    void deleteTaskById(int taskID) throws ManagerSaveException, ManagerTaskNotFoundException;

    void deleteSubtaskById(int taskID) throws ManagerSaveException, ManagerTaskNotFoundException;

    void deleteEpicById(int taskID) throws ManagerSaveException, ManagerTaskNotFoundException;

    ArrayList<Task> getAllTasks();

    ArrayList<Subtask> getAllSubtasks();

    ArrayList<Epic> getAllEpics();

    List<Task> getHistory();

    boolean equals(Object o);

    List<Task> getPrioritizedTasks();
}
