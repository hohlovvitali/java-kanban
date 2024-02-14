package manager.taskmanager;

import tasktype.*;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    void addTask(Task task);

    void addSubtask(Subtask subtask);

    void addEpic(Epic epic);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    Epic getEpicObjectByID(int epicID);

    Task getTaskObjectByID(int taskID);

    Subtask getSubtaskObjectByID(int subtaskID);

    void deleteAllTasks();

    void deleteAllSubtasks();

    void deleteAllEpics();

    void deleteTaskById(int taskID);

    void deleteSubtaskById(int taskID);

    void deleteEpicById(int taskID);

    ArrayList<Task> getAllTasks();

    ArrayList<Subtask> getAllSubtasks();

    ArrayList<Epic> getAllEpics();

    List<Task> getHistory();
}
