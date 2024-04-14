package tests.taskstests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.TaskStatus;
import tasks.TaskType;

import java.time.*;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    private static Task testTask;
    private static final Task testTaskWithoutStartTimeAndDuration = createTaskWithoutStartTime();

    private static Task create(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        LocalDateTime localDateTime = LocalDateTime.parse("1997.08.07 12:00", formatter);

        return new Task(1, "Task", TaskStatus.NEW, "Testing task",
                localDateTime.toInstant(ZoneOffset.UTC),
                localDateTime.plusMinutes(15).toInstant(ZoneOffset.UTC));
    }

    private static Task createTaskWithoutStartTime(){
        return new Task(2, "Task2", TaskStatus.NEW,
                "Task without startTime and duration");
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
    public void shouldReturnNullForStartTimeAndEndTime(){
        assertNull(testTaskWithoutStartTimeAndDuration.getStartTime(), "The task startTime isn't null");
        assertNull(testTaskWithoutStartTimeAndDuration.getEndTime(), "The task endTime isn't null");
    }

    @Test
    public void shouldReturnTaskStartTime(){
        assertNotNull(testTask.getStartTime(), "The task startTime is not returned");
        assertEquals(testTask.getStartTime(), Instant.parse("1997-08-07T12:00:00.0Z"), "The returned task startTime is incorrect.");
    }

    @Test
    public void shouldSetNewTaskStartTime(){
        testTask.setStartTime(Instant.parse("1997-08-08T12:00:00.0Z"));
        assertNotNull(testTask.getStartTime(), "The task startTime is not returned");
        assertEquals(testTask.getStartTime(), Instant.parse("1997-08-08T12:00:00.0Z"), "The returned task startTime is incorrect.");
    }

    @Test
    public void shouldReturnTaskDuration15(){
        assertNotNull(testTask.getDuration(), "The task duration is not returned");
        assertEquals(testTask.getDuration(), Duration.ofMinutes(15), "The returned task duration is incorrect.");
    }

    @Test
    public void shouldSetTaskDuration30(){
        testTask.setDuration(Duration.ofMinutes(30));
        assertNotNull(testTask.getDuration(), "The task duration is not returned");
        assertEquals(testTask.getDuration(), Duration.ofMinutes(30), "The returned task duration is incorrect.");
    }

    @Test
    public void testGetEndTime(){
        assertNotNull(testTask.getEndTime(), "The task endTime is not returned");
        assertEquals(testTask.getEndTime(), Instant.parse("1997-08-07T12:15:00.0Z"), "The returned task endTime is incorrect.");
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
        assertEquals("1,TASK,Task,NEW,Testing task,1997.08.07 12:00,1997.08.07 12:15", testTask.toString());
    }

    @Test
    public void shouldReturnTaskTypeForTask(){
        Assertions.assertEquals(testTask.getTaskType(), TaskType.TASK);
    }

    @Test
    public void shouldReturnCorrectStringForTaskWithoutStartTime(){
        assertEquals("2,TASK,Task2,NEW,Task without startTime and duration", testTaskWithoutStartTimeAndDuration.toString());
    }

    @Test
    public void shouldReturnTrueEqualsForTaskWithoutStartTime(){
        Task testTaskCopy = createTaskWithoutStartTime();
        assertEquals(testTaskWithoutStartTimeAndDuration, testTaskCopy);
    }
}