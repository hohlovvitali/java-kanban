package tests.managertests;

import manager.historymanager.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private InMemoryHistoryManager inMemoryHistoryManagerTest = new InMemoryHistoryManager();

    private final Task taskHead = new Task(1, "TestTaskHead", TaskStatus.NEW, "TestTaskHead description");

    private static void fillHistoryManager(InMemoryHistoryManager historyManager) {
        Task task1 = new Task(2, "TestTask1", TaskStatus.NEW, "TestTask1 description");
        Task task2 = new Task(3, "TestTask2", TaskStatus.NEW, "TestTask2 description");
        Task taskTail = new Task(4, "TestTaskTail", TaskStatus.NEW, "TestTaskTail description");
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(taskTail);
    }

    @BeforeEach
    public void beforeEach() {
        inMemoryHistoryManagerTest = new InMemoryHistoryManager();
    }


    @Test
    void add() {
        inMemoryHistoryManagerTest.add(taskHead);
        List<Task> history = inMemoryHistoryManagerTest.getTasks();
        assertNotNull(history, "History isn't null");
        assertEquals(1, history.size(), "History is empty");
    }

    @Test
    void removeFromMiddleHistory() {
        inMemoryHistoryManagerTest.add(taskHead);
        InMemoryHistoryManagerTest.fillHistoryManager(inMemoryHistoryManagerTest);

        inMemoryHistoryManagerTest.remove(2);
        final List<Task> history = inMemoryHistoryManagerTest.getTasks();
        assertNotNull(history, "history doesn't return");
        assertEquals(3, history.size(), "Task doesn't remove");
        assertNotEquals(history.get(1).getTaskID(), 2, "The Task was not deleted");
    }

    @Test
    void removeTail() {
        inMemoryHistoryManagerTest.add(taskHead);
        InMemoryHistoryManagerTest.fillHistoryManager(inMemoryHistoryManagerTest);

        inMemoryHistoryManagerTest.remove(4);
        final List<Task> history = inMemoryHistoryManagerTest.getTasks();
        assertNotNull(history, "history doesn't return");
        assertEquals(3, history.size(), "Task doesn't remove");
        assertNotEquals(history.get(2).getTaskID(), 4, "The TaskTail was not deleted");
    }

    @Test
    void removeHead() {
        inMemoryHistoryManagerTest.add(taskHead);
        InMemoryHistoryManagerTest.fillHistoryManager(inMemoryHistoryManagerTest);

        inMemoryHistoryManagerTest.remove(taskHead.getTaskID());
        final List<Task> history = inMemoryHistoryManagerTest.getTasks();
        assertNotNull(history, "history doesn't return");
        assertEquals(3, history.size(), "Task doesn't remove");
        assertNotEquals(history.get(0).getTaskID(), 1, "The TaskHead was not deleted");
    }
}