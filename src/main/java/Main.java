import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import repository.TaskRepository;
import service.TaskService;
import view.ConsoleView;
import controller.TaskController;

public class Main {
    
    public static void main (String[] args) throws IOException {
        
        var databaseFile = new File("database/tasks.txt");
        var taskRepository = new TaskRepository(databaseFile);
        var taskService = new TaskService(taskRepository);
        var scanner = new Scanner(System.in);
        var consoleView = new ConsoleView(scanner);
        var taskController = new TaskController(taskService, consoleView);
        taskController.start();
    }
}