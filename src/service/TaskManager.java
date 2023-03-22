package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.HashMap;

public class TaskManager {
    public HashMap<Integer, Task> tasks;
    public HashMap<Integer, Epic> epics;
    public HashMap<Integer, SubTask> subTasks;
    int seq = 0;

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.epics = new HashMap<>();
    }

    public SubTask createSubTask(SubTask subTask) {
        subTask.setId(++seq);
        subTasks.put(subTask.getId(), subTask);
        Epic epic = epics.get(subTask.getEpicId());
        epic.getSubTaskIds().add(subTask.getId());
        epic.setStatus(calculateEpicStatus(epic));
        return subTasks.get(subTask.getId());
    }

    public Task createTask(Task task) {
        task.setId(++seq);
        tasks.put(task.getId(), task);
        return tasks.get(task.getId());
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public SubTask getSubTask(int id) {
        return subTasks.get(id);
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }


    public Epic createEpic(Epic epic) {
        epic.setId(++seq);
        epics.put(epic.getId(), epic);
        return epic;
    }

    public void updateEpic(Epic epic) {
        Epic saved = epics.get(epic.getId());
        saved.setSubTaskIds(epic.getSubTaskIds());
        saved.setName(epic.getName());
        saved.setDescription(epic.getDescription());
    }

    public void updateSubTask(SubTask subTask) {
        SubTask saved = subTasks.get(subTask.getId());
        saved.setName(subTask.getName());
        saved.setStatus(subTask.getStatus());
        saved.setDescription(subTask.getDescription());
        Epic epic = epics.get(subTask.getEpicId());
        Epic savedEpic = epics.get(subTask.getEpicId());
        if (!epics.containsKey(savedEpic.getId())) {
            return;
        }
        subTasks.put(saved.getId(), saved);
        epic.setStatus(calculateEpicStatus(savedEpic));


    }

    private String calculateEpicStatus(Epic epic) {
        int completedTasks = 0;
        int inProgressTasks = 0;
        String status;
        for (Integer subTaskId : epic.getSubTaskIds()) {

            if (subTasks.get(subTaskId).getStatus().equals(Status.DONE.name())) {
                completedTasks++;
            } else if (subTasks.get(subTaskId).getStatus().equals(Status.IN_PROGRESS.name())) {
                inProgressTasks++;
            }
        }
        if (completedTasks == epic.getSubTaskIds().size()) {
            status = Status.DONE.name();
        } else if (inProgressTasks > 0) {
            status = Status.IN_PROGRESS.name();
        } else {
            status = Status.NEW.name();
        }
        return status;
    }

    public void delete(int id) {
        tasks.remove(id);
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    public void deleteEpic(int id) {
        Epic epic = epics.get(id);
        for (int i = 0; i < epic.subTaskIds.size(); i++) {
            subTasks.remove(epic.subTaskIds.get(i));
        }
        epics.remove(id);
    }


    public void deleteSubTask(int id) {
        Epic epic = epics.get(subTasks.get(id).getEpicId());
        epic.subTaskIds.removeIf(checkId -> checkId == id);
        subTasks.remove(id);
        epic.setStatus(calculateEpicStatus(epic));
    }
}
