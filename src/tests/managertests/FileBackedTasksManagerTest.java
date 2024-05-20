package tests.managertests;

import manager.managerexception.ManagerSaveException;
import manager.managerexception.ManagerTaskNotFoundException;
import manager.managerexception.ManagerValidateException;
import manager.taskmanager.FileBackedTasksManager;
import manager.taskmanager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends InMemoryTaskManagerTest {

    private final File file = new File("resources\\taskManagerTest.txt");

    @BeforeEach
    public void beforeEach() {
        taskManagerTest = new FileBackedTasksManager(file);
    }

    @AfterEach
    public void afterEach() {
        try {
            Files.delete(file.toPath());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void loadFromFileStandardBehavior() throws ManagerSaveException, ManagerValidateException, ManagerTaskNotFoundException {
        fillManager(taskManagerTest);
        fillHistoryManager(taskManagerTest);

        FileBackedTasksManager tasksManagerLoad = FileBackedTasksManager.loadFromFile(file);
        assertNotNull(tasksManagerLoad, "tasksManager didn't load");
        Assertions.assertEquals(taskManagerTest, tasksManagerLoad, "The tasksManagers aren't the same");
    }

    @Test
    void loadFromFileWithEmptyHistory() throws ManagerSaveException, ManagerValidateException {
        fillManager(taskManagerTest);

        FileBackedTasksManager tasksManagerLoad = FileBackedTasksManager.loadFromFile(file);
        assertNotNull(tasksManagerLoad, "tasksManager didn't load");
        assertTrue(tasksManagerLoad.getHistory().isEmpty(), "The history isn't empty");
        Assertions.assertEquals(taskManagerTest, tasksManagerLoad, "The tasksManagers aren't the same");
    }

    @Test
    void loadFromFileWithEmptyTasksList() throws ManagerSaveException {
        ((FileBackedTasksManager) taskManagerTest).save();
        FileBackedTasksManager tasksManagerLoad = FileBackedTasksManager.loadFromFile(file);
        assertNotNull(tasksManagerLoad, "tasksManager didn't load");
        assertTrue(tasksManagerLoad.getHistory().isEmpty(), "The history isn't empty");

        assertTrue(tasksManagerLoad.getAllTasks().isEmpty(), "The taskList isn't empty");
        assertTrue(tasksManagerLoad.getAllEpics().isEmpty(), "The epicList isn't empty");
        assertTrue(tasksManagerLoad.getAllSubtasks().isEmpty(), "The subtaskList isn't empty");

        Assertions.assertEquals(taskManagerTest, tasksManagerLoad, "The tasksManagers aren't the same");
    }

    @Test
    public void taskObjectToString() throws ManagerSaveException, ManagerValidateException {
        FileBackedTasksManagerTest.fillManager(taskManagerTest);

        Task savedTask = taskManagerTest.getTask(1);
        assertNotNull(savedTask, "Task not found");

        Epic savedEpic = (Epic) taskManagerTest.getTask(3);
        assertNotNull(savedEpic, "Epic not found");

        Subtask savedSubtask = (Subtask) taskManagerTest.getTask(5);
        assertNotNull(savedSubtask, "Subtask not found");
    }

    @Test
    public void testEquals() throws ManagerSaveException, ManagerValidateException {
        FileBackedTasksManagerTest.fillManager(taskManagerTest);

        TaskManager taskManagerCopy = new FileBackedTasksManager(file);
        FileBackedTasksManagerTest.fillManager(taskManagerCopy);
        Assertions.assertEquals(taskManagerTest, taskManagerCopy, "taskManagers are not the same");
    }

    @Test
    public void shouldThrowExceptionForLoadFromFile() {
        ManagerSaveException ex = Assertions.assertThrows(
                ManagerSaveException.class,
                generateSaveExecutable(new File("resources\\taskManager_2.txt"))
        );

        Assertions.assertEquals("resources\\taskManager_2.txt (Не удается найти указанный файл)", ex.getMessage());
    }

    @Test
    public void testToString() throws ManagerSaveException, ManagerValidateException, ManagerTaskNotFoundException {
        FileBackedTasksManagerTest.fillManager(taskManagerTest);
        FileBackedTasksManagerTest.fillHistoryManager(taskManagerTest);

        String managerRetunredString = taskManagerTest.toString();
        assertNotNull(managerRetunredString, "Manager don't return string");

        String correctString = "Path: resources\\taskManagerTest.txt\n" +
                "1,TASK,task1Test,NEW,Test task1Test description,2015.05.22 12:00,PT15M\n" +
                "2,TASK,task2Test,NEW,Test task2Test description\n" +
                "3,EPIC,epic1Test,NEW,Test epic1Test description,2015.05.23 13:00,2015.05.23 16:30\n" +
                "4,EPIC,epic2Test,NEW,Test epic2Test description\n" +
                "5,SUBTASK,subtask1Test,NEW,Test subtask1Test description,2015.05.23 15:00,PT15M,3\n" +
                "6,SUBTASK,subtask2Test,NEW,Test subtask2Test description,3\n" +
                "7,SUBTASK,subtask3Test,NEW,Test subtask3Test description,2015.05.23 16:00,PT30M,3\n" +
                "8,SUBTASK,subtask3Test,NEW,Test subtask3Test description,2015.05.23 13:00,PT30M,3\n" +
                "\n" +
                "History: 3,4,2,1,7,6,5";

        assertEquals(managerRetunredString, correctString, "Returned string isn't correct");
    }

    private static void fillHistoryManager(TaskManager manager) throws ManagerSaveException, ManagerTaskNotFoundException {
        manager.getEpicObjectByID(3);
        manager.getEpicObjectByID(4);
        manager.getTaskObjectByID(2);
        manager.getTaskObjectByID(1);
        manager.getSubtaskObjectByID(7);
        manager.getSubtaskObjectByID(6);
        manager.getSubtaskObjectByID(5);
    }

    private Executable generateSaveExecutable(File file) {
        return () -> FileBackedTasksManager.loadFromFile(file);
    }
}