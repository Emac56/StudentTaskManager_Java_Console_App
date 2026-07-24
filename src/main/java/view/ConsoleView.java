package view;

import model.Task;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.out;

public class ConsoleView {

    private Scanner scanner;

    public ConsoleView(Scanner scanner) {
        this.scanner = scanner;
    }

    public int showMainMenu() {
        out.println("===========================");
        out.println("    STUDENT TASK MANAGER   ");
        out.println("===========================");
        out.println("[1] Add Task");
        out.println("[2] View all task");
        out.println("[3] Search Task");
        out.println("[4] Update Task");
        out.println("[5] Mark Completed");
        out.println("[6] Delete Task");
        out.println("[7] View Pending Task");
        out.println("[8] View Completed Task");
        out.println("[9] Exit");
        
        while (true) {
            out.print("Enter choice: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= 9) {
                    return choice;
                }
                out.println("Error: Choice must be between 1 and 9. Please try again.");
            } catch (NumberFormatException e) {
                out.println("Error: Invalid input. Please enter a valid number (1-9).");
            }
        }
    }

    private String readStringInput(String prompt) {
        while (true) {
            out.print(prompt);
            String input = scanner.nextLine();
            if (input.contains("|")) {
                out.println("Error: Input cannot contain the pipe character '|'. Please try again.");
            } else {
                return input;
            }
        }
    }

    public Task readTask() throws IOException {
        String taskTitle = readStringInput("\nEnter Task Title:");
        String taskSubject = readStringInput("Enter Task Subject:");
        String taskType = readStringInput("Enter Task Type:");
        String taskDueDate = readStringInput("Enter Task Due-Date:");

        int taskEstimatedHours = 0;
        while (true) {
            out.print("Enter Task Estimated Hours:");
            try {
                taskEstimatedHours = Integer.parseInt(scanner.nextLine());
                if (taskEstimatedHours < 0) {
                    out.println("Error: Estimated hours cannot be negative.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                out.println("Error: Invalid hours. Please enter a valid integer.");
            }
        }

        return new Task(
                null,
                taskTitle,
                taskSubject,
                taskType,
                taskDueDate,
                taskEstimatedHours,
                null
        );
    }

    public Long readTaskId() {
        while (true) {
            out.print("\nEnter Task Id:");
            try {
                long id = Long.parseLong(scanner.nextLine());
                if (id <= 0) {
                    out.println("Error: Task ID must be a positive number. Please try again.");
                    continue;
                }
                return id;
            } catch (NumberFormatException e) {
                out.println("Error: Invalid Task ID. Please enter a valid number.");
            }
        }
    }

    public void displayTask(Task task) {
        if (task != null) {
            out.println("\n------------------------------------------");
            out.println("Task ID               :" + task.getTaskId());
            out.println("Task Title            : " + task.getTaskTitle());
            out.println("Subject               :" + task.getSubject());
            out.println("Task Type             :" + task.getTaskType());
            out.println("Due Date              : " + task.getDueDate());
            out.println("Estimated Hours       :" + task.getEstimatedHours());
            out.println("Status                :" + task.getStatus());
            out.println("------------------------------------------");         
        } else {
            out.println("\nTask not found");
        }
    }

    public void displayTasks(List<Task> taskList) {
        if (taskList.isEmpty()) {
            out.println("\nNo Tasks Found");
        } else {
            for (Task task : taskList) {
                out.println("\n----------------------------------------");
                out.println("Task ID          : " + task.getTaskId());
                out.println("Task Title       : " + task.getTaskTitle());
                out.println("Subject          : " + task.getSubject());
                out.println("Task Type        : " + task.getTaskType());
                out.println("Due Date         : " + task.getDueDate());
                out.println("Estimated Hours  : " + task.getEstimatedHours());
                out.println("Status           : " + task.getStatus());
                out.println("----------------------------------------");
                out.println();
            }
        }
    }

    public void displayMessage(String message) {
        out.println(message);
    }
}