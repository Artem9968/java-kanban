package com.yandex.taskmanager.sevice;

import com.yandex.taskmanager.model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    int historySize = 10;
    List<Task> Tasks;

    public InMemoryHistoryManager() {
        Tasks = new ArrayList<>(historySize);
    }

    @Override
    public void add(Task task) {
        if (Tasks.size() == historySize) {
            Tasks.remove(0);
        }
        Tasks.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return Tasks;
    }
}
