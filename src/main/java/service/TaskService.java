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

    /**
     * Business-layer invariant: rejects any Task whose string fields contain the
     * pipe character '|'.  The persistence format is pipe-delimited, so a '|'
     * inside a field value shifts every subsequent column index and corrupts the
     * file.  Raising here (before any repository call) is the earliest, cheapest
     * place to enforce this contract; TaskRepository.saveTask / saveAllTask adds
     * a second, defensive check so the invariant holds even when TaskService is
     * bypassed in tests or future code paths.
     *
     * @throws IllegalArgumentException if any string field contains '|'
     */
    private void validateNoPipe(Task task) {
        checkField("taskTitle", task.getTaskTitle());
        checkField("subject",   task.getSubject());
        checkField("taskType",  task.getTaskType());
        checkField("dueDate",   task.getDueDate());
        checkField("status",    task.getStatus());
    }

    private void checkField(String fieldName, String value) {
        if (value != null && value.contains("|")) {
            throw new IllegalArgumentException(
                "Field '" + fieldName + "' must not contain the pipe character '|'."
            );
        }
    }

    public String addTask(Task task) throws IOException {
        validateNoPipe(task);

        List<Task> taskList = taskRepository.loadTask();

        if (taskList.contains(task)) {
            return "\nTask already exist.";
        }
        task.setTaskId(generateTaskId());
        task.setStatus("Pending");
        taskRepository.saveTask(task);

        return "\nTask added successfully.";
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
        validateNoPipe(updatedTask);

        Task existingTask = taskRepository.findTaskById(updatedTask.getTaskId());

        if (existingTask != null) {
            // Preserve the existing status if the updatedTask status is null
            if (updatedTask.getStatus() == null) {
                updatedTask.setStatus(existingTask.getStatus());
            }
            taskRepository.updateTask(updatedTask);
            return "\nTask updated successfully.";
        } else {
            return "\nTask not found.";
        }
    }

    public String deleteTask(Long taskId) throws IOException {

        Task task = taskRepository.findTaskById(taskId);

        if (task != null) {
            taskRepository.deleteTask(taskId);
            return "\nTask deleted Successfully.";
        } else {
            return "\nTask not found.";
        }
    }

    public String markTaskCompleted(Long taskId) throws IOException {

        Task task = taskRepository.findTaskById(taskId);

        if (task != null) {
            task.setStatus("Completed");
            taskRepository.updateTask(task);
            return "\nTask mark as completed.";
        } else {
            return "\nTask not found";
        }
    }

    public List<Task> viewPendingTask() throws IOException {

        List<Task> taskList = taskRepository.loadTask();

        List<Task> pendingTask = new ArrayList<>();

        for (Task task : taskList) {
            if (task.getStatus().equalsIgnoreCase("Pending")) {
                pendingTask.add(task);
            }
        }
        return pendingTask;
    }

    public List<Task> viewCompletedTask() throws IOException {

        List<Task> taskList = taskRepository.loadTask();
        List<Task> completedTask = new ArrayList<>();

        for (Task task : taskList) {
            if (task.getStatus().equalsIgnoreCase("Completed")) {
                completedTask.add(task);
            }
        }
        return completedTask;
    }

    public Long generateTaskId() throws IOException {

        List<Task> taskList = taskRepository.loadTask();

        Long lastTaskId = 0L;

        for (Task task : taskList) {
            if (task.getTaskId() > lastTaskId) {
                lastTaskId = task.getTaskId();
            }
        }
        return lastTaskId + 1;
    }

    public List<Task> getAllTasks() throws IOException {

        return taskRepository.loadTask();
    }
}
