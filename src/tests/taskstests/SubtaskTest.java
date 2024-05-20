package tests.taskstests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Subtask;
import tasks.TaskStatus;
import tasks.TaskType;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    private static Subtask testSubtask;

    private static Subtask create() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        LocalDateTime localDateTime = LocalDateTime.parse("1997.08.07 12:00", formatter);

        return new Subtask(1, "Subtask", TaskStatus.NEW, "Testing Subtask",
                localDateTime.toInstant(ZoneOffset.UTC),
                localDateTime.plusMinutes(15).toInstant(ZoneOffset.UTC), 0);
    }

    @BeforeEach
    public void beforeEach() {
        testSubtask = SubtaskTest.create();
    }

    @Test
    public void shouldReturn1ForGetEpicID() {
        assertEquals(testSubtask.getEpicID(), 0, "The returned epicID is incorrect.");
    }

    @Test
    public void shouldReturn2ForGetEpicID() {
        testSubtask.setEpicID(2);
        assertEquals(testSubtask.getEpicID(), 2, "The returned epicID is incorrect.");
    }

    @Test
    public void shouldReturnTrueEquals() {
        Subtask testSubtaskCopy = create();
        assertEquals(testSubtask, testSubtaskCopy);
    }

    @Test
    public void shouldReturnFalseEquals() {
        Subtask testSubtaskCopy = create();
        testSubtaskCopy.setStatus(TaskStatus.IN_PROGRESS);
        assertNotEquals(testSubtask, testSubtaskCopy);
    }

    @Test
    public void shouldReturnCorrectString() {
        assertEquals("1,SUBTASK,Subtask,NEW,Testing Subtask,1997.08.07 12:00,PT15M,0", testSubtask.toString());
    }

    @Test
    public void shouldReturnSubtaskTypeForSubtask() {
        Assertions.assertEquals(testSubtask.getTaskType(), TaskType.SUBTASK);
    }
}