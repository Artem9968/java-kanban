package com.yandex.taskmarket;

import com.yandex.taskmanager.model.Status;
import com.yandex.taskmanager.model.Task;
import com.yandex.taskmanager.sevice.HistoryManager;
import com.yandex.taskmanager.sevice.Managers;
import com.yandex.taskmanager.sevice.TaskManager;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    TaskManager taskManager = Managers.getDefault();
    HistoryManager historyManager = Managers.getDefaultHistory();

    Task task1 = new Task("Потренироваться", "Выйти на пробежку", Status.IN_PROGRESS, 1600, LocalDateTime.of(2024, 12, 20, 10, 0, 0));

    @Test
    void historyManagerTest() {

        historyManager.add(task1);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
        assertEquals(historyManager.getHistory().get(0), task1, "Задачи не идентичны.");
    }
}