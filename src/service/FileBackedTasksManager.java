package service;

import ManagerExceptions.ManagerSaveException;
import model.*;
import utils.CSVUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    public static final String SPLITTER = ",";
    private static File file;
    public static final String PATH = "resources/tasks.csv";

    public FileBackedTasksManager(File file) {
        super();
        FileBackedTasksManager.file = file;
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
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, false))) {
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
            throw new ManagerSaveException(e.getMessage(), e.getCause());
        }
    }

    private static StringBuilder fillTask(List<Task> tasks) {
        StringBuilder taskToString = new StringBuilder();
        for (Task task : tasks) {
            taskToString.append(task.getId()).append(",");
            taskToString.append(task.getType()).append(",");
            taskToString.append(task.getName()).append(",");
            taskToString.append(task.getStatus()).append(",");
            taskToString.append(task.getDescription());
            taskToString.append("\n");
        }
        return taskToString;
    }

    private static StringBuilder fillEpic(List<Epic> epics) {
        StringBuilder epicToString = new StringBuilder();
        for (Task epic : epics) {
            epicToString.append(epic.getId()).append(",");
            epicToString.append(epic.getType()).append(",");
            epicToString.append(epic.getName()).append(",");
            epicToString.append(epic.getStatus()).append(",");
            epicToString.append(epic.getDescription());
            epicToString.append("\n");
        }
        return epicToString;
    }

    private static StringBuilder fillSubTask(List<SubTask> subTasks) {
        StringBuilder subTaskToString = new StringBuilder();
        for (SubTask subTask : subTasks) {
            subTaskToString.append(subTask.getId()).append(",");
            subTaskToString.append(subTask.getType()).append(",");
            subTaskToString.append(subTask.getName()).append(",");
            subTaskToString.append(subTask.getStatus()).append(",");
            subTaskToString.append(subTask.getDescription()).append(",");
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
                sb.append(task.getId()).append(",");
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
        if (file.exists()) {
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
            throw new RuntimeException(e);
        }
        return Files.exists(testFile);
    }

    @Override
    public String toString() {
        String content;
        try {
            content = Files.readString(Path.of(PATH));
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage(), e.getCause());
        }
        return content;
    }

    @Override
    public Task createTask(Task task) {
        Task task1 = super.createTask(task);
        save();
        return task1;
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        SubTask subTask1 = super.createSubTask(subTask);
        save();
        return subTask1;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic epic1 = super.createEpic(epic);
        save();
        return epic1;

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
