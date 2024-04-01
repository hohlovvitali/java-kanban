package manager.taskmanager;

import manager.managerexception.ManagerSaveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest{

    protected static void fillManager(InMemoryTaskManager taskManagerTest) throws ManagerSaveException {
        Task task1 = new Task("task1Test", "Test task1Test description");
        taskManagerTest.addTask(task1);
        Task task2 = new Task("task2Test", "Test task2Test description");
        taskManagerTest.addTask(task2);

        Epic epic1 = new Epic(0,"epic1Test", "Test epic1Test description");
        Epic epic2 = new Epic(0,"epic2Test", "Test epic2Test description");
        taskManagerTest.addEpic(epic1);
        taskManagerTest.addEpic(epic2);

        Subtask subtask1 = new Subtask("subtask1Test", "Test subtask1Test description", epic1.getTaskID());
        Subtask subtask2 = new Subtask("subtask2Test", "Test subtask2Test description", epic1.getTaskID());
        Subtask subtask3 = new Subtask("subtask3Test", "Test subtask3Test description", epic1.getTaskID());
        taskManagerTest.addSubtask(subtask1);
        taskManagerTest.addSubtask(subtask2);
        taskManagerTest.addSubtask(subtask3);
    }

    @BeforeEach
    public void beforeEach(){
        taskManagerTest = new InMemoryTaskManager();
    }

    @Test
    protected void addTask() throws ManagerSaveException {
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
    protected void addEpic() throws ManagerSaveException {
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
    void addSubtask() throws ManagerSaveException {
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
    protected  void updateTask() throws ManagerSaveException {
        InMemoryTaskManagerTest.fillManager((InMemoryTaskManager) taskManagerTest);

        Task savedTask = taskManagerTest.getTaskObjectByID(1);
        assertNotNull(savedTask, "Task not found");
        savedTask.setStatus(TaskStatus.IN_PROGRESS);
        taskManagerTest.updateTask(savedTask);
        assertEquals(taskManagerTest.getTaskObjectByID(1).getStatus(), TaskStatus.IN_PROGRESS, "TaskStatus not updated");
    }

    @Test
    protected  void updateSubtask() throws ManagerSaveException {
        InMemoryTaskManagerTest.fillManager((InMemoryTaskManager) taskManagerTest);

        Subtask savedSubtask = taskManagerTest.getSubtaskObjectByID(5);
        assertNotNull(savedSubtask, "Subtask not found");
        savedSubtask.setStatus(TaskStatus.DONE);
        taskManagerTest.updateSubtask(savedSubtask);
        assertEquals(taskManagerTest.getSubtaskObjectByID(5).getStatus(), TaskStatus.DONE, "SubtaskStatus not updated");
    }

    @Test
    protected  void updateEpic() throws ManagerSaveException {
        InMemoryTaskManagerTest.fillManager((InMemoryTaskManager) taskManagerTest);

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
    protected void getTaskObjectByID() throws ManagerSaveException {
        InMemoryTaskManagerTest.fillManager((InMemoryTaskManager) taskManagerTest);

        Task savedTask = taskManagerTest.getTaskObjectByID(5);
        assertNull(savedTask, "Task was found incorrect");

        savedTask = taskManagerTest.getTaskObjectByID(1);
        assertNotNull(savedTask, "Task not found");

        final List<Task> tasks = taskManagerTest.getAllTasks();

        assertNotNull(tasks, "The tasks are not returned.");
        assertEquals(2, tasks.size(), "The number of tasks is incorrect.");
        assertEquals(savedTask, tasks.get(0), "The tasks are not the same.");
    }

    @Test
    protected void getSubtaskObjectByID() throws ManagerSaveException {
        InMemoryTaskManagerTest.fillManager((InMemoryTaskManager) taskManagerTest);

        Subtask savedSubtask = taskManagerTest.getSubtaskObjectByID(1);
        assertNull(savedSubtask, "Subtask was found incorrect");

        savedSubtask = taskManagerTest.getSubtaskObjectByID(5);
        assertNotNull(savedSubtask, "Subtask not found");

        final List<Subtask> subtasks = taskManagerTest.getAllSubtasks();

        assertNotNull(subtasks, "The subtasks are not returned.");
        assertEquals(3, subtasks.size(), "The number of subtasks is incorrect.");
        assertEquals(savedSubtask, subtasks.get(0), "The subtasks are not the same.");


    }

    @Test
    protected void getEpicObjectByID() throws ManagerSaveException {
        InMemoryTaskManagerTest.fillManager((InMemoryTaskManager) taskManagerTest);

        Epic savedEpic = taskManagerTest.getEpicObjectByID(6);
        assertNull(savedEpic, "Epic was found incorrect");

        savedEpic = taskManagerTest.getEpicObjectByID(3);
        assertNotNull(savedEpic, "Epic not found");

        final List<Epic> epics = taskManagerTest.getAllEpics();

        assertNotNull(epics, "The epics are not returned.");
        assertEquals(2, epics.size(), "The number of epics is incorrect.");
        assertEquals(savedEpic, epics.get(0), "The epics are not the same.");
    }

    @Test
    protected void deleteAllTasks() throws ManagerSaveException {
        InMemoryTaskManagerTest.fillManager((InMemoryTaskManager) taskManagerTest);

        taskManagerTest.deleteAllTasks();
        assertTrue(taskManagerTest.getAllTasks().isEmpty(), "Tasks are not deleted");
    }

    @Test
    protected void deleteAllSubtasks() throws ManagerSaveException {
        InMemoryTaskManagerTest.fillManager((InMemoryTaskManager) taskManagerTest);

        taskManagerTest.deleteAllSubtasks();
        assertTrue(taskManagerTest.getAllSubtasks().isEmpty(), "Subtasks are not deleted");
    }

    @Test
    protected void deleteAllEpics() throws ManagerSaveException {
        InMemoryTaskManagerTest.fillManager((InMemoryTaskManager) taskManagerTest);

        taskManagerTest.deleteAllEpics();
        assertTrue(taskManagerTest.getAllEpics().isEmpty(), "Epics are not deleted");
    }

    @Test
    protected void deleteTaskById() throws ManagerSaveException {
        InMemoryTaskManagerTest.fillManager((InMemoryTaskManager) taskManagerTest);

        Task savedTask = taskManagerTest.getTaskObjectByID(1);
        assertNotNull(savedTask, "Task not found");

        ArrayList<Task> history = (ArrayList<Task>) taskManagerTest.getHistory();

        assertNotNull(history, "The epics are not returned.");
        assertEquals(1, history.size(), "The number of epics is incorrect.");
        assertEquals(savedTask, history.get(0), "The epics are not the same.");

        taskManagerTest.deleteTaskById(savedTask.getTaskID());

        history = (ArrayList<Task>) taskManagerTest.getHistory();
        savedTask = taskManagerTest.getTaskObjectByID(savedTask.getTaskID());
        assertNull(savedTask, "Task is not deleted");

        assertTrue(history.isEmpty(), "Task is not deleted from history");
    }

    @Test
    protected void deleteSubtaskById() throws ManagerSaveException {
        InMemoryTaskManagerTest.fillManager((InMemoryTaskManager) taskManagerTest);

        Subtask savedSubtask = taskManagerTest.getSubtaskObjectByID(5);
        assertNotNull(savedSubtask, "Task not found");

        ArrayList<Task> history = (ArrayList<Task>) taskManagerTest.getHistory();

        assertNotNull(history, "The epics are not returned.");
        assertEquals(1, history.size(), "The number of epics is incorrect.");
        assertEquals(savedSubtask, history.get(0), "The epics are not the same.");

        taskManagerTest.deleteSubtaskById(savedSubtask.getTaskID());

        history = (ArrayList<Task>) taskManagerTest.getHistory();
        savedSubtask = taskManagerTest.getSubtaskObjectByID(savedSubtask.getTaskID());
        assertNull(savedSubtask, "Subtask is not deleted");

        assertTrue(history.isEmpty(), "Subtask is not deleted from history");
    }

    @Test
    protected void deleteEpicById() throws ManagerSaveException {
        InMemoryTaskManagerTest.fillManager((InMemoryTaskManager) taskManagerTest);

        Epic savedEpic = taskManagerTest.getEpicObjectByID(3);
        assertNotNull(savedEpic, "Epic not found");

        ArrayList<Task> history = (ArrayList<Task>) taskManagerTest.getHistory();

        assertNotNull(history, "The epics are not returned.");
        assertEquals(1, history.size(), "The number of epics is incorrect.");
        assertEquals(savedEpic, history.get(0), "The epics are not the same.");

        taskManagerTest.deleteEpicById(savedEpic.getTaskID());

        history = (ArrayList<Task>) taskManagerTest.getHistory();
        savedEpic = taskManagerTest.getEpicObjectByID(savedEpic.getTaskID());
        assertNull(savedEpic, "Epic is not deleted");

        assertTrue(history.isEmpty(), "Epic is not deleted from history");
        assertTrue(taskManagerTest.getAllSubtasks().isEmpty(), "Subtasks are not deleted");
    }

    @Test
    protected void getAllTasks() throws ManagerSaveException {
        InMemoryTaskManagerTest.fillManager((InMemoryTaskManager) taskManagerTest);

        ArrayList<Task> taskArrayList = taskManagerTest.getAllTasks();

        assertNotNull(taskArrayList, "The TaskList are not returned.");
        assertEquals(2, taskArrayList.size(), "The number of epics is incorrect.");
    }

    @Test
    protected void getAllSubtasks() throws ManagerSaveException {
        InMemoryTaskManagerTest.fillManager((InMemoryTaskManager) taskManagerTest);

        ArrayList<Subtask> subtaskArrayList = taskManagerTest.getAllSubtasks();

        assertNotNull(subtaskArrayList, "The subtaskList are not returned.");
        assertEquals(3, subtaskArrayList.size(), "The number of epics is incorrect.");
    }

    @Test
    protected void getAllEpics() throws ManagerSaveException {
        InMemoryTaskManagerTest.fillManager((InMemoryTaskManager) taskManagerTest);

        ArrayList<Epic> epicArrayList = taskManagerTest.getAllEpics();

        assertNotNull(epicArrayList, "The epicList are not returned.");
        assertEquals(2, epicArrayList.size(), "The number of epics is incorrect.");
    }

    @Test
    protected void getHistory() throws ManagerSaveException {
        ArrayList<Task> getHistoryList = (ArrayList<Task>) taskManagerTest.getHistory();
        assertNotNull(getHistoryList, "The history are not returned.");
        assertTrue(getHistoryList.isEmpty(), "The history is not empty.");

        InMemoryTaskManagerTest.fillManager((InMemoryTaskManager) taskManagerTest);

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
    protected void testEquals() throws ManagerSaveException {
        InMemoryTaskManagerTest.fillManager((InMemoryTaskManager) taskManagerTest);

        InMemoryTaskManager taskManagerCopy = new InMemoryTaskManager();
        InMemoryTaskManagerTest.fillManager(taskManagerCopy);
        assertEquals(taskManagerTest, taskManagerCopy, "taskManagers are not the same");
    }

    @Test
    protected void getTask() throws ManagerSaveException {
        InMemoryTaskManagerTest.fillManager((InMemoryTaskManager) taskManagerTest);

        Task savedTask = ((InMemoryTaskManager) taskManagerTest).getTask(1);
        assertNotNull(savedTask, "Task not found");

        Epic savedEpic = (Epic) ((InMemoryTaskManager) taskManagerTest).getTask(3);
        assertNotNull(savedEpic, "Epic not found");

        Subtask savedSubtask = (Subtask) ((InMemoryTaskManager) taskManagerTest).getTask(5);
        assertNotNull(savedSubtask, "Subtask not found");
    }

    @Test
    public void getSubtaskObjectByIDNotMemory() throws ManagerSaveException {
        InMemoryTaskManagerTest.fillManager((InMemoryTaskManager) taskManagerTest);

        Subtask savedTask = ((InMemoryTaskManager) taskManagerTest).getSubtaskObjectByIDNotMemory(5);
        assertNotNull(savedTask, "Subtask not found");

        ArrayList<Task> getHistoryList = (ArrayList<Task>) taskManagerTest.getHistory();
        assertNotNull(getHistoryList, "The history are not returned.");
        assertTrue(getHistoryList.isEmpty(), "The history is not empty.");
    }
}