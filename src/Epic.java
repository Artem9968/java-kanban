import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subTasks;

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
        subTasks = new ArrayList<>();
    }

    public ArrayList<Integer> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(ArrayList<Integer> subtask) {
        subTasks.addAll(subtask);
    }

    public void delSubTask(Integer idSubTask) {
        subTasks.remove(idSubTask);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTasks=" + subTasks +
                '}' + super.toString();
    }
}
