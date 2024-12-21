package com.yandex.taskmarket.model;

import com.yandex.taskmanager.model.Status;
import com.yandex.taskmanager.model.Task;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void taskEquals() {
        Task task1 = new Task("Потренироваться", "Выйти на пробежку", Status.IN_PROGRESS, 1600, LocalDateTime.of(2024, 12, 20, 10, 0, 0));
        Task task2 = new Task("Потренироваться", "Выйти на пробежку", Status.IN_PROGRESS, 1600, LocalDateTime.of(2024, 12, 20, 10, 0, 0));
        assertEquals(task1, task2, "Задачи не идентичны.");
    }
}