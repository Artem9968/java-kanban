package com.yandex.taskmanager.model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subTasksId;   // сделал поле final

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
        subTasksId = new ArrayList<>();
    }

    public List<Integer> getSubTasks() {
        return subTasksId;
    }

    public void addSubTasks(List<Integer> subtask) {
        subTasksId.addAll(subtask);
    }

    public void delSubTask(Integer idSubTask) {
        subTasksId.remove(idSubTask);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTasks=" + subTasksId +
                '}' + super.toString();
    }
}
