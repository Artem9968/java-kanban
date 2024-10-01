package com.yandex.taskmanager.model;

import com.yandex.taskmanager.sevice.Status;

public class SubTask extends Task {

    private final int epicId;   // добавил поле final

    public SubTask(int id, String name, String description, Status status) {
        super(name, description, status);
        epicId = id;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "com.yandex.taskmanager.model.SubTask{" +
                "epicId=" + epicId +
                '}' + super.toString();
    }
}
