package model;

public enum Status {
    NEW("Новый"),
    IN_PROGRESS("В процессе"),
    DONE("Завершен");

    final String nameStatus;

    Status(String nameStatus) {
        this.nameStatus = nameStatus;
    }
}
