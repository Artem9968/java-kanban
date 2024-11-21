package com.yandex.taskmarket.service;

import com.yandex.taskmanager.model.Status;
import com.yandex.taskmanager.model.Task;
import com.yandex.taskmanager.sevice.HistoryManager;
import com.yandex.taskmanager.sevice.Managers;
import com.yandex.taskmanager.sevice.TaskManager;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InMemoryHistoryManagerTest {


    HistoryManager historyManager = Managers.getDefaultHistory();
    TaskManager taskManager = Managers.getDefault();

    Task task1 = new Task("Потренироваться", "Выйти на пробежку", Status.IN_PROGRESS);

    Task task2 = new Task("Поплавать", "Пойти в бассейн", Status.NEW);

    @Test
    void historyManagerTest() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        historyManager.add(task1);
        historyManager.add(task2);

        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(2, history.size(), "История не пустая.");
        assertEquals(historyManager.getHistory().getFirst(), task1, "Задачи не идентичны.");
        historyManager.remove(1);
        assertEquals(1, historyManager.getHistory().size(), "Удаление задач из истории работает");
    }
}