package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, SubTask> subTasks;
    HistoryManager historyManager;
    private int seq = 0;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
        this.tasks = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.epics = new HashMap<>();
    }

    @Override
    public Task createTask(Task task) {
        task.setId(++seq);
        tasks.put(task.getId(), task);
        return tasks.get(task.getId());
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        subTask.setId(++seq);
        subTasks.put(subTask.getId(), subTask);
        Epic epic = epics.get(subTask.getEpicId());
        epic.getSubTaskIds().add(subTask.getId());
        calculateEpicStatus(epic);
        return subTasks.get(subTask.getId());
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(++seq);
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public Task getTask(int taskId) {
        Task task = tasks.get(taskId);
        if (task != null) {
            historyManager.addTask(task);
        }
        return task;
    }

    @Override
    public SubTask getSubTask(int subTaskId) {
        SubTask subTask = subTasks.get(subTaskId);
        if (subTask != null) {
            historyManager.addTask(subTask);
        }
        return subTasks.get(subTaskId);
    }

    @Override
    public Epic getEpic(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            historyManager.addTask(epic);
        }
        return epics.get(epicId);
    }

    @Override
    public List<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<SubTask> getSubTasksByEpic(int epicId) {
        ArrayList<SubTask> subs = new ArrayList<>();
        for (Integer subTaskId : epics.get(epicId).subTaskIds) {
            subs.add(subTasks.get(subTaskId));
        }
        return subs;
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
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

    @Override
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

    @Override
    public void deleteTask(int taskId) {
        tasks.remove(taskId);
    }

    @Override
    public void deleteEpic(int epicId) {
        Epic epic = epics.get(epicId);
        for (Integer subTaskId : epic.getSubTaskIds()) {
            subTasks.remove(subTaskId);
        }
        epics.remove(epicId);
    }

    @Override
    public void deleteSubTask(int subTaskId) {
        Epic epic = epics.get(subTasks.get(subTaskId).getEpicId());
        epic.subTaskIds.removeIf(checkId -> checkId == subTaskId);
        subTasks.remove(subTaskId);
        calculateEpicStatus(epic);
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    @Override
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

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}