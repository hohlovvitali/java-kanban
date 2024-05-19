package http.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.managerexception.ManagerSaveException;
import manager.taskmanager.InMemoryTaskManager;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    public HistoryHandler(InMemoryTaskManager taskManager) throws ManagerSaveException {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String[] pathSplit = exchange.getRequestURI().getPath().split("/");

        if (exchange.getRequestMethod().equals("GET")) {
            if (pathSplit.length == 2) {
                sendText(exchange, gson.toJson(taskManager.getHistory()));
                return;
            }
        }
        sendNotFound(exchange, "Такого эндпоинта не существует");
    }
}
