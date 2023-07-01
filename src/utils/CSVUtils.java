package utils;

import exceptions.ManagerReadException;
import model.*;
import service.FileBackedTasksManager;
import service.InMemoryTaskManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static service.FileBackedTasksManager.SPLITTER;

public class CSVUtils extends InMemoryTaskManager {
    public static final String PATH = "resources/tasks.csv";
    private static final File file = new File(PATH);
    public static FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(new File(PATH));

    private static void fromString(String value) {
        String[] rows = value.split(SPLITTER);
        int id = Integer.parseInt(rows[0]);
        TaskType type = TaskType.valueOf(rows[1]);
        String name = rows[2];
        Status status = Status.valueOf(rows[3]);
        String description = rows[4];
        if (type == TaskType.TASK) {
            fileBackedTasksManager.createTask(new Task(id, name, status, description));
        } else if (type == TaskType.EPIC) {
            fileBackedTasksManager.createEpic(new Epic(id, name, status, description, type));
        } else if (type == TaskType.SUBTASK) {
            fileBackedTasksManager.createSubTask(new SubTask(id, name, status, description, type,
                    Integer.parseInt(rows[5])));
        }
    }

    public static void getUniversalTask(int id) {
        if (fileBackedTasksManager.getTask(id) != null) {
            fileBackedTasksManager.getTask(id);
        } else if (fileBackedTasksManager.getSubTask(id) != null) {
            fileBackedTasksManager.getSubTask(id);
        } else {
            fileBackedTasksManager.getEpic(id);
        }
    }

    public static void loadFromFile() {
        List<String> tasks = read();
        for (int i = 0; i < tasks.size() - 2; i++) {
            fromString(tasks.get(i));
        }
        FileBackedTasksManager.historyFromString(tasks.get(tasks.size() - 1));
    }

    private static List<String> read() {
        ArrayList<String> tasksFromFile = new ArrayList<>();
        if (!FileBackedTasksManager.checkFile()) {
            return tasksFromFile;
        }
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            bufferedReader.readLine();
            while (bufferedReader.ready()) {
                tasksFromFile.add(bufferedReader.readLine());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new ManagerReadException("Произошла ошибка при чтении файла", e.getCause());
        }
        return tasksFromFile;
    }
}
