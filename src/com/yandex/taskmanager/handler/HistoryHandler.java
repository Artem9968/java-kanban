package com.yandex.taskmanager.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.taskmanager.sevice.HistoryManager;
import com.yandex.taskmanager.sevice.TaskManager;

import java.io.IOException;
import java.net.URI;

public class HistoryHandler extends BaseHandler implements HttpHandler {
    TaskManager taskManager;
    HistoryManager historyManager;

    public HistoryHandler(TaskManager taskManager, HistoryManager historyManager) {
        this.taskManager = taskManager;
        this.historyManager = historyManager;
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        System.out.println("Началась обработка /history запроса от клиента.");
        String method = httpExchange.getRequestMethod();
        URI requestURI = httpExchange.getRequestURI();
        String path = requestURI.getPath();
        String[] splitStrings = path.split("/");
        switch (method) {
            case "GET": {
                if (splitStrings.length == 2) {
                    //response = gson.toJson(historyManager.getHistory().toString());
                    HistoryHandler.sendText(httpExchange, historyManager.getHistory().toString());
                }
                return;
            }
            default:
                HistoryHandler.sendHasError(httpExchange);
        }
    }
}
