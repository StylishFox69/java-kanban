package model;

public enum Status {
    NEW("Новый"),
    IN_PROGRESS("В процессе"),
    DONE("Завершен");

    private final String nameStatus;

    Status(String nameStatus) {
        this.nameStatus = nameStatus;
    }

    public String getNameStatus() {
        return nameStatus;
    }
}
