package manager.taskmanager;

import manager.managerexception.ManagerSaveException;
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

    Epic getEpicObjectByID(int epicID) throws ManagerSaveException;

    Task getTaskObjectByID(int taskID) throws ManagerSaveException;

    Subtask getSubtaskObjectByID(int subtaskID) throws ManagerSaveException;

    void deleteAllTasks();

    void deleteAllSubtasks();

    void deleteAllEpics() throws ManagerSaveException;

    void deleteTaskById(int taskID) throws ManagerSaveException;

    void deleteSubtaskById(int taskID) throws ManagerSaveException;

    void deleteEpicById(int taskID) throws ManagerSaveException;

    ArrayList<Task> getAllTasks();

    ArrayList<Subtask> getAllSubtasks();

    ArrayList<Epic> getAllEpics();

    List<Task> getHistory();

    boolean equals(Object o);
}
