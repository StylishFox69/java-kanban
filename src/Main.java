import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import service.Managers;
import service.TaskManager;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefaultTaskManager();
        Epic epic0 = taskManager.createEpic(new Epic("0", "DSSD"));
        SubTask subTask01 = taskManager.createSubTask(new SubTask("2", "ssd", Status.DONE, epic0.getId()));
        SubTask subTask02 = taskManager.createSubTask(new SubTask("3", "ds", Status.NEW, epic0.getId()));
        SubTask subTask03 = taskManager.createSubTask(new SubTask("4", "ds", Status.IN_PROGRESS, epic0.getId()));
        Epic epic1 = taskManager.createEpic(new Epic("1", "DSSD"));

        taskManager.getEpic(epic0.getId());
        List<Task> history = new ArrayList<>(taskManager.getHistory());
        for (Task task : history) {
            System.out.println(task);
        }
        taskManager.getEpic(epic1.getId());
        System.out.println("hhh1");
        List<Task> history1 = new ArrayList<>(taskManager.getHistory());
        for (Task task : history1) {
            System.out.println(task);
        }
        taskManager.getEpic(epic0.getId());
        System.out.println("hhh1");
        List<Task> history2 = new ArrayList<>(taskManager.getHistory());
        for (Task task : history2) {
            System.out.println(task);
        }
        taskManager.getSubTask(subTask02.getId());
        System.out.println("hhh1");
        List<Task> history3 = new ArrayList<>(taskManager.getHistory());
        for (Task task : history3) {
            System.out.println(task);
        }
        taskManager.getSubTask(subTask01.getId());
        System.out.println("hhh1");
        List<Task> history4 = new ArrayList<>(taskManager.getHistory());
        for (Task task : history4) {
            System.out.println(task);
        }
        taskManager.getSubTask(subTask03.getId());
        System.out.println("hhh1");
        List<Task> history5 = new ArrayList<>(taskManager.getHistory());
        for (Task task : history5) {
            System.out.println(task);
        }
        taskManager.getSubTask(subTask02.getId());
        System.out.println("hhh");
        List<Task> history6 = new ArrayList<>(taskManager.getHistory());
        for (Task task : history6) {
            System.out.println(task);
        }
        taskManager.deleteSubTask(subTask01.getId());
        System.out.println("hhh1");
        List<Task> history7 = taskManager.getHistory();
        for (Task task : history7) {
            System.out.println(task);
        }
        taskManager.deleteEpic(epic0.getId());
        System.out.println("hhh1");
        List<Task> history8 = taskManager.getHistory();
        for (Task task : history8) {
            System.out.println(task);
        }
        taskManager.deleteEpic(epic1.getId());
        System.out.println("hhh1");
        List<Task> history9 = taskManager.getHistory();
        for (Task task : history9) {
            System.out.println(task);
        }
    }
}
