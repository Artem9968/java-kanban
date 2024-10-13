package com.yandex.test;

import com.yandex.taskmanager.model.Epic;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {

    @Test
    void epicEquals() {
        Epic epic1 = new Epic("Освоить Java", "Разобраться в JavaCore");
        Epic epic2 = new Epic("Освоить Java", "Разобраться в JavaCore");
        assertEquals(epic1, epic2, "Задачи не идентичны.");
    }
}