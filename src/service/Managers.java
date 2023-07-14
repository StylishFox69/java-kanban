package service;

public class Managers {
    private Managers() {
    }

    public static FileBackedTasksManager getDefaultTaskManager() {
        return new FileBackedTasksManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}

