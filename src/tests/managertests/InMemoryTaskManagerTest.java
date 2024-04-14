package tests.managertests;

import manager.Managers;
import manager.managerexception.ManagerSaveException;
import manager.managerexception.ManagerValidateException;
import manager.taskmanager.InMemoryTaskManager;
import manager.taskmanager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager>{

    @BeforeEach
    public void beforeEach(){
        taskManagerTest = (InMemoryTaskManager) Managers.getDefault();
    }

    @Test
    public void getSubtaskObjectByIDNotMemory() throws ManagerSaveException, ManagerValidateException {
        InMemoryTaskManagerTest.fillManager( taskManagerTest);

        Subtask savedTask = ( taskManagerTest).getSubtaskObjectByIDNotMemory(5);
        assertNotNull(savedTask, "Subtask not found");

        ArrayList<Task> getHistoryList = (ArrayList<Task>) taskManagerTest.getHistory();
        assertNotNull(getHistoryList, "The history are not returned.");
        assertTrue(getHistoryList.isEmpty(), "The history is not empty.");
    }

    @Test
    protected void testEquals() throws ManagerSaveException, ManagerValidateException {
        InMemoryTaskManagerTest.fillManager(taskManagerTest);

        TaskManager taskManagerCopy = new InMemoryTaskManager();
        InMemoryTaskManagerTest.fillManager(taskManagerCopy);
        assertEquals(taskManagerTest, taskManagerCopy, "taskManagers are not the same");
    }

    @Test
    public void testGetPrioritizedTasks() throws ManagerSaveException, ManagerValidateException {
        InMemoryTaskManagerTest.fillManager(taskManagerTest);

        List<Task> taskList = taskManagerTest.getPrioritizedTasks();
        assertNotNull(taskList, "TasksList not returned");

        List<Task> checkList = List.of(taskManagerTest.getTask(1), taskManagerTest.getTask(8),
                taskManagerTest.getTask(5), taskManagerTest.getTask(7), taskManagerTest.getTask(2),
                taskManagerTest.getTask(6));
        assertEquals(taskList, checkList, "Tasks List isn't correct");
    }
    @Test
    public void shouldThrowExceptionForLoadFromFileWithSameStartTime() throws ManagerSaveException, ManagerValidateException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        InMemoryTaskManagerTest.fillManager( taskManagerTest);

        Task task = taskManagerTest.getTask(2);
        task.setStartTime(LocalDateTime.parse("2015.05.22 12:10", formatter).toInstant(ZoneOffset.UTC));

        ManagerValidateException ex = Assertions.assertThrows(
                ManagerValidateException.class,
                generateValidateExecutable(task)
        );

        Assertions.assertEquals("Task 2 and task 1 overlap", ex.getMessage());
    }

    @Test
    public void shouldThrowExceptionForLoadFromFileBetweenStartAndEndTime() throws ManagerSaveException, ManagerValidateException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        InMemoryTaskManagerTest.fillManager( taskManagerTest);

        Task task = taskManagerTest.getTask(2);
        task.setStartTime(LocalDateTime.parse("2015.05.22 12:00", formatter).toInstant(ZoneOffset.UTC));

        ManagerValidateException ex = Assertions.assertThrows(
                ManagerValidateException.class,
                generateValidateExecutable(task)
        );

        Assertions.assertEquals("Task 2 and task 1 overlap", ex.getMessage());
    }

    private Executable generateValidateExecutable(Task task) {
        return () -> taskManagerTest.updateTask(task);
    }
}