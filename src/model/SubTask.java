package model;

public class SubTask extends Task {
    protected int epicId;

    public SubTask(String name, String description, Status status, int epicId) {
        this.name = name;
        this.description = description;
        this.epicId = epicId;
        this.status = status;
    }



    public int getEpicId() {
        return epicId;
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
