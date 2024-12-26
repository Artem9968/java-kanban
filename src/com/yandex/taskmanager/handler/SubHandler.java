package com.yandex.taskmanager.handler;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.taskmanager.model.Status;
import com.yandex.taskmanager.model.SubTask;
import com.yandex.taskmanager.sevice.HistoryManager;
import com.yandex.taskmanager.sevice.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SubHandler extends BaseHandler implements HttpHandler {
    TaskManager taskManager;
    HistoryManager historyManager;

    public SubHandler(TaskManager taskManager, HistoryManager historyManager) {
        this.taskManager = taskManager;
        this.historyManager = historyManager;
    }

    protected final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    public void handle(HttpExchange httpExchange) throws IOException {
        System.out.println("Началась обработка /subtasks запроса от клиента.");
        String method = httpExchange.getRequestMethod();
        String response;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        URI requestURI = httpExchange.getRequestURI();
        String path = requestURI.getPath();
        String[] splitStrings = path.split("/");
        switch (method) {
            case "POST": {
                if (splitStrings.length == 3) {
                    if (taskManager.getSubTasksWithId().containsKey(Integer.parseInt(splitStrings[2]))) {
                        JsonElement jsonElement = JsonParser.parseString(new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        taskManager.updateSubTask(new SubTask(Integer.parseInt(splitStrings[2]), jsonObject.get("name").getAsString(), jsonObject.get("description").getAsString(), Status.valueOf(jsonObject.get("status").getAsString()), Integer.parseInt(jsonObject.get("duration").getAsString()), LocalDateTime.parse(jsonObject.get("time").getAsString(), formatter)));
                        SubHandler.sendTextWithNoData(httpExchange);
                    } else SubHandler.sendNotFound(httpExchange);
                } else {
                    JsonElement jsonElement = JsonParser.parseString(new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    taskManager.addSubTask(new SubTask(Integer.parseInt(splitStrings[2]), jsonObject.get("name").getAsString(), jsonObject.get("description").getAsString(), Status.valueOf(jsonObject.get("status").getAsString()), Integer.parseInt(jsonObject.get("duration").getAsString()), LocalDateTime.parse(jsonObject.get("time").getAsString(), formatter)));
                    TaskHandler.sendTextWithNoData(httpExchange);
                }
                return;
            }
            case "GET": {
                if (splitStrings.length == 3) {
                    if (taskManager.getSubTasksWithId().containsKey(Integer.parseInt(splitStrings[2]))) {
                        response = gson.toJson(taskManager.getSubTaskById(Integer.parseInt(splitStrings[2])).toString());
                        TaskHandler.sendText(httpExchange, response);
                    } else TaskHandler.sendNotFound(httpExchange);
                } else {
                    TaskHandler.sendText(httpExchange, taskManager.getSubTasksWithId().toString());
                }
                return;
            }
            case "DELETE": {
                if (splitStrings.length == 3 && taskManager.getSubTasksWithId().containsKey(Integer.parseInt(splitStrings[2]))) {
                    TaskHandler.sendText(httpExchange, "Задача №" + Integer.parseInt(splitStrings[2]) + " удалена");
                    taskManager.delSubTaskById(Integer.parseInt(splitStrings[2]));
                } else TaskHandler.sendNotFound(httpExchange);
                return;
            }
            default:
                TaskHandler.sendHasError(httpExchange);
        }
    }
}