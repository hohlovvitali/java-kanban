package http.handler;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.managerexception.ManagerSaveException;
import manager.managerexception.ManagerTaskNotFoundException;
import manager.taskmanager.InMemoryTaskManager;
import tasks.Epic;

import java.io.IOException;
import java.util.Optional;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    public EpicHandler(InMemoryTaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        EpicEndpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_EPICS:
                getEpics(exchange);
                break;
            case GET_EPIC:
                try {
                    getEpic(exchange);
                } catch (ManagerSaveException | ManagerTaskNotFoundException e) {
                    exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
                    exchange.sendResponseHeaders(500, 0);
                    exchange.close();
                    throw new RuntimeException(e);
                }
                break;
            case GET_EPIC_SUBTASKS:
                getEpicSubtasks(exchange);
                break;
            case POST_EPIC:
                postEpic(exchange);
                break;
            case DELETE_EPIC:
                deleteEpic(exchange);
                break;
            default:
                sendNotFound(exchange, "Такого эндпоинта не существует");
        }
    }

    private EpicEndpoint getEndpoint(String requestPath, String requestMethod){
        String[] pathSplit = requestPath.split("/");

        switch (requestMethod){
            case "DELETE":
                return EpicEndpoint.DELETE_EPIC;
            case "POST":
                return EpicEndpoint.POST_EPIC;
            case "GET":
                if (pathSplit.length == 2 && pathSplit[1].equals("epics")){
                    return EpicEndpoint.GET_EPICS;
                } else if (pathSplit.length == 3 && pathSplit[1].equals("epics")) {
                    return EpicEndpoint.GET_EPIC;
                } else if (pathSplit.length == 4 && pathSplit[1].equals("epics")) {
                    return EpicEndpoint.GET_EPIC_SUBTASKS;
                }
            default:
                return EpicEndpoint.UNKNOWN;
        }
    }

    private void deleteEpic(HttpExchange exchange) throws IOException {
        Optional<Integer> epicID = getTaskID(exchange);
        if(epicID.isEmpty()){
            sendNotFound(exchange, "Не указан id эпика для удаления");
            return;
        }

        try {
            taskManager.deleteEpicById(epicID.get());
            sendText(exchange, "Epic с номером " + epicID.get() + " был удален");
        } catch (ManagerTaskNotFoundException e){
            sendNotFound(exchange, "Epic с номером " + epicID.get() + " не существует");
        } catch (ManagerSaveException e) {
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(500, 0);
            exchange.close();
            throw new RuntimeException(e.getMessage());
        }
    }

    private void postEpic(HttpExchange exchange) throws IOException{
        try {
            String request = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
            Epic epic = gson.fromJson(request, Epic.class);
            taskManager.addEpic(epic);
            sendText(exchange, "Epic добавлен");
        } catch (JsonSyntaxException e){
            sendIncorrectJsonError(exchange);
        } catch (ManagerSaveException e) {
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(500, 0);
            exchange.close();
            throw new RuntimeException(e);
        }
    }

    private void getEpics(HttpExchange exchange) throws IOException {
        sendText(exchange, gson.toJson(taskManager.getAllEpics()));
    }

    private void getEpic(HttpExchange exchange) throws IOException, ManagerTaskNotFoundException, ManagerSaveException {
        Optional<Integer> epicID = getTaskID(exchange);
        try {
            if(epicID.isEmpty()){
                sendNotFound(exchange, "Некорректный id эпика для получения");
                return;
            }

            sendText(exchange, gson.toJson(taskManager.getEpicObjectByID(epicID.get())));
        } catch (ManagerTaskNotFoundException e){
            sendNotFound(exchange, "Epic с номером " + epicID.get() + " не существует");
        } catch (ManagerSaveException e) {
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(500, 0);
            exchange.close();
            throw new RuntimeException(e);
        }
    }

    private void getEpicSubtasks(HttpExchange exchange) throws IOException {
        Optional<Integer> epicID = getTaskID(exchange);
        try {
            if(epicID.isEmpty()){
                sendNotFound(exchange, "Некорректный id эпика для получения");
                return;
            }

            sendText(exchange, gson.toJson(taskManager.getEpicObjectByID(epicID.get()).getSubtaskIDList()));
        } catch (ManagerTaskNotFoundException e){
            sendNotFound(exchange, "Epic с номером " + epicID.get() + " не существует");
        } catch (ManagerSaveException e) {
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(500, 0);
            exchange.close();
            throw new RuntimeException(e);
        }
    }

    enum EpicEndpoint {GET_EPICS, GET_EPIC, GET_EPIC_SUBTASKS, POST_EPIC, DELETE_EPIC, UNKNOWN}
}
