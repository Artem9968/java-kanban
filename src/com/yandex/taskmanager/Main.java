package com.yandex.taskmanager;

import com.yandex.taskmanager.model.Epic;
import com.yandex.taskmanager.model.SubTask;
import com.yandex.taskmanager.model.Task;
import com.yandex.taskmanager.model.Status;
import com.yandex.taskmanager.sevice.*;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        // Ниже оставил необходимый минимум для проверки функциональности кода, который нужен по условию

        Task run = new Task("Потренироваться", "Выйти на пробежку", Status.IN_PROGRESS);
        Task swim = new Task("Поплавать", "Пойти в бассейн", Status.NEW);
        taskManager.addTask(run);
        taskManager.addTask(swim);
        System.out.println("------------------------------");
        System.out.println(taskManager.getTasks());
        System.out.println("------------------------------");
        Epic learnJava = new Epic("Освоить Java", "Разобраться в JavaCore");
        taskManager.addEpic(learnJava);
        System.out.println(taskManager.getEpics());
        System.out.println("------------------------------");
        SubTask readTheory = new SubTask(3, "Прочитать теорию", "Написать конспект", Status.DONE);
        SubTask practicum = new SubTask(3, "Практика", "Написать код", Status.IN_PROGRESS);
        taskManager.addSubTask(readTheory);
        taskManager.addSubTask(practicum);
        System.out.println(taskManager.getEpics());
        System.out.println("------------------------------");
        System.out.println(" ** " + taskManager.getSubsByEpicId(3));
        System.out.println("------------------------------");
        Epic checkCode = new Epic("Проверить код", "Проверить все методы классов");
        taskManager.addEpic(checkCode);
        System.out.println(taskManager.getEpicById(6));
        System.out.println("------------------------------");
        SubTask useDebug = new SubTask(6, "Использовать дебаг", "Пройтись дебагом", Status.DONE);
        taskManager.addSubTask(useDebug);
        System.out.println(taskManager.getEpicById(6));
        System.out.println("------------------------------");
        System.out.println(taskManager.getSubsByEpicId(6));
        System.out.println("------------------------------");
        taskManager.updateTask(run);
        System.out.println(" ** " + taskManager.getTasks());
        System.out.println("------------------------------");
        taskManager.updateSubTask(practicum);
        System.out.println(taskManager.getEpicById(3));
        System.out.println("------------------------------");
        System.out.println(taskManager.getSubsByEpicId(3));
        System.out.println("------------------------------");

        historyManager.add(taskManager.getTaskById(1));
        historyManager.add(taskManager.getTaskById(2));
        historyManager.add(taskManager.getSubTaskById(4));
        historyManager.add(taskManager.getSubTaskById(5));
        historyManager.add(taskManager.getEpicById(3));
        historyManager.add(taskManager.getEpicById(6));
        historyManager.add(taskManager.getTaskById(1));
        historyManager.add(taskManager.getTaskById(2));
        historyManager.add(taskManager.getSubTaskById(4));
        historyManager.add(taskManager.getSubTaskById(5));
        historyManager.add(taskManager.getEpicById(3));
        historyManager.add(taskManager.getEpicById(6));


        System.out.println("------------------------------");
        printAllTasks(taskManager, historyManager);
        System.out.println("------------------------------");

        taskManager.delTaskById(1);
        taskManager.delEpicById(3);
        System.out.println(taskManager.getTasks());
        System.out.println("------------------------------");
        System.out.println(taskManager.getEpics());
        System.out.println("------------------------------");

        taskManager.dellAllTasks();
        taskManager.dellAllSubs();
        System.out.println(taskManager.getTasks());
        System.out.println("------------------------------");
        System.out.println(taskManager.getEpics());
        System.out.println("------------------------------");
        taskManager.dellAllEpics();
        System.out.println(taskManager.getEpics());
        System.out.println("------------------------------");
        historyManager.add(taskManager.getTaskById(2));

    }

    public static void printAllTasks(TaskManager taskManager, HistoryManager historyManager) {

        System.out.println("Задачи:");
        for (Task task : taskManager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : taskManager.getEpics()) {
            System.out.println(epic);

            for (Task task : taskManager.getSubsByEpicId(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : taskManager.getSubTasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : historyManager.getHistory()) {
            System.out.println(task);
        }
    }

}

