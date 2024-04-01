package manager.taskmanager;

import manager.managerexception.ManagerSaveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends InMemoryTaskManagerTest {
    private final File file = new File("resourcesTest\\taskManager.txt");
    @BeforeEach
    public void beforeEach(){
        taskManagerTest = new FileBackedTasksManager(file);
    }

    @Test
    void loadFromFile() {
    }

    @Test
    public void addTask() throws ManagerSaveException {
        super.addTask();

    }

    @Test
    public void addSubtask() throws ManagerSaveException {
        super.addSubtask();
    }

    @Test
    public void addEpic() {
    }

    @Test
    public void updateTask() {
    }

    @Test
    public void updateSubtask() {
    }

    @Test
    public void updateEpic() {
    }

    @Test
    public void deleteTaskById() {
    }

    @Test
    public void deleteSubtaskById() {
    }

    @Test
    public void deleteEpicById() {
    }

    @Test
    public void getEpicObjectByID() {
    }

    @Test
    public void getTaskObjectByID() {
    }

    @Test
    public void getSubtaskObjectByID() {
    }

    @Test
    public void testEquals() {
    }

    @Test
    public void testToString() {
    }
}