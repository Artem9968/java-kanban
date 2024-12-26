package com.yandex.taskmanager.handler;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.taskmanager.model.Epic;
import com.yandex.taskmanager.sevice.HistoryManager;
import com.yandex.taskmanager.sevice.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class EpicHandler extends BaseHandler implements HttpHandler {
    TaskManager taskManager;
    HistoryManager historyManager;

    public EpicHandler(TaskManager taskManager, HistoryManager historyManager) {
        this.taskManager = taskManager;
        this.historyManager = historyManager;
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        System.out.println("Началась обработка /epics запроса от клиента.");
        String method = httpExchange.getRequestMethod();
        String response;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        URI requestURI = httpExchange.getRequestURI();
        String path = requestURI.getPath();
        String[] splitStrings = path.split("/");
        switch (method) {
            case "POST": {
                if (splitStrings.length == 3) {
                    if (taskManager.getEpicsWithId().containsKey(Integer.parseInt(splitStrings[2]))) {
                        JsonElement jsonElement = JsonParser.parseString(new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        taskManager.updateEpic(new Epic(jsonObject.get("name").getAsString(), jsonObject.get("description").getAsString()));
                        EpicHandler.sendTextWithNoData(httpExchange);
                    } else EpicHandler.sendNotFound(httpExchange);
                } else {
                    JsonElement jsonElement = JsonParser.parseString(new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    taskManager.addEpic(new Epic(jsonObject.get("name").getAsString(), jsonObject.get("description").getAsString()));
                    EpicHandler.sendTextWithNoData(httpExchange);
                }
                return;
            }
            case "GET": {
                if (splitStrings.length == 3) {
                    if (taskManager.getEpicsWithId().containsKey(Integer.parseInt(splitStrings[2]))) {
                        response = gson.toJson(taskManager.getEpicById(Integer.parseInt(splitStrings[2])).toString());
                        EpicHandler.sendText(httpExchange, response);
                    } else EpicHandler.sendNotFound(httpExchange);
                    return;
                } else if (splitStrings.length == 4 && splitStrings[3].equals("subtasks")) {
                    if (taskManager.getEpicsWithId().containsKey(Integer.parseInt(splitStrings[2]))) {
                        response = gson.toJson(taskManager.getSubsByEpicId(Integer.parseInt(splitStrings[2])).toString());
                        EpicHandler.sendText(httpExchange, response);
                    } else EpicHandler.sendNotFound(httpExchange);
                    return;
                } else {
                    EpicHandler.sendText(httpExchange, taskManager.getEpicsWithId().toString());
                    return;
                }
            }
            case "DELETE": {
                if (splitStrings.length == 3 && taskManager.getEpicsWithId().containsKey(Integer.parseInt(splitStrings[2]))) {
                    EpicHandler.sendText(httpExchange, "Задача №" + Integer.parseInt(splitStrings[2]) + " удалена");
                    taskManager.delEpicById(Integer.parseInt(splitStrings[2]));
                } else EpicHandler.sendNotFound(httpExchange);
                return;
            }
            default:
                EpicHandler.sendHasError(httpExchange);
        }
    }
}