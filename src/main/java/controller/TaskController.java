package controller;

import model.Task;
import service.TaskService;
import view.ConsoleView;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import static java.lang.System.out;

public class TaskController {
    private TaskService taskService;
    private ConsoleView consoleView;

    public TaskController(TaskService taskService,
                          ConsoleView consoleView) {
        this.taskService = taskService;
        this.consoleView = consoleView;
    }

    public void start() {
            
        boolean run = true;

        while (run) {

            try {    
            int userChoice = consoleView.showMainMenu();

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
                    consoleView.displayMessage("Exiting... program");
                    break;

                default:
                    consoleView.displayMessage("Invalid input!");
            }
        } catch (IOException e) {
                consoleView.displayMessage(e.getMessage());
        }
    }
    }

    public void addTaskFlow() throws IOException {

        Task task = consoleView.readTask();

        String message = taskService.addTask(task);

        consoleView.displayMessage(message);
    }

    public void viewAllTaskFlow() throws IOException {

        List<Task> taskList = taskService.getAllTasks();
        consoleView.displayTasks(taskList);
    }

    public void searchTaskFlow() throws IOException {

        Long taskId = consoleView.readTaskId();

        Task task = taskService.searchTask(taskId);

        consoleView.displayTask(task);
    }

    public void updateTaskFlow() throws IOException {

        Long taskId = consoleView.readTaskId();

        Task updatedTask = consoleView.readTask();

        updatedTask.setTaskId(taskId);

        String message = taskService.updateTask(updatedTask);

        consoleView.displayMessage(message);

    }

    public void markCompletedFlow() throws IOException {

        Long taskId = consoleView.readTaskId();

        String message = taskService.markTaskCompleted(taskId);

        consoleView.displayMessage(message);
    }

    public void deleteTaskFlow() throws IOException {

        Long taskId = consoleView.readTaskId();

        String message = taskService.deleteTask(taskId);

        consoleView.displayMessage(message);
    }

    public void viewPendingFlow() throws IOException {

        List<Task> pendingTask = taskService.viewPendingTask();

        consoleView.displayTasks(pendingTask);
    }

    public void viewCompletedFlow() throws IOException {

        List<Task> completedTask = taskService.viewCompletedTask();

        consoleView.displayTasks(completedTask);
    }
}