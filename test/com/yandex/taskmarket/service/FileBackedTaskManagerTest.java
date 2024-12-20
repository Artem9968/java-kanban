package com.yandex.taskmarket.service;

import com.yandex.taskmanager.exception.ManagerSaveException;
import com.yandex.taskmanager.model.Epic;
import com.yandex.taskmanager.model.Status;
import com.yandex.taskmanager.model.SubTask;
import com.yandex.taskmanager.model.Task;
import com.yandex.taskmanager.sevice.FileBackedTaskManager;
import com.yandex.taskmanager.sevice.InMemoryTaskManager;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FileBackedTaskManagerTest {
    private FileBackedTaskManager fileBackedTaskManager;

    @Test
    void testLoad() throws IOException {
        File tempFile = File.createTempFile("testFileLoad", ".csv");

        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(tempFile);

        Task run2 = new Task("Потренироваться", "Выйти на пробежку", Status.IN_PROGRESS, 1600, LocalDateTime.of(2024, 12, 20, 10, 0, 0));
        Task swim2 = new Task("Поплавать", "Пойти в бассейн", Status.NEW, 1600, LocalDateTime.of(2023, 12, 20, 10, 0, 0));

        fileBackedTaskManager.addTask(run2);
        fileBackedTaskManager.addTask(swim2);
        Epic checkCode2 = new Epic("Проверить код", "Проверить все методы классов");
        SubTask readTheory2 = new SubTask(3, "Прочитать теорию", "Написать конспект", Status.NEW, 1600, LocalDateTime.of(2022, 12, 20, 10, 0, 0));
        fileBackedTaskManager.addEpic(checkCode2);
        fileBackedTaskManager.addSubTask(readTheory2);

        InMemoryTaskManager manager = FileBackedTaskManager.loadFromFile(tempFile);

        List<Task> tasks = manager.getTasks();
        List<Epic> epics = manager.getEpics();
        List<SubTask> subTasks = manager.getSubTasks();

        assertEquals("Потренироваться", tasks.getFirst().getName(), "Загрузка из файла не работает");
        assertEquals("IN_PROGRESS", tasks.getFirst().getStatus().name(), "Загрузка из файла не работает");
        assertEquals("Проверить код", epics.getFirst().getName(), "Загрузка из файла не работает");
        assertEquals("NEW", epics.getFirst().getStatus().name(), "Загрузка из файла не работает");
        assertEquals("Прочитать теорию", subTasks.getFirst().getName(), "Загрузка из файла не работает");
        assertEquals("NEW", subTasks.getFirst().getStatus().name(), "Загрузка из файла не работает");
    }

    @Test
    void testSave() throws IOException {
        File tempFile = File.createTempFile("testFileSave", ".csv");
        tempFile.deleteOnExit(); // чтобы удалить после завершения тестов
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(tempFile);
        Task run = new Task("Потренироваться", "Выйти на пробежку", Status.IN_PROGRESS, 1600, LocalDateTime.of(2021, 12, 20, 10, 0, 0));

        Epic checkCode = new Epic("Проверить код", "Проверить все методы классов");
        SubTask readTheory = new SubTask(2, "Прочитать теорию", "Написать конспект", Status.DONE, 1600, LocalDateTime.of(2020, 12, 20, 10, 0, 0));
        fileBackedTaskManager.addTask(run);
        fileBackedTaskManager.addEpic(checkCode);
        fileBackedTaskManager.addSubTask(readTheory);
        try (BufferedReader reader = new BufferedReader(new FileReader(tempFile))) {
            String line;
            boolean taskFound = false;
            boolean epicFound = false;
            boolean subtaskFound = false;

            while ((line = reader.readLine()) != null) {
                if (line.contains("Потренироваться")) taskFound = true;
                if (line.contains("Проверить код")) epicFound = true;
                if (line.contains("Прочитать теорию")) subtaskFound = true;
            }
            assertTrue(taskFound, "Файл записался неверно");
            assertTrue(epicFound, "Файл записался неверно");
            assertTrue(subtaskFound, "Файл записался неверно");
        }
    }
}
