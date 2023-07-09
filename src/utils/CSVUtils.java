package utils;

import exceptions.ManagerReadException;
import model.*;
import service.FileBackedTasksManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static service.FileBackedTasksManager.SPLITTER;

public class CSVUtils{
    public static final String PATH = "resources/tasks.csv";
    private static final File FILE = new File(PATH);

    public static Task fromString(String value) {
        String[] rows = value.split(SPLITTER);
        int id = Integer.parseInt(rows[0]);
        TaskType type = TaskType.valueOf(rows[1]);
        String name = rows[2];
        Status status = Status.valueOf(rows[3]);
        String description = rows[4];
        Task task = null;
        if (type == TaskType.TASK) {
            task = new Task(id, name, status, description);
        } else if (type == TaskType.EPIC) {
            task = new Epic(id, name, status, description, type);
        } else if (type == TaskType.SUBTASK) {
            task = new SubTask(id, name, status, description, type,
                    Integer.parseInt(rows[5]));
        }
        return task;
    }

    public static void getUniversalTask(int id) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(FILE);
        if (fileBackedTasksManager.getTask(id) != null) {
            fileBackedTasksManager.getTask(id);
        } else if (fileBackedTasksManager.getSubTask(id) != null) {
            fileBackedTasksManager.getSubTask(id);
        } else {
            fileBackedTasksManager.getEpic(id);
        }
    }

    public static boolean checkFile() {
        if (FILE.exists()) {
            return true;
        } else {
            return FileBackedTasksManager.createFile();
        }
    }

    public static List<String> read() {
        ArrayList<String> tasksFromFile = new ArrayList<>();
        if (!checkFile()) {
            return tasksFromFile;
        }
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE))) {
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
    public static StringBuilder fillTask(List<Task> tasks) {
        StringBuilder taskToString = new StringBuilder();
        for (Task task : tasks) {
            taskToString.append(task.getId()).append(SPLITTER);
            taskToString.append(task.getType()).append(SPLITTER);
            taskToString.append(task.getName()).append(SPLITTER);
            taskToString.append(task.getStatus()).append(SPLITTER);
            taskToString.append(task.getDescription());
            taskToString.append("\n");
        }
        return taskToString;
    }

    public static StringBuilder fillEpic(List<Epic> epics) {
        StringBuilder epicToString = new StringBuilder();
        for (Task epic : epics) {
            epicToString.append(epic.getId()).append(SPLITTER);
            epicToString.append(epic.getType()).append(SPLITTER);
            epicToString.append(epic.getName()).append(SPLITTER);
            epicToString.append(epic.getStatus()).append(SPLITTER);
            epicToString.append(epic.getDescription());
            epicToString.append("\n");
        }
        return epicToString;
    }

    public static StringBuilder fillSubTask(List<SubTask> subTasks) {
        StringBuilder subTaskToString = new StringBuilder();
        for (SubTask subTask : subTasks) {
            subTaskToString.append(subTask.getId()).append(SPLITTER);
            subTaskToString.append(subTask.getType()).append(SPLITTER);
            subTaskToString.append(subTask.getName()).append(SPLITTER);
            subTaskToString.append(subTask.getStatus()).append(SPLITTER);
            subTaskToString.append(subTask.getDescription()).append(SPLITTER);
            subTaskToString.append(subTask.getEpicId());
            subTaskToString.append("\n");
        }
        return subTaskToString;
    }

    public static void historyFromString(String value) {
        if (value.equals("\n")) {
            return;
        }
        String[] ids = value.split(SPLITTER);
        for (String id : ids) {
            getUniversalTask(Integer.parseInt(id));
        }
    }
}
