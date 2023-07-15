package service;

import exceptions.ManagerSaveException;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import utils.CSVUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private static final String HEADING_FOR_HISTORY = "id,type,name,status,description,epic" + System.lineSeparator();
    private final File FILE = new File(CSVUtils.PATH);

    public FileBackedTasksManager(File file) {
        super();
        file = FILE;
    }

    public File getFILE() {
        return FILE;
    }

    public FileBackedTasksManager() {
    }

    public static void main(String[] args) {
        File file = new File(CSVUtils.PATH);
        FileBackedTasksManager fileBackedTasksManager = Managers.getDefaultTaskManager();
        Epic epic0 = fileBackedTasksManager.createEpic(new Epic("0", "DSSD"));
        SubTask subTask01 = fileBackedTasksManager.createSubTask(new SubTask("2", "ssd", Status.DONE, epic0.getId()));
        SubTask subTask02 = fileBackedTasksManager.createSubTask(new SubTask("3", "ds", Status.NEW, epic0.getId()));
        SubTask subTask03 = fileBackedTasksManager.createSubTask(new SubTask("4", "ds", Status.IN_PROGRESS, epic0.getId()));
        Epic epic1 = fileBackedTasksManager.createEpic(new Epic("1", "DSSD"));

        fileBackedTasksManager.getEpic(epic0.getId());
        List<Task> history = new ArrayList<>(fileBackedTasksManager.getHistory());
        for (Task task : history) {
            System.out.println(task);
        }
        fileBackedTasksManager.getEpic(epic1.getId());
        System.out.println("hhh1");
        List<Task> history1 = new ArrayList<>(fileBackedTasksManager.getHistory());
        for (Task task : history1) {
            System.out.println(task);
        }
        fileBackedTasksManager.getEpic(epic0.getId());
        System.out.println("hhh1");
        List<Task> history2 = new ArrayList<>(fileBackedTasksManager.getHistory());
        for (Task task : history2) {
            System.out.println(task);
        }
        fileBackedTasksManager.getSubTask(subTask02.getId());
        System.out.println("hhh1");
        List<Task> history3 = new ArrayList<>(fileBackedTasksManager.getHistory());
        for (Task task : history3) {
            System.out.println(task);
        }
        fileBackedTasksManager.getSubTask(subTask01.getId());
        System.out.println("hhh1");
        List<Task> history4 = new ArrayList<>(fileBackedTasksManager.getHistory());
        for (Task task : history4) {
            System.out.println(task);
        }
        fileBackedTasksManager.getSubTask(subTask03.getId());
        System.out.println("hhh1");
        List<Task> history5 = new ArrayList<>(fileBackedTasksManager.getHistory());
        for (Task task : history5) {
            System.out.println(task);
        }
        fileBackedTasksManager.getSubTask(subTask02.getId());
        System.out.println("hhh");
        List<Task> history6 = new ArrayList<>(fileBackedTasksManager.getHistory());
        for (Task task : history6) {
            System.out.println(task);
        }
        System.out.println("hhh1");
        System.out.println(fileBackedTasksManager.getHistory());
        fileBackedTasksManager = loadFromFile(file);
        System.out.println(fileBackedTasksManager.getEpics());
        System.out.println(fileBackedTasksManager.getTasks());
        System.out.println(fileBackedTasksManager.getSubTasks());
        System.out.println(fileBackedTasksManager.getHistory());
    }

    public static boolean createFile() {
        Path testFile;
        try {
            testFile = Files.createFile(Paths.get(CSVUtils.PATH));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка создания файла.");
        }
        return Files.exists(testFile);
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fb = new FileBackedTasksManager(file);
        List<String> tasks = CSVUtils.read(fb);
        for (int i = 0; i < tasks.size() - 2; i++) {
            Task task = CSVUtils.fromString(tasks.get(i));
            switch (task.getType()) {
                case SUBTASK:
                    SubTask subTask = (SubTask) task;
                    Epic epic = fb.epics.get(subTask.getEpicId());
                    epic.getSubTaskIds().add(subTask.getId());
                    fb.subTasks.put(subTask.getId(), subTask);
                    break;
                case EPIC:
                    Epic epic1 = (Epic) task;
                    fb.epics.put(epic1.getId(), epic1);
                    break;
                case TASK:
                    fb.tasks.put(task.getId(), task);
                    break;
            }
        }
        historyFromString(tasks.get(tasks.size() - 1), fb);
        return fb;
    }

    public static void historyFromString(String value, FileBackedTasksManager fileBackedTasksManager) {
        if (value.equals(System.lineSeparator())) {
            return;
        }
        String[] ids = value.split(CSVUtils.SPLITTER);
        for (String id : ids) {
            CSVUtils.getUniversalTask(Integer.parseInt(id), fileBackedTasksManager);
        }
    }


    private void save() {
        StringBuilder historyToString = new StringBuilder();
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE, false))) {
            historyToString.append(HEADING_FOR_HISTORY);
            historyToString.append(CSVUtils.fillTask(getTasks()));
            historyToString.append(CSVUtils.fillEpic(getEpics()));
            historyToString.append(CSVUtils.fillSubTask(getSubTasks()));
            historyToString.append(System.lineSeparator());
            if (historyManager.getHistory().isEmpty()) {
                historyToString.append(System.lineSeparator());
                return;
            }
            historyToString.append(historyToString());
            bufferedWriter.write(String.valueOf(historyToString));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения в файл.");
        }
    }

    public String historyToString() {
        if (getHistory().isEmpty()) {
            return System.lineSeparator();
        } else {
            StringBuilder sb = new StringBuilder();
            for (Task task : historyManager.getHistory()) {
                sb.append(task.getId()).append(CSVUtils.SPLITTER);
            }
            sb.delete(sb.length() - 1, sb.length());
            return String.valueOf(sb);
        }
    }

    @Override
    public String toString() {
        String content;
        try {
            content = Files.readString(Path.of(CSVUtils.PATH));
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка при сохранении файла");
        }
        return content;
    }

    @Override
    public Task createTask(Task task) {
        Task savedTask = super.createTask(task);
        save();
        return savedTask;
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        SubTask savedSubTask = super.createSubTask(subTask);
        save();
        return savedSubTask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic savedEpic = super.createEpic(epic);
        save();
        return savedEpic;

    }

    @Override
    public Task getTask(int taskId) {
        Task task = super.getTask(taskId);
        save();
        return task;
    }

    @Override
    public SubTask getSubTask(int subTaskId) {
        SubTask subTask = super.getSubTask(subTaskId);
        save();
        return subTask;
    }

    @Override
    public Epic getEpic(int epicId) {
        Epic epic = super.getEpic(epicId);
        save();
        return epic;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void deleteTask(int taskId) {
        super.deleteTask(taskId);
        save();
    }

    @Override
    public void deleteEpic(int epicId) {
        super.deleteEpic(epicId);
        save();
    }

    @Override
    public void deleteSubTask(int subTaskId) {
        super.deleteSubTask(subTaskId);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }
}
