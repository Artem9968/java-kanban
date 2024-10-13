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

    HistoryManager historyManager = Managers.getDefaultHistory();   // работа с менеджером истории

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
        historyManager.add(tasks.get(idTask));
        return tasks.get(idTask);
    }

    @Override
    public Epic getEpicById(int idEpic) {     // вернуть эпик по его id
        historyManager.add(epics.get(idEpic));
        return epics.get(idEpic);
    }

    @Override
    public SubTask getSubTaskById(int idSubTask) {    // вернуть подзадачу по ее id
        historyManager.add(subTasks.get(idSubTask));
        return subTasks.get(idSubTask);
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
        tasks.remove(id);
    }

    @Override
    public void delEpicById(int epicId) {    // удалить эпик
        final Epic epic = epics.remove(epicId);  // изменил логику метода согласно рекомендации
        if (epic != null) {
            for (int subtaskId : epic.getSubTasks())
                subTasks.remove(subtaskId);
        }
    }

    @Override
    public void delSubTaskById(int idSubtusk) {    // удалить подзадачу
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
        tasks.clear();
    }

    @Override
    public void dellAllEpics() {    // удалить все эпики
        subTasks.clear();
        epics.clear();
    }

    @Override
    public void dellAllSubs() {    // удалить все подзадачи
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubTasks().clear();
            epic.setStatus(Status.NEW);
        }
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
