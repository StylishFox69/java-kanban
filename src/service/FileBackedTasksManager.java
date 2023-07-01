package service;

import exceptions.ManagerSaveException;
import model.*;
import utils.CSVUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    public static final String SPLITTER = ",";
    public static final String PATH = "resources/tasks.csv";
    private static final File FILE = new  File(PATH);

    public FileBackedTasksManager(File file) {
        super();
        file = FILE;
    }

    public static void main(String[] args) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(new File(PATH));
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
        CSVUtils.loadFromFile();
        System.out.println(historyManager.getHistory());
    }

    public void save() {
        if (!checkFile()) {
            return;
        }
        StringBuilder historyToString = new StringBuilder();
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE, false))) {
            historyToString.append("id,type,name,status,description,epic" + "\n");
            historyToString.append(fillTask(getTasks()));
            historyToString.append(fillEpic(getEpics()));
            historyToString.append(fillSubTask(getSubTasks()));
            historyToString.append("\n");
            if (historyManager.getHistory().size() == 0) {
                historyToString.append("end");
                return;
            }
            historyToString.append(historyToString());
            bufferedWriter.write(String.valueOf(historyToString));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения в файл.");

        }
    }

    private static StringBuilder fillTask(List<Task> tasks) {
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

    private static StringBuilder fillEpic(List<Epic> epics) {
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

    private static StringBuilder fillSubTask(List<SubTask> subTasks) {
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

    public String historyToString() {
        if (getHistory().size() == 0) {
            return "end";
        } else {
            StringBuilder sb = new StringBuilder();
            for (Task task : historyManager.getHistory()) {
                sb.append(task.getId()).append(SPLITTER);
            }
            sb.delete(sb.length() - 1, sb.length());
            return String.valueOf(sb);
        }
    }

    public static void historyFromString(String value) {
        if (value.equals("end")) {
            return;
        }
        String[] ids = value.split(SPLITTER);
        for (String id : ids) {
            CSVUtils.getUniversalTask(Integer.parseInt(id));
        }
    }

    public static boolean checkFile() {
        if (FILE.exists()) {
            return true;
        } else {
            return createFile();
        }
    }

    private static boolean createFile() {
        Path testFile;
        try {
            testFile = Files.createFile(Paths.get(PATH));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка создания файла.");
        }
        return Files.exists(testFile);
    }

    @Override
    public String toString() {
        String content;
        try {
            content = Files.readString(Path.of(PATH));
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
    public List<SubTask> getSubTasks() {
        return super.getSubTasks();
    }

    @Override
    public List<Task> getTasks() {
        return super.getTasks();
    }

    @Override
    public List<Epic> getEpics() {
        return super.getEpics();
    }

    @Override
    public List<SubTask> getSubTasksByEpic(int epicId) {
        return super.getSubTasksByEpic(epicId);
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
