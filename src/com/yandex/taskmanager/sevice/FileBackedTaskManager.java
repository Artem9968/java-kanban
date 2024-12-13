package com.yandex.taskmanager.sevice;

import com.yandex.taskmanager.exception.ManagerSaveException;
import com.yandex.taskmanager.model.Epic;
import com.yandex.taskmanager.model.Status;
import com.yandex.taskmanager.model.SubTask;
import com.yandex.taskmanager.model.Task;
import com.yandex.taskmanager.model.TaskType;

import java.io.*;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubTask(SubTask subtask) {
        super.addSubTask(subtask);
        save();
    }

    @Override
    public List<Task> getTasks() {
        return super.getTasks();
    }

    @Override
    public List<Epic> getEpics() {
        return super.getEpics();
    }

    @Override
    public List<SubTask> getSubTasks() {
        return super.getSubTasks();
    }

    @Override
    public Task getTaskById(int idTask) {
        return super.getTaskById(idTask);
    }

    @Override
    public Epic getEpicById(int idEpic) {
        return super.getEpicById(idEpic);
    }

    @Override
    public SubTask getSubTaskById(int idSubTask) {
        return super.getSubTaskById(idSubTask);
    }

    @Override
    public List<SubTask> getSubsByEpicId(int epicId) {
        return super.getSubsByEpicId(epicId);
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subtask) {
        super.updateSubTask(subtask);
        save();
    }

    @Override
    public void delTaskById(int id) {
        super.delTaskById(id);
        save();
    }

    @Override
    public void delEpicById(int epicId) {
        super.delEpicById(epicId);
        save();
    }

    @Override
    public void delSubTaskById(int idSubtusk) {
        super.delSubTaskById(idSubtusk);
        save();
    }

    @Override
    public void dellAllTasks() {
        super.dellAllTasks();
        save();
    }

    @Override
    public void dellAllEpics() {
        super.dellAllEpics();
        save();
    }

    @Override
    public void dellAllSubs() {
        super.dellAllSubs();
        save();
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            if (file.length() == 0) {
                writer.write("id,type,name,status,description,epic\n");
            }
            for (Task task : super.getTasks()) {
                writer.write((toString(task)));
                writer.newLine();
            }
            for (Epic epic : super.getEpics()) {
                writer.write((toString(epic)));
                writer.newLine();
            }
            for (SubTask subTask : super.getSubTasks()) {
                writer.write((toString(subTask)));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка во время записи файла: " + file.getName(), e);
        }
    }

    public static String toString(Task task) {
        String epicId = "";
        if (task.getType().equals(TaskType.SUBTASK)) {
            SubTask subTask = (SubTask) task;
            epicId = String.valueOf(subTask.getEpicId());
        }
        return task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() + "," +
                task.getDescription() + "," + epicId;
    }

    private static Task fromString(String line) {
        String[] parts = line.split(",");
        int id = Integer.parseInt(parts[0]);
        String type = parts[1];
        String name = parts[2];
        Status status = Status.valueOf(parts[3]);
        String description = parts[4];
        String epicIdNumber = parts.length > 5 ? parts[5] : "";  // использовал тернарный оператор
        Task task = null;
        switch (type) {
            case "TASK":
                task = new Task(name, description, status);
                task.setId(id);
                break;
            case "EPIC":
                task = new Epic(name, description);
                task.setId(id);
                task.setStatus(Status.NEW);
                break;
            case "SUBTASK":
                int epicId = Integer.parseInt(epicIdNumber);
                task = new SubTask(epicId, name, description, status);
                task.setId(id);
                break;
        }
        return task;
    }

    public static InMemoryTaskManager loadFromFile(File file) throws IOException {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            reader.readLine();      //   пропускаем титул файла
            while ((line = reader.readLine()) != null) {
                Task task = fromString(line);
                if (task instanceof Epic) {
                    manager.addEpic((Epic) task);
                } else if (task instanceof SubTask) {
                    manager.addSubTask((SubTask) task);
                } else {
                    manager.addTask(task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при чтении файла: " + file.getName(), e);
        }

        return manager;
    }
}
