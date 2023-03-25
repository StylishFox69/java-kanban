package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.List;

public interface TaskManager {
    Task createTask(Task task);

    SubTask createSubTask(SubTask subTask);

    Epic createEpic(Epic epic);

    Task getTask(int taskId);

    SubTask getSubTask(int subTaskId);

    Epic getEpic(int epicId);

    List<SubTask> getSubTasks();

    List<Task> getTasks();

    List<Epic> getEpics();

    List<SubTask> getSubTasksByEpic(int epicId);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(SubTask subTask);

    void deleteTask(int taskId);

    void deleteEpic(int epicId);

    void deleteSubTask(int subTaskId);

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    List<Task> getHistory();

}
