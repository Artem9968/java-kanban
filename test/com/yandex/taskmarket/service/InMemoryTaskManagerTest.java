package com.yandex.taskmarket.service;

import com.yandex.taskmanager.exception.ManagerSaveException;
import com.yandex.taskmanager.model.Epic;
import com.yandex.taskmanager.model.Status;
import com.yandex.taskmanager.model.SubTask;
import com.yandex.taskmanager.model.Task;
import com.yandex.taskmanager.sevice.InMemoryTaskManager;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    InMemoryTaskManager taskManager = new InMemoryTaskManager();
    Task run = new Task("Потренироваться", "Выйти на пробежку", Status.DONE, 1400, LocalDateTime.of(2024, 12, 20, 10, 0, 0));
    Epic learnJava = new Epic("Освоить Java", "Разобраться в JavaCore");
    Task run2 = new Task("Потренироваться", "Выйти на пробежку", Status.IN_PROGRESS, 1400, LocalDateTime.of(2024, 12, 20, 10, 0, 0));

    @Test
    void addNewTask() {
        taskManager.addTask(run);

        final Task savedTask = taskManager.getTaskById(run.getId());

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(run, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(run, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void addNewEpic() {
        taskManager.addEpic(learnJava);

        final Epic savedEpic = taskManager.getEpicById(learnJava.getId());

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(learnJava, savedEpic, "Задачи не совпадают.");

        final List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(learnJava, epics.getFirst(), "Задачи не совпадают.");
    }

    @Test
    void addNewSubTask() {
        taskManager.addEpic(learnJava);
        SubTask readTheory = new SubTask(learnJava.getId(), "Прочитать теорию", "Написать конспект", Status.DONE, 1600, LocalDateTime.of(2022, 12, 20, 10, 0, 0));
        taskManager.addSubTask(readTheory);
        SubTask practicum = new SubTask(learnJava.getId(), "Практика", "Написать код", Status.IN_PROGRESS, 1600, LocalDateTime.of(2021, 12, 20, 10, 0, 0));
        taskManager.addSubTask(practicum);
        final SubTask savedSubTask = taskManager.getSubTaskById(readTheory.getId());

        assertNotNull(savedSubTask, "Задача не найдена.");
        assertEquals(readTheory, savedSubTask, "Задачи не совпадают.");

        final List<SubTask> subTasks = taskManager.getSubTasks();

        assertNotNull(subTasks, "Задачи не возвращаются.");
        assertEquals(2, subTasks.size(), "Неверное количество задач.");
        assertEquals(readTheory, subTasks.getFirst(), "Задачи не совпадают.");
    }

    @Test
    void checkEpics() {
        taskManager.addEpic(learnJava);
        SubTask readTheory = new SubTask(learnJava.getId(), "Прочитать теорию", "Написать конспект", Status.DONE, 1600, LocalDateTime.of(2020, 12, 20, 10, 0, 0));
        taskManager.addSubTask(readTheory);
        SubTask practicum = new SubTask(learnJava.getId(), "Практика", "Написать код", Status.DONE, 1600, LocalDateTime.of(2021, 12, 20, 10, 0, 0));
        taskManager.addSubTask(practicum);

        assertEquals(practicum.getStatus(), learnJava.getStatus(), "Статусы не совпадают");

        SubTask newPracticum = new SubTask(learnJava.getId(), "Практика", "Написать код", Status.IN_PROGRESS, 1600, LocalDateTime.of(2022, 12, 20, 10, 0, 0));
        taskManager.updateSubTask(newPracticum);

        assertEquals(practicum.getStatus(), learnJava.getStatus(), "Статусы не совпадают");
    }

    @Test
    public void shouldIntersectionTask() throws ManagerSaveException {
        Throwable throwable1 = assertThrows(ManagerSaveException.class, () -> {
            taskManager.addTask(run);
            taskManager.addTask(run2);
        });
        assertNotNull(throwable1.getMessage());
    }
}