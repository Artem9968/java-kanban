package com.yandex.taskmanager.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.taskmanager.sevice.HistoryManager;
import com.yandex.taskmanager.sevice.TaskManager;

import java.io.IOException;
import java.net.URI;

public class PriorityHandler extends  BaseHandler implements HttpHandler {
    private TaskManager taskManager;
    private HistoryManager historyManager;

    public PriorityHandler(TaskManager taskManager, HistoryManager historyManager) {
        this.taskManager = taskManager;
        this.historyManager = historyManager;
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        System.out.println("Началась обработка /prioritized запроса от клиента.");
        String method = httpExchange.getRequestMethod();
        String response;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        URI requestURI = httpExchange.getRequestURI();
        String path = requestURI.getPath();
        String[] splitStrings = path.split("/");
        switch (method) {
            case "GET": {
                if (splitStrings.length == 2) {
                    response = gson.toJson(taskManager.getPrioritizedTasks().toString());
                    TaskHandler.sendText(httpExchange, response);
                }
                return;
            }
            default:
                TaskHandler.sendHasError(httpExchange);
        }
    }
}