package http.handler;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.managerexception.ManagerSaveException;
import manager.managerexception.ManagerTaskNotFoundException;
import manager.managerexception.ManagerValidateException;
import manager.taskmanager.InMemoryTaskManager;
import tasks.Subtask;

import java.io.IOException;
import java.util.Optional;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
    public SubtaskHandler(InMemoryTaskManager taskManager) throws ManagerSaveException {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        SubtaskEndpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_SUBTASKS:
                getSubtasks(exchange);
                break;
            case GET_SUBTASK:
                try {
                    getSubtask(exchange);
                } catch (ManagerTaskNotFoundException e) {
                    exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
                    exchange.sendResponseHeaders(500, 0);
                    exchange.close();
                    throw new RuntimeException(e);
                } catch (ManagerSaveException e) {
                    throw new RuntimeException(e);
                }
                break;
            case POST_SUBTASK:
                postSubtask(exchange);
                break;
            case DELETE_SUBTASK:
                deleteSubtask(exchange);
                break;
            default:
                sendNotFound(exchange, "Такого эндпоинта не существует");
        }
    }

    private SubtaskEndpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathSplit = requestPath.split("/");

        switch (requestMethod) {
            case "DELETE":
                return SubtaskEndpoint.DELETE_SUBTASK;
            case "POST":
                return SubtaskEndpoint.POST_SUBTASK;
            case "GET":
                if (pathSplit.length == 2 && pathSplit[1].equals("subtasks")) {
                    return SubtaskEndpoint.GET_SUBTASKS;
                } else if (pathSplit.length == 3 && pathSplit[1].equals("subtasks")) {
                    return SubtaskEndpoint.GET_SUBTASK;
                }
            default:
                return SubtaskEndpoint.UNKNOWN;
        }
    }

    private void deleteSubtask(HttpExchange exchange) throws IOException {
        Optional<Integer> taskID = getTaskID(exchange);
        if (taskID.isEmpty()) {
            sendNotFound(exchange, "Не указан id эпика для удаления");
            return;
        }

        try {
            taskManager.deleteSubtaskById(taskID.get());
            sendText(exchange, "Subtask с номером " + taskID.get() + " был удален");
        } catch (ManagerTaskNotFoundException e) {
            sendNotFound(exchange, "Subtask с номером " + taskID.get() + " не существует");
        } catch (ManagerSaveException e) {
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(500, 0);
            exchange.close();
            throw new RuntimeException(e.getMessage());
        }
    }

    private void postSubtask(HttpExchange exchange) throws IOException {
        Optional<Integer> taskID = getTaskID(exchange);

        try {
            String request = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
            Subtask subtask = gson.fromJson(request, Subtask.class);
            if (taskID.isEmpty()) {
                taskManager.addSubtask(subtask);
                sendText(exchange, "Subtask добавлен");
            } else {
                taskManager.getSubtaskObjectByID(taskID.get());
                taskManager.updateSubtask(subtask);

                exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
                exchange.sendResponseHeaders(201, 0);
                exchange.close();
            }
        } catch (JsonSyntaxException e) {
            sendIncorrectJsonError(exchange);
        } catch (ManagerSaveException e) {
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(500, 0);
            exchange.close();
            throw new RuntimeException(e);
        } catch (ManagerTaskNotFoundException e) {
            sendNotFound(exchange, "Subtask с номером " + taskID.get() + " не существует");
        } catch (ManagerValidateException e) {
            String output = "";
            output = taskID.map(integer -> "Обновленная задача с номером " + integer + " пересекается с уже существующими задачами").orElse("Новая Subtask пересекатеся с уже существующими задачами");
            sendHasInteractions(exchange, output);
        }
    }

    private void getSubtasks(HttpExchange exchange) throws IOException {
        sendText(exchange, gson.toJson(taskManager.getAllSubtasks()));
    }

    private void getSubtask(HttpExchange exchange) throws IOException, ManagerTaskNotFoundException, ManagerSaveException {
        Optional<Integer> taskID = getTaskID(exchange);
        try {
            if (taskID.isEmpty()) {
                sendNotFound(exchange, "Некорректный id эпика для получения");
                return;
            }

            sendText(exchange, gson.toJson(taskManager.getSubtaskObjectByID(taskID.get())));
        } catch (ManagerTaskNotFoundException e) {
            sendNotFound(exchange, "Subtask с номером " + taskID.get() + " не существует");
        } catch (ManagerSaveException e) {
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(500, 0);
            exchange.close();
            throw new RuntimeException(e);
        }
    }

    enum SubtaskEndpoint {GET_SUBTASKS, GET_SUBTASK, POST_SUBTASK, DELETE_SUBTASK, UNKNOWN}
}
