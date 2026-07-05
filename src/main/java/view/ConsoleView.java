package view;

import model.Task;

import java.io.IOException;
import java.util.Scanner;

import static java.lang.System.out;

public class ConsoleView {

    private Scanner scanner;

    public ConsoleView(Scanner scanner) {
        this.scanner = scanner;
    }

    public int showMainMenu() throws IOException {
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

        return userChoice;
    }

    public Task readTask() throws IOException {
        out.print("Enter Task Title:");
        String taskTitle =  scanner.nextLine();

        out.print("Enter Task Subject:");
        String taskSubject =  scanner.nextLine();

        out.print("Enter Task Type:");
        String taskType =  scanner.nextLine();

        out.print("Enter Task Due-Date:");
        String taskDueDate =  scanner.nextLine();

        out.print("Enter Task Estimated Hours:");
        int taskEstimatedHours =  scanner.nextInt();

        var newTask = Task(taskTitle)
    }
}