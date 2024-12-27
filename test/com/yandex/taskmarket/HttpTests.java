package com.yandex.taskmarket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import com.yandex.taskmanager.handler.*;
import com.yandex.taskmanager.model.Epic;
import com.yandex.taskmanager.model.Status;
import com.yandex.taskmanager.model.SubTask;
import com.yandex.taskmanager.model.Task;
import com.yandex.taskmanager.sevice.HistoryManager;
import com.yandex.taskmanager.sevice.InMemoryTaskManager;
import com.yandex.taskmanager.sevice.Managers;
import com.yandex.taskmanager.sevice.TaskManager;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpTests {

    static HttpServer httpServer;
    static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    static HttpClient client = HttpClient.newHttpClient();

    static {
        try {
            httpServer = HttpServer.create();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final TaskManager taskManager = new InMemoryTaskManager();

    @BeforeAll
    static void start() throws IOException {

        HistoryManager historyManager = Managers.getDefaultHistory();

        Task run = new Task("Потренироваться", "Выйти на пробежку", Status.IN_PROGRESS, 1600, LocalDateTime.of(2024, 12, 20, 10, 0, 0));
        Task swim = new Task("Поплавать", "Пойти в бассейн", Status.NEW, 1900, LocalDateTime.of(2023, 12, 20, 10, 0, 0));
        taskManager.addTask(run);
        taskManager.addTask(swim);
        System.out.println(taskManager.getTasks());

        Epic learnJava = new Epic("Освоить Java", "Разобраться в JavaCore");
        taskManager.addEpic(learnJava);

        SubTask readTheory = new SubTask(3, "Прочитать теорию", "Написать конспект", Status.DONE, 1600, LocalDateTime.of(2022, 12, 20, 10, 0, 0));
        SubTask practicum = new SubTask(3, "Практика", "Написать код", Status.IN_PROGRESS, 1600, LocalDateTime.of(2021, 12, 20, 10, 0, 0));
        taskManager.addSubTask(readTheory);
        taskManager.addSubTask(practicum);

        Epic checkCode = new Epic("Проверить код", "Проверить все методы классов");
        taskManager.addEpic(checkCode);

        SubTask useDebug = new SubTask(6, "Использовать дебаг", "Пройтись дебагом", Status.DONE, 1600, LocalDateTime.of(2020, 12, 20, 10, 0, 0));
        taskManager.addSubTask(useDebug);

        historyManager.add(taskManager.getTaskById(1));
        historyManager.add(taskManager.getTaskById(2));
        historyManager.add(taskManager.getSubTaskById(4));
        historyManager.add(taskManager.getSubTaskById(5));
        historyManager.add(taskManager.getEpicById(3));
        historyManager.add(taskManager.getEpicById(6));
        historyManager.add(taskManager.getTaskById(1));
        historyManager.add(taskManager.getTaskById(2));
        historyManager.add(taskManager.getSubTaskById(4));
        historyManager.add(taskManager.getSubTaskById(5));
        historyManager.add(taskManager.getEpicById(3));
        historyManager.add(taskManager.getEpicById(6));
        historyManager.add(taskManager.getTaskById(1));
        httpServer.bind(new InetSocketAddress(8080), 0);
        httpServer.createContext("/tasks", new TaskHandler(taskManager, historyManager));
        httpServer.createContext("/subtasks", new SubHandler(taskManager, historyManager));
        httpServer.createContext("/epics", new EpicHandler(taskManager, historyManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager, historyManager));
        httpServer.createContext("/prioritized", new PriorityHandler(taskManager, historyManager));

        httpServer.start();
    }

    @Test
    public void testTasks() {
        try {
            HttpRequest request1 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks")).GET().build();
            HttpRequest request2 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/subtasks/99")).GET().build();
            HttpRequest request3 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/999")).GET().build();
            HttpRequest request4 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/100")).POST(HttpRequest.BodyPublishers.ofString(gson.toJson("{\"name\": \"Приготовить еду\", \"description\": \"Нарезать овощи\", \"status\": \"NEW\", \"duration\": \"1600\", \"time\": \"16.12.24 13:15\"}"))).build();
            HttpRequest request5 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/101")).POST(HttpRequest.BodyPublishers.ofString(gson.toJson("{\"name\": \"Приготовить еду\", \"description\": \"Нарезать овощи\", \"status\": \"NEW\", \"duration\": \"1600\", \"time\": \"24.12.24 13:15\"}"))).build();
            HttpRequest request6 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/100")).DELETE().build();
            HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> response4 = client.send(request4, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> response5 = client.send(request5, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> response6 = client.send(request6, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response1.statusCode());
            assertTrue(response2.statusCode() == 200);
            assertTrue(response3.statusCode() == 404);
            assertTrue(response4.statusCode() == 201);
            assertTrue(response5.statusCode() == 201);
            assertTrue(response6.statusCode() == 200);
        } catch (IOException | InterruptedException e) {
        }
    }

    @Test
    public void testSubs() {
        try {
            HttpRequest request1 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/subtasks")).GET().build();
            HttpRequest request2 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/subtasks/1626573417")).GET().build();
            HttpRequest request3 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/1626573412298")).GET().build();
            HttpRequest request4 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/subtasks/-772151814")).POST(HttpRequest.BodyPublishers.ofString(gson.toJson("{\"epicid\":\"50\", \"name\": \"Приготовить еду\", \"description\": \"Нарезать овощи\", \"status\": \"NEW\", \"duration\": \"1470\", \"time\": \"10.12.24 23:10\"}"))).build();
            HttpRequest request5 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/subtasks")).POST(HttpRequest.BodyPublishers.ofString(gson.toJson("{\"epicid\":\"50\", \"name\": \"Приготовить еду\", \"description\": \"Нарезать овощи\", \"status\": \"NEW\", \"duration\": \"1470\", \"time\": \"11.12.24 13:55\"}"))).build();
            HttpRequest request6 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/subtasks/-772151814")).DELETE().build();
            HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> response4 = client.send(request4, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> response5 = client.send(request5, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> response6 = client.send(request6, HttpResponse.BodyHandlers.ofString());
            assertTrue(response1.statusCode() == 200);
            assertTrue(response2.statusCode() == 200);
            assertTrue(response3.statusCode() == 404);
            assertTrue(response4.statusCode() == 201);
            assertTrue(response5.statusCode() == 201);
            assertTrue(response6.statusCode() == 200);
        } catch (IOException | InterruptedException e) {
        }
    }

    @Test
    public void testEpics() {
        try {
            HttpRequest request1 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/epics")).GET().build();
            HttpRequest request2 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/epics/200")).GET().build();
            HttpRequest request3 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/201")).GET().build();
            HttpRequest request7 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/epics/200/subtasks")).GET().build();
            HttpRequest request4 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/epics/200")).POST(HttpRequest.BodyPublishers.ofString(gson.toJson("{\"name\": \"Приготовить еду\", \"description\": \"Нарезать овощи\"}"))).build();
            HttpRequest request5 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/epics")).POST(HttpRequest.BodyPublishers.ofString(gson.toJson("{\"name\": \"Приготовить еду\", \"description\": \"Нарезать овощи\"}"))).build();
            HttpRequest request6 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/epics/200")).DELETE().build();
            HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> response4 = client.send(request4, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> response5 = client.send(request5, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> response6 = client.send(request6, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> response7 = client.send(request7, HttpResponse.BodyHandlers.ofString());
            assertTrue(response1.statusCode() == 200);
            assertTrue(response2.statusCode() == 200);
            assertTrue(response3.statusCode() == 404);
            assertTrue(response4.statusCode() == 201);
            assertTrue(response5.statusCode() == 201);
            assertTrue(response6.statusCode() == 200);
            assertTrue(response7.statusCode() == 200);
        } catch (IOException | InterruptedException e) {
        }
    }

    @Test
    public void testTimeSort() {
        try {
            HttpRequest request1 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/prioritized")).GET().build();
            HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
            assertTrue(response1.statusCode() == 200);
        } catch (IOException | InterruptedException e) {
        }
    }

    @Test
    public void testHistory() {
        try {
            HttpRequest request1 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/history")).GET().build();
            HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
            assertTrue(response1.statusCode() == 200);
        } catch (IOException | InterruptedException e) {
        }
    }

    @AfterAll
    static void stop() {
        httpServer.stop(1);
    }
}