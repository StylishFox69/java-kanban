package model;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    public ArrayList<Integer> subTaskIds = new ArrayList<>();

    public Epic(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    public Epic(int id, String name, Status status, String description, TaskType type) {
        super(id, name, status, description, type);
    }

    public ArrayList<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    public TaskType getType(){
        return TaskType.EPIC;
    }

    @Override
    public String toString() {
        String result = "Epic{" +
                "id = " + id;
        if (subTaskIds == null) {
            result += " subTasks = " + "null ";
        } else {
            result += " subTasks = " + subTaskIds.size();
        }
        result += ", name = '" + name + '\'' +
                ", status = '" + status + '\'' +
                ", description = '" + description + '\'' +
                '}';
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Epic epic = (Epic) o;
        return (getId() == epic.getId()) &&
                Objects.equals(getName(), epic.getName()) &&
                Objects.equals(getDescription(), epic.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription());
    }
}
