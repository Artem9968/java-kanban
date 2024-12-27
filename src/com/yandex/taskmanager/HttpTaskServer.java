package com.yandex.taskmanager;

import com.sun.net.httpserver.HttpServer;
import com.yandex.taskmanager.handler.TaskHandler;
import com.yandex.taskmanager.handler.SubHandler;
import com.yandex.taskmanager.handler.EpicHandler;
import com.yandex.taskmanager.handler.HistoryHandler;
import com.yandex.taskmanager.handler.PriorityHandler;

import com.yandex.taskmanager.sevice.HistoryManager;
import com.yandex.taskmanager.sevice.Managers;
import com.yandex.taskmanager.sevice.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static HttpServer httpServer;

    protected static TaskManager taskManager = Managers.getDefault();
    protected static HistoryManager historyManager = Managers.getDefaultHistory();

    public HttpTaskServer() throws IOException {

        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler(taskManager, historyManager));
        httpServer.createContext("/subtasks", new SubHandler(taskManager, historyManager));
        httpServer.createContext("/epics", new EpicHandler(taskManager, historyManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager, historyManager));
        httpServer.createContext("/prioritized", new PriorityHandler(taskManager, historyManager));
    }

    public static void main(String[] args) throws IOException {          // перенес метод main
        System.out.println("-----Работа сервера------");
        final HttpTaskServer server = new HttpTaskServer();
        server.start();

        System.out.println("-----Конец работы сервера------");
    }

    private void start() {
        httpServer.start(); // запускаем сервер
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }
}
