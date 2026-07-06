package controller;

import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.TaskService;
import view.ConsoleView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @Mock
    private ConsoleView consoleView;

    private TaskController controller;

    @BeforeEach
    void setUp() {
        controller = new TaskController(taskService, consoleView);
    }
     @Test
void shouldAddTask() throws IOException {

    Task task = new Task(
            null,
            "Java",
            "Programming",
            "Project",
            "2026",
            5,
            null
    );

    when(consoleView.readTask()).thenReturn(task);
    when(taskService.addTask(task)).thenReturn("Task Added");

    controller.addTaskFlow();

    verify(consoleView).readTask();
    verify(taskService).addTask(task);
    verify(consoleView).displayMessage("Task Added");
}
    
    @Test
void shouldDisplayAllTasks() throws IOException {

    List<Task> list = new ArrayList<>();

    when(taskService.getAllTasks()).thenReturn(list);

    controller.viewAllTaskFlow();

    verify(taskService).getAllTasks();
    verify(consoleView).displayTasks(list);
}
    
    @Test
void shouldSearchTask() throws IOException {

    Task task = new Task(
            1L,
            "Java",
            "Programming",
            "Project",
            "2026",
            5,
            "Pending"
    );

    when(consoleView.readTaskId()).thenReturn(1L);
    when(taskService.searchTask(1L)).thenReturn(task);

    controller.searchTaskFlow();

    verify(taskService).searchTask(1L);
    verify(consoleView).displayTask(task);
}
    
    @Test
void shouldUpdateTask() throws IOException {

    Task task = new Task(
            null,
            "Updated",
            "Programming",
            "Homework",
            "2026",
            4,
            null
    );

    when(consoleView.readTaskId()).thenReturn(1L);
    when(consoleView.readTask()).thenReturn(task);
    when(taskService.updateTask(task)).thenReturn("Updated");

    controller.updateTaskFlow();

    verify(taskService).updateTask(task);
    verify(consoleView).displayMessage("Updated");
}
    
    @Test
void shouldMarkCompleted() throws IOException {

    when(consoleView.readTaskId()).thenReturn(1L);
    when(taskService.markTaskCompleted(1L))
            .thenReturn("Completed");

    controller.markCompletedFlow();

    verify(taskService).markTaskCompleted(1L);
    verify(consoleView).displayMessage("Completed");
}
    
    @Test
void shouldDeleteTask() throws IOException {

    when(consoleView.readTaskId()).thenReturn(1L);
    when(taskService.deleteTask(1L))
            .thenReturn("Deleted");

    controller.deleteTaskFlow();

    verify(taskService).deleteTask(1L);
    verify(consoleView).displayMessage("Deleted");
}
    
    @Test
void shouldViewPendingTasks() throws IOException {

    List<Task> list = new ArrayList<>();

    when(taskService.viewPendingTask()).thenReturn(list);

    controller.viewPendingFlow();

    verify(taskService).viewPendingTask();
    verify(consoleView).displayTasks(list);
}
    
    @Test
void shouldViewCompletedTasks() throws IOException {

    List<Task> list = new ArrayList<>();

    when(taskService.viewCompletedTask()).thenReturn(list);

    controller.viewCompletedFlow();

    verify(taskService).viewCompletedTask();
    verify(consoleView).displayTasks(list);
}
    
    @Test
void shouldExitProgram() throws IOException {

    when(consoleView.showMainMenu()).thenReturn(9);

    controller.start();

    verify(consoleView).showMainMenu();
    verify(consoleView).displayMessage("Exiting... program");
}
    
    @Test
void shouldDisplayInvalidInputMessage() throws IOException {

    when(consoleView.showMainMenu())
            .thenReturn(100)
            .thenReturn(9);

    controller.start();

    verify(consoleView).displayMessage("Invalid input!");
}
    
    @Test
void shouldCallAddTaskFlow() throws IOException {

    Task task = new Task(
            null,
            "Java",
            "Programming",
            "Project",
            "2026",
            5,
            null
    );

    when(consoleView.showMainMenu())
            .thenReturn(1)
            .thenReturn(9);

    when(consoleView.readTask()).thenReturn(task);
    when(taskService.addTask(task))
            .thenReturn("Added");

    controller.start();

    verify(taskService).addTask(task);
}
    
    @Test
void shouldCallViewAllTaskFlow() throws IOException {

    when(consoleView.showMainMenu())
            .thenReturn(2)
            .thenReturn(9);

    when(taskService.getAllTasks())
            .thenReturn(new ArrayList<>());

    controller.start();

    verify(taskService).getAllTasks();
}
}
    