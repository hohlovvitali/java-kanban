package manager.taskmanager;

import manager.managerexception.ManagerSaveException;
import tasktype.*;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    void addTask(Task task) throws ManagerSaveException;

    void addSubtask(Subtask subtask) throws ManagerSaveException;

    void addEpic(Epic epic) throws ManagerSaveException;

    void updateTask(Task task) throws ManagerSaveException;

    void updateSubtask(Subtask subtask) throws ManagerSaveException;

    void updateEpic(Epic epic) throws ManagerSaveException;

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
}
