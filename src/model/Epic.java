package model;

import java.util.ArrayList;

public class Epic extends Task {
    public ArrayList<Integer> subTaskIds = new ArrayList<>();

    public ArrayList<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    public void setSubTaskIds(ArrayList<Integer> subTaskIds) {
        this.subTaskIds = subTaskIds;
    }

    public Epic(String name, String description) {
        this.name = name;
        this.description = description;

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
}
