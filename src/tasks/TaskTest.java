package tasks;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    private static Task testTask;

    private static Task create(){
        return new Task(1, "Task", TaskStatus.NEW, "Testing task");
    }

    @BeforeEach
    public void beforeEach(){
        testTask = TaskTest.create();
    }

    @Test
    public void shouldReturnNewTaskStatus(){
        assertNotNull(testTask.getStatus(), "The task status is not returned");
        assertEquals(testTask.getStatus(), TaskStatus.NEW, "The returned task status is incorrect.");
    }

    @Test
    public void shouldReturnInProgressTaskStatus(){
        testTask.setStatus(TaskStatus.IN_PROGRESS);
        assertNotNull(testTask.getStatus(), "The task status is not returned");
        assertEquals(testTask.getStatus(), TaskStatus.IN_PROGRESS, "The returned task status is incorrect.");
    }

    @Test
    public void shouldReturnDoneTaskStatus(){
        testTask.setStatus(TaskStatus.DONE);
        assertNotNull(testTask.getStatus(), "The task status is not returned");
        assertEquals(testTask.getStatus(), TaskStatus.DONE, "The returned task status is incorrect.");
    }

    @Test
    public void shouldReturn2ForGetTaskID(){
        testTask.setTaskID(2);
        assertEquals(testTask.getTaskID(), 2, "The returned taskID is incorrect.");
    }

    @Test
    public void shouldReturnTrueEquals(){
        Task testTaskCopy = create();
        assertEquals(testTask, testTaskCopy);
    }

    @Test
    public void shouldReturnFalseEquals(){
        Task testTaskCopy = create();
        testTaskCopy.setStatus(TaskStatus.IN_PROGRESS);
        assertNotEquals(testTask, testTaskCopy);
    }

    @Test
    public void shouldReturnCorrectString(){
        assertEquals("1,TASK,Task,NEW,Testing task", testTask.toString());
    }

    @Test
    public void shouldReturnTaskTypeForTask(){
        assertEquals(testTask.getTaskType(), TaskType.TASK);
    }
}