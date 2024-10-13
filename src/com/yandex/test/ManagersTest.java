package com.yandex.test;

import com.yandex.taskmanager.sevice.Managers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void getDefault() {
        assertNotNull(Managers.getDefault(), "Класс  InMemoryTaskManager не проинициализирован.");
    }

    @Test
    void getDefaultHistory() {
        assertNotNull(Managers.getDefaultHistory(), "Класс InMemoryHistoryManager не проинициализирован.");
    }
}