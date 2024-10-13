package com.yandex.taskmarket.model;

import com.yandex.taskmanager.model.Status;
import com.yandex.taskmanager.model.SubTask;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {

    @Test
    void subTaskEquals() {
        SubTask subTask1 = new SubTask(3, "Прочитать теорию", "Написать конспект", Status.DONE);
        SubTask subTask2 = new SubTask(3, "Прочитать теорию", "Написать конспект", Status.DONE);
        assertEquals(subTask1, subTask2, "Задачи не идентичны.");
    }
}