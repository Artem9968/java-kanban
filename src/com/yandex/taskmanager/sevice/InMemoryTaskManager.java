package com.yandex.taskmanager.sevice;

import com.yandex.taskmanager.model.Epic;
import com.yandex.taskmanager.model.Status;
import com.yandex.taskmanager.model.SubTask;
import com.yandex.taskmanager.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    private final Map<Integer, Task> tasks = new HashMap<>();  // сделал final
    private final Map<Integer, Epic> epics = new HashMap<>();    // сделал final
    private final Map<Integer, SubTask> subTasks = new HashMap<>();  // сделал final

    private int id = 0;

    @Override
    public void addTask(Task task) {   // добавить задачу
        task.setId(++id);               // использую префиксный инкремент
        tasks.put((task.getId()), task);
    }

    @Override
    public void addEpic(Epic epic) {            // добавить эпик
        epic.setId(++id);                         // использую префиксный инкремент
        epics.put(epic.getId(), epic);
    }

    @Override
    public void addSubTask(SubTask subtask) {      // добавить подзадачу конкретному эпику по его id
        subtask.setId(++id);                                // использую префиксный инкремент
        subTasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.getSubTasks().add(subtask.getId());
        checkStatusEpic(epic);
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());     // если что, вернется пустой список (проверил - вроде работает)
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());       // если что, вернется пустой список (проверил - вроде работает)
    }

    @Override
    public List<SubTask> getSubTasks() {     // добавил метод на подзадачи
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public Task getTaskById(int idTask) {   // вернуть задачу по ее id
        Task task = tasks.get(idTask);     // ввел переменную, что не обращаться к мапе лишний раз
        historyManager.add(task);           // проверка в методе add
        return task;
    }

    @Override
    public Epic getEpicById(int idEpic) {     // вернуть эпик по его id
        Epic epic = epics.get(idEpic);         // ввел переменную, что не обращаться к мапе лишний раз
        historyManager.add(epic);                // проверка в методе add
        return epic;
    }

    @Override
    public SubTask getSubTaskById(int idSubTask) {    // вернуть подзадачу по ее id
        SubTask subTask = subTasks.get(idSubTask);     // ввел переменную, что не обращаться к мапе лишний раз
        historyManager.add(subTask);                      // проверка в методе add
        return subTask;
    }

    @Override
    public List<SubTask> getSubsByEpicId(int epicId) {    // вернуть все подзадачи конкретного эпика по id эпика
        final Epic epic = epics.get(epicId);                  // реализовал рекомендации
        if (epic != null) {
            List<SubTask> subs = new ArrayList<>();
            for (int subTaskId : epic.getSubTasks())
                subs.add(subTasks.get(subTaskId));
            return subs;
        }
        return null;
    }

    @Override
    public void updateTask(Task task) {          // обновить задачу
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {     // обновить эпик
        final Epic saved = epics.get(epic.getId());   // изменил метод согласно рекомендациям
        saved.setName(epic.getName());
        saved.setDescription(epic.getDescription());
    }

    @Override
    public void updateSubTask(SubTask subtask) {     // обновить подзадачу эпика
        subTasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        checkStatusEpic(epic);
    }

    @Override
    public void delTaskById(int id) {   // удалить задачу
        historyManager.remove(id);
        tasks.remove(id);
    }

    @Override
    public void delEpicById(int epicId) {    // удалить эпик
        historyManager.remove(epicId);
        final Epic epic = epics.remove(epicId);  // изменил логику метода согласно рекомендации
        if (epic != null) {
            for (int subtaskId : epic.getSubTasks()) {
                historyManager.remove(subtaskId);
                subTasks.remove(subtaskId);
            }
        }
    }

    @Override
    public void delSubTaskById(int idSubtusk) {    // удалить подзадачу
        historyManager.remove(idSubtusk);
        final SubTask subTask = subTasks.remove(idSubtusk); // изменил логику метода согласно рекомендации
        if (subTask != null) {
            final int epicId = subTask.getEpicId();
            final Epic epic = epics.get(epicId);
            epic.delSubTask(idSubtusk);
            checkStatusEpic(epic);
        }
    }

    @Override
    public void dellAllTasks() {     // удалить все задачи
        for (int taskId : tasks.keySet()) {
            historyManager.remove(taskId);
        }
        tasks.clear();
    }

    @Override
    public void dellAllEpics() {    // удалить все эпики
        for (int subId : subTasks.keySet()) {
            historyManager.remove(subId);
        }
        dellAllSubs();
        epics.clear();
    }

    @Override
    public void dellAllSubs() {    // удалить все подзадачи
        for (int subId : subTasks.keySet()) {
            historyManager.remove(subId);
        }
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubTasks().clear();
            epic.setStatus(Status.NEW);
        }
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void checkStatusEpic(Epic epic) {     // метод расчета статуса эпика по статусам его подзадач
        int first = 0;
        int done = 0;
        for (int i : epic.getSubTasks()) {
            SubTask sub = subTasks.get(i);
            switch (sub.getStatus()) {
                case NEW:
                    first++;
                    break;
                case IN_PROGRESS:
                    epic.setStatus(Status.IN_PROGRESS);
                    return;
                case DONE:
                    done++;
                    break;
            }
        }
        if (done > 0 && done == epic.getSubTasks().size())
            epic.setStatus(Status.DONE);
        else if (first == epic.getSubTasks().size())
            epic.setStatus(Status.NEW);
        else epic.setStatus(Status.IN_PROGRESS);      // добавил необходимое условие
    }
}