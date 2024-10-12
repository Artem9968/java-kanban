package com.yandex.taskmanager.model;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subTasksId;   // сделал поле final

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
        subTasksId = new ArrayList<>();
    }

    public ArrayList<Integer> getSubTasks() {
        return subTasksId;
    }

    public void setSubTasks(ArrayList<Integer> subtask) {
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
