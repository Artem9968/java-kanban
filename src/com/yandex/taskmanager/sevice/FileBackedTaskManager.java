package com.yandex.taskmanager.sevice;

import com.yandex.taskmanager.model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }


    @Override
    public void addTask(Task task) {
        super.addTask(task);
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
    }

    @Override
    public void addSubTask(SubTask subtask) {
        super.addSubTask(subtask);
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
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
    }

    @Override
    public void updateSubTask(SubTask subtask) {
        super.updateSubTask(subtask);
    }

    @Override
    public void delTaskById(int id) {
        super.delTaskById(id);
    }

    @Override
    public void delEpicById(int epicId) {
        super.delEpicById(epicId);
    }

    @Override
    public void delSubTaskById(int idSubtusk) {
        super.delSubTaskById(idSubtusk);
    }

    @Override
    public void dellAllTasks() {
        super.dellAllTasks();
    }

    @Override
    public void dellAllEpics() {
        super.dellAllEpics();
    }

    @Override
    public void dellAllSubs() {
        super.dellAllSubs();
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return super.toString();
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
            throw new ManagerSaveException("Ошибка при сохранении данных в файл: " + file.getName(), e);
        }
    }
//    private String toString(Task task) {
//        return String.format("%d,%s,%s,%s,%s,%s",
//                task.getId(),
//                task.getType(),
//                task.getName(),
//                task.getStatus(),
//                task.getDescription(),
//                task instanceof SubTask ? ((SubTask) task).getEpicId() : ""
//        );
//    }

    public static String toString(Task task) {
        String epicId = "";
        if (task.getType().equals(TaskType.SUBTASK)) {
            SubTask subTask = (SubTask) task;
            epicId = String.valueOf(subTask.getEpicId());
        }
        return task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription() + "," + epicId;
    }

    private static Task fromString(String line) {
        String[] parts = line.split(",");
        int idNumber = Integer.parseInt(parts[0]);
        String type = parts[1];
        String name = parts[2];
        Status status = Status.valueOf(parts[3]);
        String description = parts[4];
        String epicIdNumber = parts.length > 5 ? parts[5] : "";

        Task task = null;
        switch (type) {
            case "TASK":
                task = new Task(name, description, status);
                task.setId(idNumber);
                break;
            case "EPIC":
                task = new Epic(name, description);
                task.setId(idNumber);
                break;
            case "SUBTASK":
                int epicId = Integer.parseInt(epicIdNumber);
                task = new SubTask(epicId, name, description,  status);
                task.setId(idNumber);
                break;
        }
        return task;
    }

    public static FileBackedTaskManager loadFromFile(File file) throws IOException {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            // Пропускаем заголовок
            reader.readLine();
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
            throw new ManagerSaveException("Ошибка при загрузке данных из файла: " + file.getName(), e);
        }

        return manager;
    }


}
