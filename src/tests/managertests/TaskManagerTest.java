package tests.managertests;

import manager.managerexception.ManagerSaveException;
import manager.managerexception.ManagerTaskNotFoundException;
import manager.managerexception.ManagerValidateException;
import manager.taskmanager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest <T extends TaskManager>{

    protected T taskManagerTest;

    protected static void fillManager(TaskManager taskManagerTest) throws ManagerSaveException, ManagerValidateException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");

        Task task1 = new Task(0, "task1Test", TaskStatus.NEW, "Test task1Test description",
                LocalDateTime.parse("2015.05.22 12:00", formatter).toInstant(ZoneOffset.UTC),
                LocalDateTime.parse("2015.05.22 12:15", formatter).toInstant(ZoneOffset.UTC));
        taskManagerTest.addTask(task1);
        Task task2 = new Task(0, "task2Test", TaskStatus.NEW, "Test task2Test description");
        taskManagerTest.addTask(task2);

        Epic epic1 = new Epic(0,"epic1Test", "Test epic1Test description");
        Epic epic2 = new Epic(0,"epic2Test", "Test epic2Test description");
        taskManagerTest.addEpic(epic1);
        taskManagerTest.addEpic(epic2);

        Subtask subtask1 = new Subtask(0, "subtask1Test", TaskStatus.NEW, "Test subtask1Test description",
                LocalDateTime.parse("2015.05.23 15:00", formatter).toInstant(ZoneOffset.UTC),
                LocalDateTime.parse("2015.05.23 15:15", formatter).toInstant(ZoneOffset.UTC), 3);
        Subtask subtask2 = new Subtask(0, "subtask2Test", TaskStatus.NEW, "Test subtask2Test description", 3);
        Subtask subtask3 = new Subtask(0, "subtask3Test", TaskStatus.NEW, "Test subtask3Test description",
                LocalDateTime.parse("2015.05.23 16:00", formatter).toInstant(ZoneOffset.UTC),
                LocalDateTime.parse("2015.05.23 16:30", formatter).toInstant(ZoneOffset.UTC), 3);
        Subtask subtask4 = new Subtask(0, "subtask3Test", TaskStatus.NEW, "Test subtask3Test description",
                LocalDateTime.parse("2015.05.23 13:00", formatter).toInstant(ZoneOffset.UTC),
                LocalDateTime.parse("2015.05.23 13:30", formatter).toInstant(ZoneOffset.UTC), 3);
        taskManagerTest.addSubtask(subtask1);
        taskManagerTest.addSubtask(subtask2);
        taskManagerTest.addSubtask(subtask3);
        taskManagerTest.addSubtask(subtask4);
    }

    @Test
    protected void addTask() throws ManagerSaveException, ManagerValidateException, ManagerTaskNotFoundException {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        taskManagerTest.addTask(task);

        final Task savedTask = taskManagerTest.getTaskObjectByID(1);

        assertNotNull(savedTask, "task not found");
        assertEquals(task, savedTask, "The tasks are not the same.");

        final List<Task> tasks = taskManagerTest.getAllTasks();

        assertNotNull(tasks, "The tasks are not returned.");
        assertEquals(1, tasks.size(), "The number of tasks is incorrect.");
        assertEquals(task, tasks.get(0), "The tasks are not the same.");
    }

    @Test
    protected void addEpic() throws ManagerSaveException, ManagerTaskNotFoundException {
        Epic epic = new Epic(0,"Test addNewEpic", "Test addNewEpic description");
        taskManagerTest.addEpic(epic);

        final Epic savedEpic = taskManagerTest.getEpicObjectByID(1);

        assertNotNull(savedEpic, "epic not found");
        assertEquals(epic, savedEpic, "The epics are not the same.");

        final List<Epic> epics = taskManagerTest.getAllEpics();

        assertNotNull(epics, "The epics are not returned.");
        assertEquals(1, epics.size(), "The number of epics is incorrect.");
        assertEquals(epic, epics.get(0), "The epics are not the same.");
    }

    @Test
    protected void addEpicWithSubtask() throws ManagerSaveException, ManagerTaskNotFoundException {
        Epic epic = new Epic(0,"Test addNewEpic", "Test addNewEpic description");
        taskManagerTest.addEpic(epic);

        final Epic savedEpic = taskManagerTest.getEpicObjectByID(1);

        assertNotNull(savedEpic, "epic not found");
        assertEquals(epic, savedEpic, "The epics are not the same.");

        final List<Epic> epics = taskManagerTest.getAllEpics();

        assertNotNull(epics, "The epics are not returned.");
        assertEquals(1, epics.size(), "The number of epics is incorrect.");
        assertEquals(epic, epics.get(0), "The epics are not the same.");
    }

    @Test
    void addSubtask() throws ManagerSaveException, ManagerValidateException, ManagerTaskNotFoundException {
        Epic epic = new Epic(0,"Test addNewEpic", "Test addNewEpic description");
        taskManagerTest.addEpic(epic);

        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description", 1);
        taskManagerTest.addSubtask(subtask);

        final Subtask savedSubtask = taskManagerTest.getSubtaskObjectByID(2);

        assertNotNull(savedSubtask, "subtask not found");
        assertEquals(subtask, savedSubtask, "The subtasks are not the same.");

        final List<Subtask> subtasks = taskManagerTest.getAllSubtasks();

        assertNotNull(subtasks, "The subtasks are not returned.");
        assertEquals(1, subtasks.size(), "The number of subtasks is incorrect.");
        assertEquals(subtask, subtasks.get(0), "The subtasks are not the same.");
    }

    @Test
    protected  void updateTask() throws ManagerSaveException, ManagerValidateException, ManagerTaskNotFoundException {
        InMemoryTaskManagerTest.fillManager(taskManagerTest);

        Task savedTask = taskManagerTest.getTaskObjectByID(1);
        assertNotNull(savedTask, "Task not found");
        savedTask.setStatus(TaskStatus.IN_PROGRESS);
        taskManagerTest.updateTask(savedTask);
        assertEquals(taskManagerTest.getTaskObjectByID(1).getStatus(), TaskStatus.IN_PROGRESS, "TaskStatus not updated");
    }

    @Test
    protected  void updateSubtask() throws ManagerSaveException, ManagerValidateException, ManagerTaskNotFoundException {
        InMemoryTaskManagerTest.fillManager(taskManagerTest);

        Subtask savedSubtask = taskManagerTest.getSubtaskObjectByID(5);
        assertNotNull(savedSubtask, "Subtask not found");
        savedSubtask.setStatus(TaskStatus.DONE);
        taskManagerTest.updateSubtask(savedSubtask);
        assertEquals(taskManagerTest.getSubtaskObjectByID(5).getStatus(), TaskStatus.DONE, "SubtaskStatus not updated");
    }

    @Test
    protected  void updateEpic() throws ManagerSaveException, ManagerValidateException, ManagerTaskNotFoundException {
        InMemoryTaskManagerTest.fillManager(taskManagerTest);

        Subtask savedSubtask = taskManagerTest.getSubtaskObjectByID(5);
        assertNotNull(savedSubtask, "Subtask not found");
        savedSubtask.setStatus(TaskStatus.DONE);
        taskManagerTest.updateSubtask(savedSubtask);

        Epic savedEpic = taskManagerTest.getEpicObjectByID(3);
        assertNotNull(savedEpic, "Epic not found");
        taskManagerTest.updateEpic(savedEpic);
        assertEquals(taskManagerTest.getEpicObjectByID(3).getStatus(), TaskStatus.IN_PROGRESS, "EpicStatus not updated");
    }

    @Test
    protected void getTaskObjectByID() throws ManagerSaveException, ManagerValidateException, ManagerTaskNotFoundException {
        InMemoryTaskManagerTest.fillManager(taskManagerTest);

        ManagerTaskNotFoundException ex = Assertions.assertThrows(
                ManagerTaskNotFoundException.class,
                generateTaskNotFoundExecutable(5)
        );

        Assertions.assertEquals("Task not found", ex.getMessage());

        Task savedTask = taskManagerTest.getTaskObjectByID(1);
        assertNotNull(savedTask, "Task not found");

        final List<Task> tasks = taskManagerTest.getAllTasks();

        assertNotNull(tasks, "The tasks are not returned.");
        assertEquals(2, tasks.size(), "The number of tasks is incorrect.");
        assertEquals(savedTask, tasks.get(0), "The tasks are not the same.");
    }

    @Test
    protected void getSubtaskObjectByID() throws ManagerSaveException, ManagerValidateException, ManagerTaskNotFoundException {
        InMemoryTaskManagerTest.fillManager(taskManagerTest);

        ManagerTaskNotFoundException ex = Assertions.assertThrows(
                ManagerTaskNotFoundException.class,
                generateSubtaskNotFoundExecutable(1)
        );

        Assertions.assertEquals("Subtask not found", ex.getMessage());

        Subtask savedSubtask = taskManagerTest.getSubtaskObjectByID(5);
        assertNotNull(savedSubtask, "Subtask not found");

        final List<Subtask> subtasks = taskManagerTest.getAllSubtasks();

        assertNotNull(subtasks, "The subtasks are not returned.");
        assertEquals(4, subtasks.size(), "The number of subtasks is incorrect.");
        assertEquals(savedSubtask, subtasks.get(0), "The subtasks are not the same.");


    }

    @Test
    protected void getEpicObjectByID() throws ManagerSaveException, ManagerValidateException, ManagerTaskNotFoundException {
        InMemoryTaskManagerTest.fillManager(taskManagerTest);

        ManagerTaskNotFoundException ex = Assertions.assertThrows(
                ManagerTaskNotFoundException.class,
                generateEpicNotFoundExecutable(6)
        );

        Assertions.assertEquals("Epic not found", ex.getMessage());

        Epic savedEpic = taskManagerTest.getEpicObjectByID(3);
        assertNotNull(savedEpic, "Epic not found");

        final List<Epic> epics = taskManagerTest.getAllEpics();

        assertNotNull(epics, "The epics are not returned.");
        assertEquals(2, epics.size(), "The number of epics is incorrect.");
        assertEquals(savedEpic, epics.get(0), "The epics are not the same.");
    }

    @Test
    protected void deleteAllTasks() throws ManagerSaveException, ManagerValidateException {
        InMemoryTaskManagerTest.fillManager(taskManagerTest);

        taskManagerTest.deleteAllTasks();
        assertTrue(taskManagerTest.getAllTasks().isEmpty(), "Tasks are not deleted");
    }

    @Test
    protected void deleteAllSubtasks() throws ManagerSaveException, ManagerValidateException {
        InMemoryTaskManagerTest.fillManager(taskManagerTest);

        taskManagerTest.deleteAllSubtasks();
        assertTrue(taskManagerTest.getAllSubtasks().isEmpty(), "Subtasks are not deleted");
    }

    @Test
    protected void deleteAllEpics() throws ManagerSaveException, ManagerValidateException, ManagerTaskNotFoundException {
        InMemoryTaskManagerTest.fillManager( taskManagerTest);

        taskManagerTest.deleteAllEpics();
        assertTrue(taskManagerTest.getAllEpics().isEmpty(), "Epics are not deleted");
    }

    @Test
    protected void deleteTaskById() throws ManagerSaveException, ManagerValidateException, ManagerTaskNotFoundException {
        InMemoryTaskManagerTest.fillManager( taskManagerTest);

        Task savedTask = taskManagerTest.getTaskObjectByID(1);
        assertNotNull(savedTask, "Task not found");

        ArrayList<Task> history = (ArrayList<Task>) taskManagerTest.getHistory();

        assertNotNull(history, "The epics are not returned.");
        assertEquals(1, history.size(), "The number of epics is incorrect.");
        assertEquals(savedTask, history.get(0), "The epics are not the same.");

        taskManagerTest.deleteTaskById(savedTask.getTaskID());

        history = (ArrayList<Task>) taskManagerTest.getHistory();

        ManagerTaskNotFoundException ex = Assertions.assertThrows(
                ManagerTaskNotFoundException.class,
                generateTaskNotFoundExecutable(savedTask.getTaskID())
        );

        Assertions.assertEquals("Task not found", ex.getMessage());

        assertTrue(history.isEmpty(), "Task is not deleted from history");
    }

    @Test
    protected void deleteSubtaskById() throws ManagerSaveException, ManagerValidateException, ManagerTaskNotFoundException {
        InMemoryTaskManagerTest.fillManager( taskManagerTest);

        Subtask savedSubtask = taskManagerTest.getSubtaskObjectByID(5);
        assertNotNull(savedSubtask, "Task not found");

        ArrayList<Task> history = (ArrayList<Task>) taskManagerTest.getHistory();

        assertNotNull(history, "The epics are not returned.");
        assertEquals(1, history.size(), "The number of epics is incorrect.");
        assertEquals(savedSubtask, history.get(0), "The epics are not the same.");

        taskManagerTest.deleteSubtaskById(savedSubtask.getTaskID());

        history = (ArrayList<Task>) taskManagerTest.getHistory();

        ManagerTaskNotFoundException ex = Assertions.assertThrows(
                ManagerTaskNotFoundException.class,
                generateSubtaskNotFoundExecutable(savedSubtask.getTaskID())
        );

        Assertions.assertEquals("Subtask not found", ex.getMessage());

        assertTrue(history.isEmpty(), "Subtask is not deleted from history");
    }

    @Test
    protected void deleteEpicById() throws ManagerSaveException, ManagerValidateException, ManagerTaskNotFoundException {
        InMemoryTaskManagerTest.fillManager( taskManagerTest);

        Epic savedEpic = taskManagerTest.getEpicObjectByID(3);
        assertNotNull(savedEpic, "Epic not found");

        ArrayList<Task> history = (ArrayList<Task>) taskManagerTest.getHistory();

        assertNotNull(history, "The epics are not returned.");
        assertEquals(1, history.size(), "The number of epics is incorrect.");
        assertEquals(savedEpic, history.get(0), "The epics are not the same.");

        taskManagerTest.deleteEpicById(savedEpic.getTaskID());

        history = (ArrayList<Task>) taskManagerTest.getHistory();

        ManagerTaskNotFoundException ex = Assertions.assertThrows(
                ManagerTaskNotFoundException.class,
                generateEpicNotFoundExecutable(savedEpic.getTaskID())
        );

        Assertions.assertEquals("Epic not found", ex.getMessage());

        assertTrue(history.isEmpty(), "Epic is not deleted from history");
        assertTrue(taskManagerTest.getAllSubtasks().isEmpty(), "Subtasks are not deleted");
    }

    @Test
    protected void getAllTasks() throws ManagerSaveException, ManagerValidateException {
        InMemoryTaskManagerTest.fillManager( taskManagerTest);

        ArrayList<Task> taskArrayList = taskManagerTest.getAllTasks();

        assertNotNull(taskArrayList, "The TaskList are not returned.");
        assertEquals(2, taskArrayList.size(), "The number of epics is incorrect.");
    }

    @Test
    protected void getAllSubtasks() throws ManagerSaveException, ManagerValidateException {
        InMemoryTaskManagerTest.fillManager( taskManagerTest);

        ArrayList<Subtask> subtaskArrayList = taskManagerTest.getAllSubtasks();

        assertNotNull(subtaskArrayList, "The subtaskList are not returned.");
        assertEquals(4, subtaskArrayList.size(), "The number of epics is incorrect.");
    }

    @Test
    protected void getAllEpics() throws ManagerSaveException, ManagerValidateException {
        InMemoryTaskManagerTest.fillManager( taskManagerTest);

        ArrayList<Epic> epicArrayList = taskManagerTest.getAllEpics();

        assertNotNull(epicArrayList, "The epicList are not returned.");
        assertEquals(2, epicArrayList.size(), "The number of epics is incorrect.");
    }

    @Test
    protected void getHistory() throws ManagerSaveException, ManagerValidateException, ManagerTaskNotFoundException {
        ArrayList<Task> getHistoryList = (ArrayList<Task>) taskManagerTest.getHistory();
        assertNotNull(getHistoryList, "The history are not returned.");
        assertTrue(getHistoryList.isEmpty(), "The history is not empty.");

        InMemoryTaskManagerTest.fillManager( taskManagerTest);

        Task savedTask = taskManagerTest.getTaskObjectByID(1);
        assertNotNull(savedTask, "Task not found");

        Epic savedEpic = taskManagerTest.getEpicObjectByID(3);
        assertNotNull(savedEpic, "Epic not found");

        Subtask savedSubtask = taskManagerTest.getSubtaskObjectByID(5);
        assertNotNull(savedSubtask, "Subtask not found");

        ArrayList<Task> historyList = new ArrayList<>();
        historyList.add(savedTask);
        historyList.add(savedEpic);
        historyList.add(savedSubtask);

        getHistoryList = (ArrayList<Task>) taskManagerTest.getHistory();
        assertNotNull(getHistoryList, "The history are not returned.");
        assertEquals(3, getHistoryList.size(), "The number of tasks in history is incorrect.");
        assertEquals(historyList, getHistoryList, "The histories are not the same.");
    }

    @Test
    protected void getTask() throws ManagerSaveException, ManagerValidateException {
        InMemoryTaskManagerTest.fillManager( taskManagerTest);

        Task savedTask = ( taskManagerTest).getTask(1);
        assertNotNull(savedTask, "Task not found");

        Epic savedEpic = (Epic) ( taskManagerTest).getTask(3);
        assertNotNull(savedEpic, "Epic not found");

        Subtask savedSubtask = (Subtask) ( taskManagerTest).getTask(5);
        assertNotNull(savedSubtask, "Subtask not found");
    }

    protected Executable generateTaskNotFoundExecutable(int taskID) {
        return () -> taskManagerTest.getTaskObjectByID(taskID);
    }

    protected Executable generateSubtaskNotFoundExecutable(int taskID) {
        return () -> taskManagerTest.getSubtaskObjectByID(taskID);
    }

    protected Executable generateEpicNotFoundExecutable(int taskID) {
        return () -> taskManagerTest.getEpicObjectByID(taskID);
    }

    @Test
    abstract void testEquals() throws ManagerSaveException, ManagerValidateException;
}