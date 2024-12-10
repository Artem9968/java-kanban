package com.yandex.taskmarket.service;

import com.yandex.taskmanager.model.Epic;
import com.yandex.taskmanager.model.Status;
import com.yandex.taskmanager.model.SubTask;
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

    @Test
    void setHistoryManager2() {
        Epic epic1 = new Epic("Освоить Java", "Разобраться в JavaCore");
        taskManager.addEpic(epic1);
        SubTask subTask1 = new SubTask(1, "Прочитать теорию", "Написать конспект", Status.DONE);
        SubTask subTask2 = new SubTask(1, "Практика", "Написать код", Status.IN_PROGRESS);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        historyManager.add(epic1);
        historyManager.add(subTask1);
        historyManager.add(subTask2);
        taskManager.getEpicById(1);
        taskManager.getTaskById(2);
        taskManager.getSubTaskById(3);
        taskManager.addTask(task1);
        taskManager.getTaskById(1);
        assertEquals(3, historyManager.getHistory().size(), "Все задачи добавились в историю");
        historyManager.remove(1);
        assertEquals(2, historyManager.getHistory().size(), "Эпик удалился успешно");
        historyManager.remove(2);
        historyManager.remove(3);
        assertEquals(true, historyManager.getHistory().isEmpty(), "Все задачи удалены");
    }
}