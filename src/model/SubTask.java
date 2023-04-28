package model;

public class SubTask extends Task {
    protected int epicId;

    public SubTask(String name, String description, Status status, int epicId) {
        this.name = name;
        this.description = description;
        this.epicId = epicId;
        this.status = status;
    }

    public SubTask(int id, String name, Status status, String description, TaskType type, int epicId) {
        super(id, name, status, description, type);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public TaskType getType(){
        return TaskType.SUBTASK;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", description='" + description + '\'' +
                ", epicId='" + epicId + '\'' +

                '}';
    }
}
