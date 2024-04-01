package tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    private static Epic testEpic;
    private static HashMap<Integer, Subtask> subtaskHashMap;

    private static Epic create(){
        return new Epic(1, "Epic", "Testing epic");
    }

    private static void  fillSubtaskHashMap(){
        Subtask subtask1 = new Subtask(2, "Subtask1", TaskStatus.NEW, "Testing Subtask1", 1);
        Subtask subtask2 = new Subtask(3, "Subtask2", TaskStatus.NEW, "Testing Subtask2", 1);
        Subtask subtask3 = new Subtask(4, "Subtask3", TaskStatus.NEW, "Testing Subtask3", 1);

        subtaskHashMap= new HashMap<>();
        subtaskHashMap.put(subtask1.getTaskID(), subtask1);
        subtaskHashMap.put(subtask2.getTaskID(), subtask2);
        subtaskHashMap.put(subtask3.getTaskID(), subtask3);
    }

    private void setArrayList(){
        for (Integer subtaskID: subtaskHashMap.keySet()){
            testEpic.setSubtaskIDList(subtaskID);
        }
    }
    @BeforeEach
    public void beforeEach(){
        testEpic = EpicTest.create();
        EpicTest.fillSubtaskHashMap();
    }

    @Test
    public void shouldReturnNewWithEmptySubtaskList(){
        assertNotNull(testEpic.getStatus(), "The task status is not returned");
        assertEquals(testEpic.getStatus(), TaskStatus.NEW, "The returned task status is incorrect.");
    }

    @Test
    public void shouldReturnNewWithAllNewSubtaskList(){
        setArrayList();
        assertArrayEquals(testEpic.getSubtaskIDList().toArray(), subtaskHashMap.keySet().toArray(),
                "The returned epicIDList is incorrect.");

        testEpic.checkEpicStatus(subtaskHashMap);
        assertNotNull(testEpic.getStatus(), "The task status is not returned");
        assertEquals(testEpic.getStatus(), TaskStatus.NEW, "The returned task status is incorrect.");
    }

    @Test
    public void shouldReturnDoneWithAllDoneSubtaskList(){
        setArrayList();
        assertArrayEquals(testEpic.getSubtaskIDList().toArray(), subtaskHashMap.keySet().toArray(),
                "The returned epicIDList is incorrect.");

        for (Subtask subtask: subtaskHashMap.values()){
            subtask.setStatus(TaskStatus.DONE);
        }

        testEpic.checkEpicStatus(subtaskHashMap);
        assertNotNull(testEpic.getStatus(), "The task status is not returned");
        assertEquals(testEpic.getStatus(), TaskStatus.DONE, "The returned task status is incorrect.");
    }

    @Test
    public void shouldReturnInProgressWithAllInProgressSubtaskList(){
        setArrayList();
        assertArrayEquals(testEpic.getSubtaskIDList().toArray(), subtaskHashMap.keySet().toArray(),
                "The returned epicIDList is incorrect.");

        for (Subtask subtask: subtaskHashMap.values()){
            subtask.setStatus(TaskStatus.IN_PROGRESS);
        }

        testEpic.checkEpicStatus(subtaskHashMap);
        assertNotNull(testEpic.getStatus(), "The task status is not returned");
        assertEquals(testEpic.getStatus(), TaskStatus.IN_PROGRESS, "The returned task status is incorrect.");
    }

    @Test
    public void shouldReturnInProgressWithNewAndDoneSubtaskList(){
        setArrayList();
        assertArrayEquals(testEpic.getSubtaskIDList().toArray(), subtaskHashMap.keySet().toArray(),
                "The returned epicIDList is incorrect.");

        Subtask subtask = subtaskHashMap.get(3);
        subtask.setStatus(TaskStatus.DONE);
        subtaskHashMap.put(3, subtask);

        testEpic.checkEpicStatus(subtaskHashMap);
        assertNotNull(testEpic.getStatus(), "The task status is not returned");
        assertEquals(testEpic.getStatus(), TaskStatus.IN_PROGRESS, "The returned task status is incorrect.");
    }

    @Test
    public void shouldDeleteAllSubtasks(){
        setArrayList();
        assertArrayEquals(testEpic.getSubtaskIDList().toArray(), subtaskHashMap.keySet().toArray(),
                "The returned epicIDList is incorrect.");

        testEpic.deleteAllSubtaskID();
        assertTrue(testEpic.getSubtaskIDList().isEmpty(), "Not all subtasksID have been deleted");
    }

    @Test
    public void shouldDeleteSubtaskWithID3(){
        setArrayList();
        assertArrayEquals(testEpic.getSubtaskIDList().toArray(), subtaskHashMap.keySet().toArray(),
                "The returned epicIDList is incorrect.");

        testEpic.deleteSubtaskID(3);
        assertFalse(testEpic.getSubtaskIDList().contains(3), "subtask with ID 3 haven't been deleted");
    }

    @Test
    public void shouldReturnTrueEquals(){
        Epic testEpicCopy = create();
        assertEquals(testEpic, testEpicCopy);
    }

    @Test
    public void shouldReturnFalseEquals(){
        Task testEpicCopy = create();
        testEpicCopy.setStatus(TaskStatus.IN_PROGRESS);
        assertNotEquals(testEpic, testEpicCopy);
    }

    @Test
    public void shouldReturnCorrectString(){
        assertEquals("1,EPIC,Epic,NEW,Testing epic", testEpic.toString());
    }

    @Test
    public void shouldReturnSubtaskTypeForSubtask(){
        assertEquals(testEpic.getTaskType(), TaskType.EPIC);
    }
}