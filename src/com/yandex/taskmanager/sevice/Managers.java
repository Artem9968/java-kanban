package com.yandex.taskmanager.sevice;

public class Managers {

    private Managers() {        // добавил приватный конструктор без параметров
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}