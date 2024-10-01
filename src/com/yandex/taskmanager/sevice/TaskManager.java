package com.yandex.taskmanager.sevice;

import com.yandex.taskmanager.model.Epic;
import com.yandex.taskmanager.model.SubTask;
import com.yandex.taskmanager.model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    private final HashMap<Integer, Task> tasks = new HashMap<>();  // сделал final
    private final HashMap<Integer, Epic> epics = new HashMap<>();    // сделал final
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();  // сделал final

    private int id = 1;

    public void addTask(Task task) {   // добавить задачу
        task.setId(++id);               // использую префиксный инкремент
        tasks.put((task.getId()), task);
    }

    public void addEpic(Epic epic) {            // добавить эпик
        epic.setId(id);
        epics.put(epic.getId(), epic);
        id++;
    }

    public void addSubTask(int epicId, SubTask subtask) {      // добавить подзадачу конкретному эпику по его id
        subtask.setId(id);
        subTasks.put(subtask.getId(), subtask);
        id++;
        // subtask.setEpicId(epicId);
        Epic epic = epics.get(subtask.getEpicId());
        epic.getSubTasks().add(subtask.getId());
        checkStatusEpic(epic);
    }

    public ArrayList<Task> getTasks() {        // заменил на ArrayList
        return new ArrayList<>(tasks.values());     // если что, вернется пустой список (проверил - вроде работает)
    }

    public ArrayList<Epic> getEpics() {     // заменил на ArrayList
        return new ArrayList<>(epics.values());       // если что, вернется пустой список (проверил - вроде работает)
    }

    public ArrayList<SubTask> getSubTasks() {     // добавил метод на подзадачи
        return new ArrayList<>(subTasks.values());
    }

    public Task getTaskById(int idTask) {   // вернуть задачу по ее id
        return tasks.get(idTask);            // удалил ненужную предварительную проверку
    }

    public Epic getEpicById(int idEpic) {     // вернуть эпик по его id
        return epics.get(idEpic);                // удалил ненужную предварительную проверку
    }

    public SubTask getSubTaskById(int idSubTask) {    // вернуть подзадачу по ее id
        return subTasks.get(id);                        // удалил ненужную предварительную проверку
    }

    public ArrayList<SubTask> getSubsByEpicId(int epicId) {    // вернуть все подзадачи конкретного эпика по id эпика
        if (epics.containsKey(epicId)) {
            ArrayList<SubTask> subs = new ArrayList<>();
            Epic epic = epics.get(epicId);                 // реализовал рекомендации
            for (int i : epic.getSubTasks())
                subs.add(subTasks.get(i));
            return subs;
        } else
            return null;
    }

    public void updateTask(Task task) {          // обновить задачу
        tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {     // обновить эпик
        final Epic saved = epics.get(epic.getId());   // изменил метод согласно рекомендациям
        saved.setName(epic.getName());
        saved.setDescription(epic.getDescription());
    }

    public void updateSubTask(SubTask subtask) {     // обновить подзадачу эпика
        subTasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        checkStatusEpic(epic);
    }

    public void delTaskById(int id) {   // удалить задачу
        tasks.remove(id);
    }

    public void delEpicById(int epicId) {    // удалить эпик
        final Epic epic = epics.remove(epicId);  // изменил логику метода согласно рекомендации
        if (epic != null) {
            for (int subtaskId : epic.getSubTasks())
                subTasks.remove(subtaskId);
        }
    }

    public void delSubTaskById(int idSubtusk) {    // удалить подзадачу
        final SubTask subTask = subTasks.remove(idSubtusk); // изменил логику метода согласно рекомендации
        if (subTask != null) {
            final int epicId = subTask.getEpicId();
            final Epic epic = epics.get(epicId);
            epic.delSubTask(idSubtusk);
            checkStatusEpic(epic);
        }
    }

    public void dellAllTasks() {     // удалить все задачи
        tasks.clear();
    }

    public void dellAllEpics() {    // удалить все эпики
        subTasks.clear();
        epics.clear();
    }

    public void dellAllSubs() {    // удалить все подзадачи
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubTasks().clear();
            epic.setStatus(Status.NEW);
        }
    }

    public void setTaskStatus(Task task, Status status) {
        task.setStatus(status);
    }

    public void setSubTaskStatus(SubTask subTask, Status status) {
        subTask.setStatus(status);
    }

    private void checkStatusEpic(Epic epic) {     // метод расчета статуса эпика по статусам его подзадач
        int first = 0;
        int done = 0;
        for (int i : epic.getSubTasks()) {
            SubTask sub = subTasks.get(i);
            switch (sub.getStatus()) {
                case NEW:
                    first++;
                case IN_PROGRESS:
                    epic.setStatus(Status.IN_PROGRESS);  // добавил согласно рекомендации
                    return;
                case DONE:
                    done++;
            }
        }
        if (done > 0 && done == epic.getSubTasks().size())
            epic.setStatus(Status.DONE);
        else if (first == epic.getSubTasks().size())
            epic.setStatus(Status.NEW);
    }
}
