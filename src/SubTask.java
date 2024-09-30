public class SubTask extends Task {

    private int epicId;

    public SubTask(String name, String description, Status status) {
        super(name, description, status);
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int id) {
        epicId = id;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epicId=" + epicId +
                '}' + super.toString();
    }
}
