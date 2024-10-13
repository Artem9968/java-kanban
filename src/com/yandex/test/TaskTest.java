package com.yandex.test;

import com.yandex.taskmanager.model.Status;
import com.yandex.taskmanager.model.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void taskEquals() {
        Task task1 = new Task("Потренироваться", "Выйти на пробежку", Status.IN_PROGRESS);
        Task task2 = new Task("Потренироваться", "Выйти на пробежку", Status.IN_PROGRESS);
        assertEquals(task1, task2, "Задачи не идентичны.");
    }
}