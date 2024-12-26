package com.yandex.taskmanager.handler;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.taskmanager.model.Status;
import com.yandex.taskmanager.model.Task;
import com.yandex.taskmanager.sevice.HistoryManager;
import com.yandex.taskmanager.sevice.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TaskHandler extends BaseHandler implements HttpHandler {
    TaskManager taskManager;
    HistoryManager historyManager;

    public TaskHandler(TaskManager taskManager, HistoryManager historyManager) {
        this.taskManager = taskManager;
        this.historyManager = historyManager;
    }

    protected final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    public void handle(HttpExchange httpExchange) throws IOException {
        System.out.println("Началась обработка /tasks запроса от клиента.");
        String method = httpExchange.getRequestMethod();
        String response;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        URI requestURI = httpExchange.getRequestURI();
        String path = requestURI.getPath();
        String[] splitStrings = path.split("/");
        switch (method) {
            case "POST": {
                if (splitStrings.length == 3) {
                    if (taskManager.getTasksWithId().containsKey(Integer.parseInt(splitStrings[2]))) {
                        JsonElement jsonElement = JsonParser.parseString(new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        taskManager.updateTask(new Task(jsonObject.get("name").getAsString(), jsonObject.get("description").getAsString(), Status.valueOf(jsonObject.get("status").getAsString()), Integer.parseInt(jsonObject.get("duration").getAsString()), LocalDateTime.parse(jsonObject.get("time").getAsString(),formatter)));
                        TaskHandler.sendTextWithNoData(httpExchange);
                    } else TaskHandler.sendNotFound(httpExchange);
                } else {
                    JsonElement jsonElement = JsonParser.parseString(new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    taskManager.addTask(new Task(jsonObject.get("name").getAsString(), jsonObject.get("description").getAsString(), Status.valueOf(jsonObject.get("status").getAsString()), Integer.parseInt(jsonObject.get("duration").getAsString()), LocalDateTime.parse(jsonObject.get("time").getAsString(),formatter)));
                    TaskHandler.sendTextWithNoData(httpExchange);
                }
                return;
            }
            case "GET": {
                if (splitStrings.length == 3) {
                    if (taskManager.getTasksWithId().containsKey(Integer.parseInt(splitStrings[2]))) {
                        response = gson.toJson(taskManager.getTaskById(Integer.parseInt(splitStrings[2])).toString());
                        TaskHandler.sendText(httpExchange, response);
                    } else TaskHandler.sendNotFound(httpExchange);
                } else {
                    //response = gson.toJson(taskManager.getTasks().toString());
                    TaskHandler.sendText(httpExchange, taskManager.getTasksWithId().toString());
                }
                return;
            }
            case "DELETE": {
                if (splitStrings.length == 3 && taskManager.getTasksWithId().containsKey(Integer.parseInt(splitStrings[2]))) {
                    TaskHandler.sendText(httpExchange, "Задача №" + Integer.parseInt(splitStrings[2]) + " удалена");
                    taskManager.delTaskById(Integer.parseInt(splitStrings[2]));
                } else TaskHandler.sendNotFound(httpExchange);
                return;
            }
            default:
                TaskHandler.sendHasError(httpExchange);
        }
    }
}