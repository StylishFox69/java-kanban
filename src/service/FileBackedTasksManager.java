package service;

import model.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    private final File file;
    public static final String path = "C:\\Users\\Рамиль\\dev\\java-kanban\\resources\\tasks.csv";


    public FileBackedTasksManager(File file) {
        super();
        this.file = file;
    }

    public static void main(String[] args) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(new File(path));
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
        fileBackedTasksManager.deleteEpic(epic1.getId());
        System.out.println("hhh1");
        fileBackedTasksManager.deleteAllSubtasks();
        fileBackedTasksManager.loadFromFile();
        System.out.println(fileBackedTasksManager.historyManager.getHistory());
    }

    private Task getUniversalTask(int id) {
        if (super.getTask(id) != null) {
            return super.getTask(id);
        } else if (super.getSubTask(id) != null) {
            return super.getSubTask(id);
        } else {
            return super.getEpic(id);
        }

    }

    private void historyFromString(String value) {
        if (value.equals("end")) {
            return;
        }
        String[] ids = value.split(",");
        for (String id : ids) {
            this.getUniversalTask(Integer.parseInt(id));
        }
    }

    private void fromString(String value) {
        String[] rows = value.split(",");
        TaskType type = TaskType.valueOf(rows[1]);
        if (type == TaskType.TASK) {
            super.createTask(new Task(Integer.parseInt(rows[0]), rows[2], Status.valueOf(rows[3]), rows[4]));
        } else if (type == TaskType.EPIC) {
            super.createEpic(new Epic(Integer.parseInt(rows[0]), rows[2], Status.valueOf(rows[3]), rows[4], type));
        } else if (type == TaskType.SUBTASK) {
            super.createSubTask(new SubTask(Integer.parseInt(rows[0]), rows[2], Status.valueOf(rows[3]), rows[4], type,
                    Integer.parseInt(rows[5])));
        }
    }

    public void loadFromFile() {
        List<String> tasks = read();
        for (int i = 0; i < tasks.size() - 2; i++) {
            fromString(tasks.get(i));
        }
        historyFromString(tasks.get(tasks.size() - 1));
    }

    private String historyToString(HistoryManager manager) {
        if (super.getHistory().size() == 0) {
            return "end";
        } else {
            StringBuilder sb = new StringBuilder();
            for (Task task : manager.getHistory()) {
                sb.append(task.getId()).append(",");
            }
            sb.delete(sb.length()-1, sb.length());
            return String.valueOf(sb);
        }
    }

    private ArrayList<String> read() {
        ArrayList<String> tasksFromFile = new ArrayList<>();
        if (!checkFile()) {
            return tasksFromFile;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();
            while (br.ready()) {
                tasksFromFile.add(br.readLine());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new ManagerReadException(e.getMessage(), e.getCause());
        }
        return tasksFromFile;
    }

    @Override
    public String toString() {
        String content;
        try {
            content = Files.readString(Path.of(path));
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage(), e.getCause());
        }
        return content;
    }

    private void save() {
        if (!checkFile()) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
            sb.append("id,type,name,status,description,epic" + "\n");
            for (Task task : super.getTasks()) {
                sb.append(task.getId()).append(",");
                sb.append(task.getType()).append(",");
                sb.append(task.getName()).append(",");
                sb.append(task.getStatus()).append(",");
                sb.append(task.getDescription());
                sb.append("\n");
            }
            for (Epic epic : super.getEpics()) {
                sb.append(epic.getId()).append(",");
                sb.append(epic.getType()).append(",");
                sb.append(epic.getName()).append(",");
                sb.append(epic.getStatus()).append(",");
                sb.append(epic.getDescription());
                sb.append("\n");
            }
            for (SubTask subTask : super.getSubTasks()) {
                sb.append(subTask.getId()).append(",");
                sb.append(subTask.getType()).append(",");
                sb.append(subTask.getName()).append(",");
                sb.append(subTask.getStatus()).append(",");
                sb.append(subTask.getDescription()).append(",");
                sb.append(subTask.getEpicId());
                sb.append("\n");
            }
            sb.append("\n");
            if (historyManager.getHistory().size() == 0) {
                sb.append("end");
                return;
            }
            sb.append(historyToString(historyManager));
            bw.write(String.valueOf(sb));
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage(), e.getCause());
        }
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


    private boolean checkFile() {
        if (file.exists()) {
            return true;
        } else {
            return createFile();
        }
    }


    private boolean createFile() {
        Path testFile;
        try {
            testFile = Files.createFile(Paths.get(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Files.exists(testFile);
    }

    private static class ManagerSaveException extends RuntimeException {
        public ManagerSaveException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    private static class ManagerReadException extends RuntimeException {
        public ManagerReadException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
