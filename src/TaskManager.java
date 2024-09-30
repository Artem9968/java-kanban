import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, SubTask> subtasks = new HashMap<>();

    private int id = 1;

    public void addTask(Task task) {   // добавить задачу
        task.setId(id);
        tasks.put((task.getId()), task);
        id++;
    }

    public void addEpic(Epic epic) {            // добавить эпик
        epic.setId(id);
        epics.put(epic.getId(), epic);
        id++;
    }

    public void addSubTask(int epicId, SubTask subtask) {      // добавить подзадачу конкретному эпику по его id
        subtask.setId(id);
        subtasks.put(subtask.getId(), subtask);
        id++;
        subtask.setEpicId(epicId);
        Epic epic = epics.get(subtask.getEpicId());
        epic.getSubTasks().add(subtask.getId());
        checkStatusEpic(epic);
    }

    public HashMap<Integer, Task> getTasks() {      // вернуть все задачи
        if (!tasks.isEmpty())
            return tasks;
        else
            return null;
    }

    public HashMap<Integer, Epic> getEpics() {     // вернуть все эпики
        if (!epics.isEmpty())
            return epics;
        else
            return null;
    }

    public Task getTaskById(int idTask) {   // вернуть задачу по ее id
        if (tasks.containsKey(idTask)) {
            return tasks.get(idTask);
        } else {
            return null;
        }
    }

    public Epic getEpicById(int idEpic) {     // вернуть эпик по его id
        if (epics.containsKey(idEpic)) {
            return epics.get(idEpic);
        } else {
            return null;
        }
    }

    public SubTask getSubTaskById(int idSubTask) {    // вернуть подзадачу по ее id
        if (subtasks.containsKey(idSubTask)) {
            return subtasks.get(id);
        } else {
            return null;
        }
    }

    public ArrayList<SubTask> getSubsByEpicId(int epicId) {    // вернуть все подзадачи конкретного эпика по id эпика
        if (epics.containsKey(epicId)) {
            ArrayList<SubTask> subs = new ArrayList<>();
            for (SubTask sub : subtasks.values())
                if (sub.getEpicId() == epicId)
                    subs.add(sub);
            return subs;
        } else
            return null;
    }

    public void updateTask(int id, Task task) {          // обновить задачу
        task.setId(id);
        tasks.put(id, task);
    }

    public void updateEpic(int id, Epic epic) {     // обновить эпик
        epic.setId(id);
        ArrayList<Integer> subs = epics.get(id).getSubTasks();
        Status status = epics.get(id).getStatus();
        epic.setStatus(status);
        epic.setSubTasks(subs);
        epics.put(id, epic);
    }

    public void updateSubTask(int idSubTusk, SubTask subtask) {     // обновить подзадачу эпика
        int epicId = subtasks.get(idSubTusk).getEpicId();
        subtask.setId(idSubTusk);
        subtasks.put(subtask.getId(), subtask);
        subtask.setEpicId(epicId);
        Epic epic = epics.get(subtask.getEpicId());
        checkStatusEpic(epic);
    }

    public void delTaskById(int id) {   // удалить задачу
        tasks.remove(id);
    }

    public void delEpicById(int id) {    // удалить эпик
        if (epics.containsKey(id)) {
            for (int i : epics.get(id).getSubTasks())
                subtasks.remove(i);
            epics.remove(id);
        }
    }

    public void delSubTaskById(int idSubTusk) {    // удалить подзадачу
        if (subtasks.containsKey(idSubTusk)) {
            int epicId = subtasks.get(idSubTusk).getEpicId();
            subtasks.remove(idSubTusk);
            epics.get(epicId).delSubTask(idSubTusk);
            Epic epic = epics.get(epicId);

            if (!epic.getSubTasks().isEmpty())
                checkStatusEpic(epic);
            else
                epic.setStatus(Status.DONE);
        }
    }

    public void dellAllTasks() {     // удалить все задачи
        tasks.clear();
    }

    public void dellAllEpics() {    // удалить все эпики
        subtasks.clear();
        epics.clear();
    }

    public void dellAllSubs() {    // удалить все подзадачи
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubTasks().clear();
            epic.setStatus(Status.NEW);
        }
    }

    public void checkStatusEpic(Epic epic) {     // метод расчета статуса эпика по статусам его подзадач
        int first = 0;
        int progress = 0;
        int done = 0;
        for (int i : epic.getSubTasks()) {
            SubTask sub = subtasks.get(i);
            switch (sub.getStatus()) {
                case NEW:
                    first++;
                case IN_PROGRESS:
                    progress++;
                case DONE:
                    done++;
            }
        }
        if (progress > 0)
            epic.setStatus(Status.IN_PROGRESS);
        else if (done > 0 && done == epic.getSubTasks().size())
            epic.setStatus(Status.DONE);
        else if (first == epic.getSubTasks().size())
            epic.setStatus(Status.NEW);
    }
}
