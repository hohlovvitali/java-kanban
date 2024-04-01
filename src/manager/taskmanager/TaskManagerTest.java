package manager.taskmanager;

import manager.managerexception.ManagerSaveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest <T extends TaskManager>{

    protected T taskManagerTest;

    @Test
    abstract void addTask() throws ManagerSaveException;

    @Test
    abstract void addSubtask() throws ManagerSaveException;

    @Test
    abstract void addEpic() throws ManagerSaveException;

    @Test
    abstract void updateTask() throws ManagerSaveException;

    @Test
    abstract void updateSubtask() throws ManagerSaveException;

    @Test
    abstract void updateEpic() throws ManagerSaveException;

    @Test
    abstract void getEpicObjectByID() throws ManagerSaveException;

    @Test
    abstract void getTaskObjectByID() throws ManagerSaveException;

    @Test
    abstract void getSubtaskObjectByID() throws ManagerSaveException;

    @Test
    abstract void deleteAllTasks() throws ManagerSaveException;

    @Test
    abstract void deleteAllSubtasks() throws ManagerSaveException;

    @Test
    abstract void deleteAllEpics() throws ManagerSaveException;

    @Test
    abstract void deleteTaskById() throws ManagerSaveException;

    @Test
    abstract void deleteSubtaskById() throws ManagerSaveException;

    @Test
    abstract void deleteEpicById() throws ManagerSaveException;

    @Test
    abstract void getAllTasks() throws ManagerSaveException;

    @Test
    abstract void getAllSubtasks() throws ManagerSaveException;

    @Test
    abstract void getAllEpics() throws ManagerSaveException;

    @Test
    abstract void getHistory() throws ManagerSaveException;

    @Test
    abstract void testEquals() throws ManagerSaveException;

    @Test
    protected abstract void getTask() throws ManagerSaveException;
}