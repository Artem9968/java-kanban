package com.yandex.taskmanager.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class SubTask extends Task {

    private final int epicId;   // добавил поле final

    public SubTask(int id, String name, String description, Status status, int duration, LocalDateTime startTime) {
        super(name, description, status, duration, startTime);
        epicId = id;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epicId=" + epicId +
                '}' + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return epicId == subTask.epicId;
    }

    @Override
    public int hashCode() {
        return super.hashCode() + Objects.hashCode(epicId);
    }
}
