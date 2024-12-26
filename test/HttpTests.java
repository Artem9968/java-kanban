import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import com.yandex.taskmanager.handler.*;
import com.yandex.taskmanager.model.Epic;
import com.yandex.taskmanager.model.Status;
import com.yandex.taskmanager.model.SubTask;
import com.yandex.taskmanager.model.Task;
import com.yandex.taskmanager.sevice.HistoryManager;
import com.yandex.taskmanager.sevice.Managers;
import com.yandex.taskmanager.sevice.TaskManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.LocalDateTime;

public class HttpTests {


    static TaskManager taskManager = Managers.getDefault();
    static HistoryManager historyManager = Managers.getDefaultHistory();
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

    public HttpTests() {
        Task run = new Task("Потренироваться", "Выйти на пробежку", Status.IN_PROGRESS, 1600, LocalDateTime.of(2024, 12, 20, 10, 0, 0));
        Task swim = new Task("Поплавать", "Пойти в бассейн", Status.NEW, 1900, LocalDateTime.of(2023, 12, 20, 10, 0, 0));
        taskManager.addTask(run);
        taskManager.addTask(swim);

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
    }

    @BeforeAll
    static void start() throws IOException {
        HttpTests httpTest = new HttpTests();
        httpServer.bind(new InetSocketAddress(8080), 0);
        httpServer.createContext("/tasks", new TaskHandler(taskManager, historyManager));
        httpServer.createContext("/subtasks", new SubHandler(taskManager, historyManager));
        httpServer.createContext("/epics", new EpicHandler(taskManager, historyManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager, historyManager));
        httpServer.createContext("/prioritized", new PriorityHandler(taskManager, historyManager));

        httpServer.start();
    }

//    @Test
//    void testTasks() {
//        try {
//            HttpRequest request1 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks")).GET().build();
//        }
//        }
//
}
