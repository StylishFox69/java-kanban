package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.HashMap;

public class TaskManager {
     HashMap<Integer, Task> tasks;
     HashMap<Integer, Epic> epics;
     HashMap<Integer, SubTask> subTasks;
    int seq = 0;

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

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        Epic savedEpic = epics.get(epic.getId());
        savedEpic.setSubTaskIds(epic.getSubTaskIds());
        savedEpic.setName(epic.getName());
        savedEpic.setDescription(epic.getDescription());
        calculateEpicStatus(savedEpic);
    }

    public void updateSubTask(SubTask subTask) {
        SubTask savedSubTask = subTasks.get(subTask.getId());
        savedSubTask.setName(subTask.getName());
        savedSubTask.setStatus(subTask.getStatus());
        savedSubTask.setDescription(subTask.getDescription());
        Epic epic = epics.get(subTask.getEpicId());
        Epic savedEpic = epics.get(subTask.getEpicId());
        if (!epics.containsKey(savedEpic.getId())) {
            return;
        }
        subTasks.put(savedSubTask.getId(), savedSubTask);
        calculateEpicStatus(epic);
    }

    public void deleteTask(int id) {
        tasks.remove(id);
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
        calculateEpicStatus(epic);
    }

    public void printTasks(){
        for (Task task : tasks.values()) {
            System.out.println(task);
        }
    }

    public void printEpics(){
        for (Epic epic : epics.values()) {
            System.out.println(epic);
        }
    }

    public void printSubTasks(){
        for (SubTask subTask : subTasks.values()) {
            System.out.println(subTask);
        }
    }

    public void printSubTasksByEpic(int id){
        Epic epic = epics.get(id);
        for (Integer subTaskId : epic.subTaskIds) {
            System.out.println(subTasks.get(subTaskId));
        }
    }

    public void deleteAllTasks(){
tasks.clear();
    }

    public void deleteAllEpics(){
epics.clear();
    }

    public void deleteAllSubtasks(){
subTasks.clear();
    }



    private void calculateEpicStatus(Epic epic) {
        int completedTasks = 0;
        int inProgressTasks = 0;
        for (Integer subTaskId : epic.getSubTaskIds()) {

            if (subTasks.get(subTaskId).getStatus() == (Status.DONE)) {
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
