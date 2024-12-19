package com.yandex.taskmanager.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private final List<Integer> subTasksId;   // сделал поле final

    public Epic(String name, String description) {
        super(name, description, Status.NEW, 0, "01.01.24 00:00");
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
    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTasks=" + subTasksId +
                '}' + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subTasksId, epic.subTasksId);
    }

    @Override
    public int hashCode() {
        return super.hashCode() + Objects.hashCode(subTasksId);
    }
}
