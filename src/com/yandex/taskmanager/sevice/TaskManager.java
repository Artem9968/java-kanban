package com.yandex.taskmanager.sevice;

import com.yandex.taskmanager.model.Epic;
import com.yandex.taskmanager.model.SubTask;
import com.yandex.taskmanager.model.Task;

import java.util.List;

public interface TaskManager {
    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubTask(SubTask subtask);

    List<Task> getTasks();

    List<Epic> getEpics();

    List<SubTask> getSubTasks();

    Task getTaskById(int idTask);

    Epic getEpicById(int idEpic);

    SubTask getSubTaskById(int idSubTask);

    List<SubTask> getSubsByEpicId(int epicId);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(SubTask subtask);

    void delTaskById(int id);

    void delEpicById(int epicId);

    void delSubTaskById(int idSubtusk);

    void dellAllTasks();

    void dellAllEpics();

    void dellAllSubs();

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();
}