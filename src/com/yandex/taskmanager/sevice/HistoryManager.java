package com.yandex.taskmanager.sevice;

import com.yandex.taskmanager.model.Task;

import java.util.List;

public interface HistoryManager {

    public void add(Task task);

    public List<Task> getHistory();

    public void remove(int id);
}