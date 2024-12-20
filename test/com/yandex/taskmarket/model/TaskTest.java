package com.yandex.taskmarket.model;

import com.yandex.taskmanager.model.Status;
import com.yandex.taskmanager.model.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void taskEquals() {
        Task task1 = new Task("Потренироваться", "Выйти на пробежку", Status.IN_PROGRESS,1600, "01.11.20 12:15");
        Task task2 = new Task("Потренироваться", "Выйти на пробежку", Status.IN_PROGRESS,1600, "01.11.20 12:15");
        assertEquals(task1, task2, "Задачи не идентичны.");
    }
}