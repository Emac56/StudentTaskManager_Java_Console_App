package controller;

import model.Task;
import service.TaskService;

import static java.lang.System.clearProperty;
import static java.lang.System.out;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

public class TaskController {
    private TaskService taskService;
    private ConsoleView consoleView;

    public TaskController(TaskService taskService,
                          ConsoleView consoleView) {
        this.taskService = taskService;
        this.consoleView = consoleView;
    }

    public void start() throws IOException {
        Scanner scanner = new Scanner(System.in);

        boolean run = true;

        while (run) {


            switch (userChoice) {
                case 1:
                    addTaskFlow();
                    break;
                case 2:
                    viewAllTaskFlow();
                    break;
                case 3:
                    searchTaskFlow();
                    break;
                case 4:
                    updateTaskFlow();
                    break;
                case 5:
                    markCompletedFlow();
                    break;
                case 6:
                    deleteTaskFlow();
                    break;
                case 7:
                    viewPendingFlow();
                    break;
                case 8:
                    viewCompletedFlow();
                    break;
                case 9:
                    run = false;
                    out.println("Exiting program...");
                    break;

                default:
                    out.println("Invalid input!");
            }
        }
        scanner.close();
    }

    public void addTaskFlow() throws IOException {

        Task task = view.getTaskInput();

        String result = taskService.addTask(task);

        view.displayMessage(result);
    }

    public void viewAllTaskFlow() throws IOException {

        List<Task> tasks = taskService.viewAllTask();
        view.displayTask(tasks);
    }

    public void searchTaskFlow() throws IOException {

        Long taskId = view.getTaskIdInput();

        Task searchTask = taskService.searchTask(taskId);

        view.displayTask(searchTask);
    }

    public void updateTaskFlow() throws IOException {

        Long taskId = view.getTaskIdInput();

        Task updatedTask = view.getUpdatedTaskInput();

        updatedTask.setTaskId(taskId);

        String result = taskService.updateTask(updatedTask);

        view.displayMessage(result);

    }

    public void markCompletedFlow() throws IOException {

        Long taskId = view.getTaskIdInput();

        String result = taskService.markTaskCompleted(taskId);

        view.displayMessage(result);
    }

    public void deleteTaskFlow() throws IOException {

        Long taskId = view.getTaskIdInput();

        String result = taskService.deleteTask(taskId);

        view.displayMessage(result);
    }

    public void viewPendingFlow() throws IOException {

        List<Task> pendingTask = taskService.viewPendingTask();

        view.displayTask(pendingTask);
    }

    public void viewCompletedFlow() {

        List<Task> completedTask = taskService.viewCompletedTask();

        view.displayTask(completedTask);
    }
}