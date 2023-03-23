package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    int seq = 0;
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, SubTask> subTasks;

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

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public SubTask getSubTask(int id) {
        return subTasks.get(id);
    }

    public Epic getEpic(int id) {
        return epics.get(id);
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

    public List<SubTask> getSubTasksByEpic(int id) {
        ArrayList<SubTask> subs = new ArrayList<>();
        for (Integer subTaskId : epics.get(id).subTaskIds) {
            subs.add(subTasks.get(subTaskId));
        }
        return subs;
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        boolean isEpicExists = false;
        for (Integer key : epics.keySet()) {
            Epic savedEpic = epics.get(key);
            if (savedEpic.getId() == epic.getId()) {
                isEpicExists = true;
            }
        }
        if (!isEpicExists) {
            System.out.println("No such epic in memory");
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

    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public void deleteEpic(int id) {
        Epic epic = epics.get(id);
        ArrayList<Integer> subTaskKeys = new ArrayList<>();
        for (Integer key : subTasks.keySet()) {
            if (subTasks.get(key).getEpicId() == epic.getId()) {
                subTaskKeys.add(key);
            }
        }
        for (Integer subTaskKey : subTaskKeys) {
            subTasks.remove(subTaskKey);
        }
        epics.remove(id);
    }

    public void deleteSubTask(int id) {
        Epic epic = epics.get(subTasks.get(id).getEpicId());
        epic.subTaskIds.removeIf(checkId -> checkId == id);
        subTasks.remove(id);
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
