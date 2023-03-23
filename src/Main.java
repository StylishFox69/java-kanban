import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task = taskManager.createTask(new Task());
        System.out.println("create task: " + task);

        Task taskFromManager = taskManager.getTask(task.getId());
        System.out.println("get task: " + taskFromManager);

        taskFromManager.setName("New name");
        taskManager.updateTask(taskFromManager);
        System.out.println("update task: " + taskFromManager);


        Epic epic1 = taskManager.createEpic(new Epic("Epic1", "DS"));
        System.out.println("create epic 1: " + epic1);

        SubTask subTask1Epic1 = taskManager.createSubTask(new SubTask("SubTask 1.1", "DS", Status.IN_PROGRESS, epic1.getId()));
        System.out.println("create subTask 1.1: " + subTask1Epic1);

        SubTask subTask2Epic1 = taskManager.createSubTask(new SubTask("SubTask 1.2", "DS", Status.NEW, epic1.getId()));
        System.out.println("create subTask 1.2: " + subTask2Epic1);

        Epic epic2 = taskManager.createEpic(new Epic("Epic2", "DS"));
        System.out.println("create epic 2: " + epic2);

        SubTask subTask1Epic2 = taskManager.createSubTask(new SubTask("SubTask 2.1", "DS", Status.DONE, epic2.getId()));
        System.out.println("create subTask 2.1: " + subTask1Epic2);

        Epic epicFromManager = taskManager.getEpic(epic1.getId());
        System.out.println("get epic 1: " + epicFromManager);

        Epic epicFromManager2 = taskManager.getEpic(epic2.getId());
        System.out.println("get epic 2: " + epicFromManager2);

        System.out.println("Print epics: ");
        System.out.println(taskManager.getEpics());
        System.out.println("Print tasks: ");
        System.out.println(taskManager.getTasks());
        System.out.println("Print subTasks: ");
        System.out.println(taskManager.getSubTasks());

        SubTask subTaskFromManager = taskManager.getSubTask(subTask1Epic1.getId());
        System.out.println("get subTask 1.1: " + subTaskFromManager);
        subTaskFromManager.setName("new sub");
        subTaskFromManager.setStatus(Status.DONE);

        taskManager.updateSubTask(subTaskFromManager);
        System.out.println("update subTask 1.1: " + subTaskFromManager);

        taskManager.updateEpic(epicFromManager);
        System.out.println("update epic 1: " + epicFromManager);

        taskManager.deleteTask(taskFromManager.getId());
        System.out.println("delete task: " + task);

        taskManager.deleteSubTask(subTask2Epic1.getId());
        System.out.println("delete subtask 1.2: " + subTask2Epic1);

        taskManager.updateEpic(epic2);
        System.out.println("Update epic 2: " + epic2);

        taskManager.updateEpic(epic1);
        System.out.println("Update epic 1: " + epic1);

        taskManager.deleteEpic(epic1.getId());
        System.out.println("delete epic 1: " + epic1);

        System.out.println("Print epics: ");
        System.out.println(taskManager.getEpics());
        System.out.println("Print tasks: ");
        System.out.println(taskManager.getTasks());
        System.out.println("Print subTasks: ");
        System.out.println(taskManager.getSubTasks());

        System.out.println("get subtask by epicId");
        System.out.println(taskManager.getSubTasksByEpic(epic2.getId()));

        taskManager.deleteAllTasks();
        taskManager.deleteAllSubtasks();
        taskManager.deleteAllEpics();

        System.out.println("Print epics: ");
        System.out.println(taskManager.getEpics());
        System.out.println("Print tasks: ");
        System.out.println(taskManager.getTasks());
        System.out.println("Print subTasks: ");
        System.out.println(taskManager.getSubTasks());
    }
}
