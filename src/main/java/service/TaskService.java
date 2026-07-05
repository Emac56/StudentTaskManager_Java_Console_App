package service;

import model.Task;
import repository.TaskRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TaskService {
    private TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    public String addTask(Task task) throws IOException {

        List<Task> taskList = taskRepository.loadTask();

        if (taskList.contains(task)) {
            return "Task already exist.";
        }
        task.setTaskId(generateTaskId());
        taskRepository.saveTask(task);

        return "Task added successfully.";
    }
    public List<Task> viewAllTask() throws IOException {

        List<Task> taskList = taskRepository.loadTask();

        return taskList;
    }
    public Task searchTask(Long taskId) throws IOException {

        Task task = taskRepository.findTaskById(taskId);

        return task;
    }

    public String updateTask(Task updatedTask) throws IOException {

        Task existingTask = taskRepository.findTaskById(updatedTask.getTaskId());

        if (existingTask != null) {
            taskRepository.updateTask(updatedTask);
            return "Task updated successfully.";
        } else {
            return "Task not found.";
        }
    }
    public String deleteTask(Long taskId) throws IOException {

        Task task = taskRepository.findTaskById(taskId);

        if (task != null) {
            taskRepository.deleteTask(taskId);
            return "Task deleted Successfully.";
        } else {
            return "Task not found.";
        }
    }
    public String markTaskCompleted(Long taskId) throws IOException {

        Task task  = taskRepository.findTaskById(taskId);

        if (task != null) {
            task.setStatus("Completed");
            taskRepository.updateTask(task);
            return "Task mark as completed.";
        } else {
            return "Task not found";
        }
    }
    public List<Task> viewPendingTask() throws IOException{

        List<Task> taskList =  taskRepository.loadTask();

        List<Task> pendingTask = new ArrayList<>();

        for (Task task : taskList) {
            if (task.getStatus().equalsIgnoreCase("Pending")) {
                pendingTask.add(task);
            }
        } return pendingTask;
    }
    public List<Task> viewCompletedTask() throws IOException {

        List<Task> taskList = taskRepository.loadTask();
        List<Task> completedTask = new ArrayList<>();

        for (Task task : taskList) {
            if (task.getStatus().equalsIgnoreCase("Completed")) {
                completedTask.add(task);
            }
        } return completedTask;
    }

    public Long generateTaskId() throws IOException {

        List<Task> taskList = taskRepository.loadTask();

        Long lastTaskId = 0L;

        for (Task task : taskList) {
            if (task.getTaskId() > lastTaskId) {
                lastTaskId = task.getTaskId();
            }
        } return lastTaskId + 1;
    }
}