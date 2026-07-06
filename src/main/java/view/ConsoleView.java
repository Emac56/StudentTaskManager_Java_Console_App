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
        out.print("Enter choice: ");
        int userChoice = scanner.nextInt();
        scanner.nextLine();

        return userChoice;
    }

    public Task readTask() throws IOException {
        out.print("\nEnter Task Title:");
        String taskTitle =  scanner.nextLine();

        out.print("Enter Task Subject:");
        String taskSubject =  scanner.nextLine();

        out.print("Enter Task Type:");
        String taskType =  scanner.nextLine();

        out.print("Enter Task Due-Date:");
        String taskDueDate =  scanner.nextLine();

        out.print("Enter Task Estimated Hours:");
        int taskEstimatedHours = Integer.parseInt(scanner.nextLine());

        return new Task(
        null,
        taskTitle,
        taskSubject,
        taskType,
        taskDueDate,
        taskEstimatedHours,
        null);
    }
    public Long readTaskId() {
        
        out.print("\nEnter Task Id:");
        Long taskId = Long.parseLong(scanner.nextLine());
        
        return taskId;
    }
    public void displayTask(Task task) {
        
        if(task != null) {
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