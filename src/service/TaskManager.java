package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, SubTask> subTasks;
    private int seq = 0;

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.epics = new HashMap<>();
    }

    public Task createTask(Task task) {
        task.setId(++seq);
        tasks.put(task.getId(), task);
        return tasks.get(task.getId());
    }

    public SubTask createSubTask(SubTask subTask) {
        subTask.setId(++seq);
        subTasks.put(subTask.getId(), subTask);
        Epic epic = epics.get(subTask.getEpicId());
        epic.getSubTaskIds().add(subTask.getId());
        calculateEpicStatus(epic);
        return subTasks.get(subTask.getId());
    }

    public Epic createEpic(Epic epic) {
        epic.setId(++seq);
        epics.put(epic.getId(), epic);
        return epic;
    }

    public Task getTask(int taskId) {
        return tasks.get(taskId);
    }

    public SubTask getSubTask(int subTaskId) {
        return subTasks.get(subTaskId);
    }

    public Epic getEpic(int epicId) {
        return epics.get(epicId);
    }

    public List<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public List<SubTask> getSubTasksByEpic(int epicId) {
        ArrayList<SubTask> subs = new ArrayList<>();
        for (Integer subTaskId : epics.get(epicId).subTaskIds) {
            subs.add(subTasks.get(subTaskId));
        }
        return subs;
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            return;
        }
            Epic savedEpic = epics.get(epic.getId());
            savedEpic.setName(epic.getName());
            savedEpic.setDescription(epic.getDescription());
            calculateEpicStatus(savedEpic);
            epics.put(savedEpic.getId(), savedEpic);
    }

    public void updateSubTask(SubTask subTask) {
        SubTask savedSubTask = subTasks.get(subTask.getId());
        savedSubTask.setName(subTask.getName());
        savedSubTask.setStatus(subTask.getStatus());
        savedSubTask.setDescription(subTask.getDescription());
        Epic savedEpic = epics.get(subTask.getEpicId());
        if (!epics.containsKey(savedEpic.getId())) {
            return;
        }
        subTasks.put(savedSubTask.getId(), savedSubTask);
        calculateEpicStatus(savedEpic);
    }

    public void deleteTask(int taskId) {
        tasks.remove(taskId);
    }

    public void deleteEpic(int epicId) {
        Epic epic = epics.get(epicId);
        for (Integer subTaskId : epic.getSubTaskIds()) {
            subTasks.remove(subTaskId);
        }
        epics.remove(epicId);
    }

    public void deleteSubTask(int subTaskId) {
        Epic epic = epics.get(subTasks.get(subTaskId).getEpicId());
        epic.subTaskIds.removeIf(checkId -> checkId == subTaskId);
        subTasks.remove(subTaskId);
        calculateEpicStatus(epic);
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    public void deleteAllSubtasks() {
        subTasks.clear();
        for (Integer key : epics.keySet()) {
            calculateEpicStatus(epics.get(key));
        }
    }

    private void calculateEpicStatus(Epic epic) {
        int completedTasks = 0;
        int inProgressTasks = 0;
        for (Integer subTaskId : epic.getSubTaskIds()) {
            if (subTasks.size() == 0) {
                epic.setStatus(Status.NEW);
            } else if (subTasks.get(subTaskId).getStatus() == (Status.DONE)) {
                completedTasks++;
            } else if (subTasks.get(subTaskId).getStatus() == (Status.IN_PROGRESS)) {
                inProgressTasks++;
            }
        }
        if (completedTasks == epic.getSubTaskIds().size()) {
            epic.setStatus(Status.DONE);
        } else if (inProgressTasks > 0) {
            epic.setStatus(Status.IN_PROGRESS);
        } else {
            epic.setStatus(Status.NEW);
        }
    }
}
