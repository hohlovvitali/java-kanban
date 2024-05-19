package tests.httptests;

import com.google.gson.Gson;
import http.HttpTaskServer;
import manager.managerexception.ManagerSaveException;
import manager.managerexception.ManagerTaskNotFoundException;
import manager.managerexception.ManagerValidateException;
import manager.taskmanager.InMemoryTaskManager;
import manager.taskmanager.TaskManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTasksTest {

    // создаём экземпляр InMemoryTaskManager
    TaskManager manager = new InMemoryTaskManager();
    // передаём его в качестве аргумента в конструктор HttpTaskServer
    HttpTaskServer taskServer = new HttpTaskServer((InMemoryTaskManager) manager);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
    final String urlHost = "http://localhost:8080/";
    Gson gson = HttpTaskServer.getGson();

    public HttpTaskManagerTasksTest() throws IOException, IOException, ManagerSaveException {
    }

    @BeforeEach
    public void setUp() throws ManagerTaskNotFoundException, ManagerSaveException {
        manager.deleteAllEpics();
        manager.deleteAllTasks();
        manager.deleteAllSubtasks();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        Task task = new Task("TestTask 1", TaskStatus.NEW,"TestTask 1 description",
                LocalDateTime.parse("2015.05.22 12:00", formatter).toInstant(ZoneOffset.UTC), Duration.ofMinutes(60));

        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(urlHost + "tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Task> tasksFromManager = manager.getAllTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("TestTask 1", tasksFromManager.get(0).getTaskName(), "Некорректное имя задачи");
    }

    @Test
    public void testAddEpic() throws IOException, InterruptedException {

        Epic epic1 = new Epic("EpicTest 1", TaskStatus.NEW,"EpicTest 1 description");

        String epicJson = gson.toJson(epic1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(urlHost + "epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Epic> tasksFromManager = manager.getAllEpics();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("EpicTest 1", tasksFromManager.get(0).getTaskName(), "Некорректное имя задачи");
    }

    @Test
    public void testAddSubtask() throws IOException, InterruptedException {

        Epic epic1 = new Epic("EpicTest 1", TaskStatus.NEW,"EpicTest 1 description");

        Subtask subtask1 = new Subtask("TestSubtask 1", TaskStatus.NEW,"TestSubtask 1 description",
                LocalDateTime.parse("2015.05.20 12:00", formatter).toInstant(ZoneOffset.UTC), Duration.ofMinutes(60), 1);

        String epicJson = gson.toJson(epic1);
        String subtaskJson = gson.toJson(subtask1);

        HttpClient client = HttpClient.newHttpClient();
        URI urlEpic = URI.create(urlHost + "epics");
        HttpRequest request = HttpRequest.newBuilder().uri(urlEpic).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        URI urlSubtask= URI.create(urlHost + "subtasks");
        request = HttpRequest.newBuilder().uri(urlSubtask).POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        ArrayList<Subtask> tasksFromManager = manager.getAllSubtasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("TestSubtask 1", tasksFromManager.get(0).getTaskName(), "Некорректное имя задачи");
    }

    @Test
    public void getTasksByID() throws ManagerSaveException, ManagerValidateException, IOException, InterruptedException, ManagerTaskNotFoundException {
        fillManager(manager);

        HttpClient client = HttpClient.newHttpClient();
        URI urlEpic = URI.create(urlHost + "epics/3");
        HttpRequest request = HttpRequest.newBuilder().uri(urlEpic).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(manager.getEpicObjectByID(3)), response.body(), "Не тот Epic возращается");

        URI urlSubtask = URI.create(urlHost + "subtasks/5");
        request = HttpRequest.newBuilder().uri(urlSubtask).GET().build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(manager.getSubtaskObjectByID(5)), response.body(), "Не та Subtask возращается");

        client = HttpClient.newHttpClient();
        URI urlTask = URI.create(urlHost + "tasks/1");
        request = HttpRequest.newBuilder().uri(urlTask).GET().build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(manager.getTaskObjectByID(1)), response.body(), "Не та Task возращается");
    }

    @Test
    public void getEpics() throws ManagerSaveException, ManagerValidateException, IOException, InterruptedException, ManagerTaskNotFoundException {
        fillManager(manager);

        HttpClient client = HttpClient.newHttpClient();
        URI urlEpic = URI.create(urlHost + "epics");
        HttpRequest request = HttpRequest.newBuilder().uri(urlEpic).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(manager.getAllEpics()), response.body(), "Возращаются не те задачи");
    }

    @Test
    public void getEpicSubtasks() throws ManagerSaveException, ManagerValidateException, IOException, InterruptedException, ManagerTaskNotFoundException {
        fillManager(manager);

        HttpClient client = HttpClient.newHttpClient();
        URI urlEpic = URI.create(urlHost + "epics/3/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(urlEpic).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(manager.getEpicObjectByID(3).getSubtaskIDList()), response.body(), "Возращаются не те задачи");
    }

    @Test
    public void getTasks() throws ManagerSaveException, ManagerValidateException, IOException, InterruptedException, ManagerTaskNotFoundException {
        fillManager(manager);

        HttpClient client = HttpClient.newHttpClient();
        URI urlEpic = URI.create(urlHost + "tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(urlEpic).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(manager.getAllTasks()), response.body(), "Возращаются не те задачи");
    }

    @Test
    public void getSubtasks() throws ManagerSaveException, ManagerValidateException, IOException, InterruptedException, ManagerTaskNotFoundException {
        fillManager(manager);

        HttpClient client = HttpClient.newHttpClient();
        URI urlEpic = URI.create(urlHost + "subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(urlEpic).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(manager.getAllSubtasks()), response.body(), "Возращаются не те задачи");
    }

    @Test
    public void getIncorrectTasksByID() throws ManagerSaveException, ManagerValidateException, IOException, InterruptedException {
        fillManager(manager);

        HttpClient client = HttpClient.newHttpClient();
        URI urlEpic = URI.create(urlHost + "epics/7");
        HttpRequest request = HttpRequest.newBuilder().uri(urlEpic).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Неверный статус ответа");

        URI urlSubtask = URI.create(urlHost + "subtasks/7");
        request = HttpRequest.newBuilder().uri(urlSubtask).GET().build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Неверный статус ответа");

        client = HttpClient.newHttpClient();
        URI urlTask = URI.create(urlHost + "tasks/7");
        request = HttpRequest.newBuilder().uri(urlTask).GET().build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Неверный статус ответа");
    }

    @Test
    public void updateTaskByID() throws ManagerSaveException, ManagerValidateException, IOException, InterruptedException {
        fillManager(manager);

        Task task = new Task("TestTask 1", TaskStatus.DONE, "TestTask 1 description",
                LocalDateTime.parse("2015.05.22 11:00", formatter).toInstant(ZoneOffset.UTC), Duration.ofMinutes(75));

        task.setTaskID(1);
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(urlHost + "tasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Неверный статус ответа");

        url = URI.create(urlHost + "tasks/1");
        request = HttpRequest.newBuilder().uri(url).GET().build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(gson.toJson(task), response.body(), "Задача не обновилась");
    }

    @Test
    public void updateSubtaskAndEpicByID() throws ManagerSaveException, ManagerValidateException, IOException, InterruptedException {
        fillManager(manager);

        Subtask subtask = new Subtask("TestSubtask 1", TaskStatus.DONE,"TestSubtask 1 description",
                LocalDateTime.parse("2015.05.20 12:00", formatter).toInstant(ZoneOffset.UTC), Duration.ofMinutes(60), 3);

        subtask.setTaskID(5);
        String taskJson = gson.toJson(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(urlHost + "subtasks/5");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Неверный статус ответа");

        url = URI.create(urlHost + "subtasks/5");
        request = HttpRequest.newBuilder().uri(url).GET().build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(gson.toJson(subtask), response.body(), "Задача не обновилась");

        url = URI.create(urlHost + "epics/3");
        request = HttpRequest.newBuilder().uri(url).GET().build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(gson.fromJson(response.body(), Epic.class).getStatus(), TaskStatus.IN_PROGRESS, "Epic не обовился");
    }

    @Test
    public void testAddTaskWithInteractions() throws IOException, InterruptedException, ManagerSaveException, ManagerValidateException {
        fillManager(manager);

        Task task = new Task("TestTask 3", TaskStatus.NEW,"TestTask 3 description",
                LocalDateTime.parse("2015.05.22 12:30", formatter).toInstant(ZoneOffset.UTC), Duration.ofMinutes(60));

        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(urlHost + "tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, response.statusCode(), "Неверный возращяемый статус");
    }

    @Test
    public void testAddSubtaskWithInteractions() throws IOException, InterruptedException, ManagerSaveException, ManagerValidateException {
        fillManager(manager);

        Subtask subtask = new Subtask("TestSubtask 1", TaskStatus.NEW,"TestSubtask 1 description",
                LocalDateTime.parse("2015.05.20 12:30", formatter).toInstant(ZoneOffset.UTC), Duration.ofMinutes(60), 3);

        String taskJson = gson.toJson(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(urlHost + "subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, response.statusCode(), "Неверный возращяемый статус");
    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException, ManagerSaveException, ManagerValidateException {
        fillManager(manager);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(urlHost + "tasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Неверный возращяемый статус");

        client = HttpClient.newHttpClient();
        url = URI.create(urlHost + "tasks/1");
        request = HttpRequest.newBuilder().uri(url).GET().build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Задача не была удалена");
    }

    @Test
    public void testDeleteSubtask() throws IOException, InterruptedException, ManagerSaveException, ManagerValidateException {
        fillManager(manager);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(urlHost + "subtasks/5");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Неверный возращяемый статус");

        client = HttpClient.newHttpClient();
        url = URI.create(urlHost + "subtasks/5");
        request = HttpRequest.newBuilder().uri(url).GET().build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Задача не была удалена");
    }

    @Test
    public void testDeleteEpic() throws IOException, InterruptedException, ManagerSaveException, ManagerValidateException {
        fillManager(manager);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(urlHost + "epics/3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Неверный возращяемый статус");

        client = HttpClient.newHttpClient();
        url = URI.create(urlHost + "epics/3");
        request = HttpRequest.newBuilder().uri(url).GET().build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Задача не была удалена");
    }

    @Test
    public void testGetPrioritized() throws IOException, InterruptedException, ManagerSaveException, ManagerValidateException {
        fillManager(manager);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(urlHost + "prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Неверный возращяемый статус");

        assertEquals(gson.toJson(manager.getPrioritizedTasks()), response.body(), "Неверный список приоритетных задач");
    }

    @Test
    public void testGetHistory() throws IOException, InterruptedException, ManagerSaveException, ManagerValidateException {
        fillManager(manager);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(urlHost + "tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Неверный возращяемый статус");
        assertEquals(gson.toJson(manager.getAllTasks()), response.body(), "Не верно возращенные задачи");

        client = HttpClient.newHttpClient();
        url = URI.create(urlHost + "history");
        request = HttpRequest.newBuilder().uri(url).GET().build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Неверный возращяемый статус");

        assertEquals(gson.toJson(manager.getHistory()), response.body(), "Неверно возращенная история");
    }

    private static void fillManager(TaskManager taskManagerTest) throws ManagerSaveException, ManagerValidateException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");

        Task task1 = new Task("TestTask 1", TaskStatus.NEW,"TestTask 1 description",
                LocalDateTime.parse("2015.05.22 12:00", formatter).toInstant(ZoneOffset.UTC), Duration.ofMinutes(60));
        taskManagerTest.addTask(task1);

        Task task2 = new Task("TestTask 2", TaskStatus.NEW,"TestTask 2 description",
                LocalDateTime.parse("2015.05.22 13:00", formatter).toInstant(ZoneOffset.UTC), Duration.ofMinutes(75));
        taskManagerTest.addTask(task2);

        Epic epic1 = new Epic("EpicTest 1", TaskStatus.NEW,"EpicTest 1 description");
        Epic epic2 = new Epic("EpicTest 2", TaskStatus.NEW,"EpicTest 2 description");
        taskManagerTest.addEpic(epic1);
        taskManagerTest.addEpic(epic2);

        Subtask subtask1 = new Subtask("TestSubtask 1", TaskStatus.NEW,"TestSubtask 1 description",
                LocalDateTime.parse("2015.05.20 12:00", formatter).toInstant(ZoneOffset.UTC), Duration.ofMinutes(60), 3);
        Subtask subtask2 = new Subtask("TestSubtask 2", TaskStatus.NEW,"TestSubtask 2 description",
                LocalDateTime.parse("2015.05.21 16:00", formatter).toInstant(ZoneOffset.UTC), Duration.ofMinutes(75), 3);
        taskManagerTest.addSubtask(subtask1);
        taskManagerTest.addSubtask(subtask2);
    }
}
