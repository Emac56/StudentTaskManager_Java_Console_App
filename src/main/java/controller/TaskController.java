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
            out.println("\n======Task Menu======");
            out.println("[1] Add Task");
            out.println("[2] View all task");
            out.println("[3] Search Task");
            out.println("[4] Update Task");
            out.println("[5] Mark Completed");
            out.println("[6] Delete Task");
            out.println("[7] View Pending Task");
            out.println("[8] View Completed Task");
            out.println("[9] View Today's Priority");
            out.println("[10] Exit");
            out.print("Enter choice : ");
            int userChoice = scanner.nextInt();

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
                    viewTodayPriotityFlow();
                    break;
                case 10:
                    run = false;
                    out.println("Exiting program...");
                    break;

                default:
                    out.println("Invalid input!");
            }
        }
        scanner.close();
    }

    void addTaskFlow() throws IOException {

        Task task = view.getTaskInput();

        String result = taskService.addTask(task);

        view.displayMessage(result);
    }

    void viewAllTaskFlow() throws IOException {

        List<Task> result = taskService.viewAllTask();
        view.displayTask(task);
    }

    static void searchTaskFlow() {
    }

    static void updateTaskFlow() {
    }

    static void markCompletedFlow() {
    }

    static void deleteTaskFlow() {
    }

    static void viewPendingFlow() {
    }

    static void viewCompletedFlow() {
    }

    static void viewTodayPriotityFlow() {
    }
}

