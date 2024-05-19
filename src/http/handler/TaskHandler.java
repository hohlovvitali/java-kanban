package http.handler;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.managerexception.ManagerSaveException;
import manager.managerexception.ManagerTaskNotFoundException;
import manager.managerexception.ManagerValidateException;
import manager.taskmanager.InMemoryTaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.Optional;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    public TaskHandler(InMemoryTaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        TaskEndpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_TASKS:
                getTasks(exchange);
                break;
            case GET_TASK:
                try {
                    getTask(exchange);
                } catch (ManagerTaskNotFoundException | ManagerSaveException e) {
                    exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
                    exchange.sendResponseHeaders(500, 0);
                    exchange.close();
                    throw new RuntimeException(e);
                }
                break;
            case POST_TASK:
                postTask(exchange);
                break;
            case DELETE_TASK:
                deleteTask(exchange);
                break;
            default:
                sendNotFound(exchange, "Такого эндпоинта не существует");
        }
    }

    private TaskEndpoint getEndpoint(String requestPath, String requestMethod){
        String[] pathSplit = requestPath.split("/");

        switch (requestMethod){
            case "DELETE":
                return TaskEndpoint.DELETE_TASK;
            case "POST":
                return TaskEndpoint.POST_TASK;
            case "GET":
                if (pathSplit.length == 2 && pathSplit[1].equals("tasks")){
                    return TaskEndpoint.GET_TASKS;
                } else if (pathSplit.length == 3 && pathSplit[1].equals("tasks")) {
                    return TaskEndpoint.GET_TASK;
                }
            default:
                return TaskEndpoint.UNKNOWN;
        }
    }

    private void deleteTask(HttpExchange exchange) throws IOException {
        Optional<Integer> taskID = getTaskID(exchange);
        if(taskID.isEmpty()){
            sendNotFound(exchange, "Не указан id эпика для удаления");
            return;
        }

        try {
            taskManager.deleteTaskById(taskID.get());
            sendText(exchange, "Task с номером " + taskID.get() + " был удален");
        } catch (ManagerTaskNotFoundException e){
            sendNotFound(exchange, "Task с номером " + taskID.get() + " не существует");
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void postTask(HttpExchange exchange) throws IOException{
        Optional<Integer> taskID = getTaskID(exchange);

        try {
            String request = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
            Task task = gson.fromJson(request, Task.class);
            if (taskID.isEmpty()){
                taskManager.addTask(task);
                sendText(exchange, "Task добавлен");
            } else {
                taskManager.getTaskObjectByID(taskID.get());
                taskManager.updateTask(task);

                exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
                exchange.sendResponseHeaders(201, 0);
                exchange.close();
            }
        } catch (JsonSyntaxException e){
            sendIncorrectJsonError(exchange);
        } catch (ManagerSaveException e) {
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(500, 0);
            exchange.close();
            throw new RuntimeException(e);
        } catch (ManagerTaskNotFoundException e){
            sendNotFound(exchange, "Task с номером " + taskID.get() + " не существует");
        } catch (ManagerValidateException e) {
            String output = taskID.map(integer -> "Обновленная задача с номером " + integer + " пересекается с уже существующими задачами").orElse("Новая Task пересекатеся с уже существующими задачами");
            sendHasInteractions(exchange, output);
        }
    }

    private void getTasks(HttpExchange exchange) throws IOException {
        sendText(exchange, gson.toJson(taskManager.getAllTasks()));
    }

    private void getTask(HttpExchange exchange) throws IOException, ManagerTaskNotFoundException, ManagerSaveException {
        Optional<Integer> taskID = getTaskID(exchange);
        try {
            if(taskID.isEmpty()){
                sendNotFound(exchange, "Некорректный id эпика для получения");
                return;
            }

            sendText(exchange, gson.toJson(taskManager.getTaskObjectByID(taskID.get())));
        } catch (ManagerTaskNotFoundException e){
            sendNotFound(exchange, "Task с номером " + taskID.get() + " не существует");
        } catch (ManagerSaveException e) {
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(500, 0);
            exchange.close();
            throw new RuntimeException(e);
        }
    }

    enum TaskEndpoint {GET_TASKS, GET_TASK, POST_TASK, DELETE_TASK, UNKNOWN}
}
