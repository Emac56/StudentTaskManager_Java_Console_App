package view;

import model.Task;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ConsoleViewTest {

    @Test
    void shouldReturnUserChoice() {

        String input = "1\n";

        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        ConsoleView consoleView = new ConsoleView(scanner);

        int choice = consoleView.showMainMenu();

        assertEquals(1, choice);
    }

    @Test
    void shouldReadTask() throws Exception {

        String input =
                "Java Project\n" +
                "Programming\n" +
                "Project\n" +
                "2026-07-10\n" +
                "10\n";

        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        ConsoleView consoleView = new ConsoleView(scanner);

        Task task = consoleView.readTask();

        assertNull(task.getTaskId());
        assertEquals("Java Project", task.getTaskTitle());
        assertEquals("Programming", task.getSubject());
        assertEquals("Project", task.getTaskType());
        assertEquals("2026-07-10", task.getDueDate());
        assertEquals(10, task.getEstimatedHours());
        assertNull(task.getStatus());
    }

    @Test
    void shouldReadTaskId() {

        String input = "15\n";

        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        ConsoleView consoleView = new ConsoleView(scanner);

        Long id = consoleView.readTaskId();

        assertEquals(15L, id);
    }

    @Test
    void shouldDisplayTaskWithoutThrowingException() {

        Scanner scanner = new Scanner(new ByteArrayInputStream(new byte[0]));

        ConsoleView consoleView = new ConsoleView(scanner);

        Task task = new Task(
                1L,
                "Java",
                "Programming",
                "Project",
                "2026",
                5,
                "Pending"
        );

        assertDoesNotThrow(() -> consoleView.displayTask(task));
    }

    @Test
    void shouldDisplayTaskNotFoundWithoutThrowingException() {

        Scanner scanner = new Scanner(new ByteArrayInputStream(new byte[0]));

        ConsoleView consoleView = new ConsoleView(scanner);

        assertDoesNotThrow(() -> consoleView.displayTask(null));
    }

    @Test
    void shouldDisplayTaskListWithoutThrowingException() {

        Scanner scanner = new Scanner(new ByteArrayInputStream(new byte[0]));

        ConsoleView consoleView = new ConsoleView(scanner);

        List<Task> tasks = List.of(
                new Task(
                        1L,
                        "Java",
                        "Programming",
                        "Project",
                        "2026",
                        5,
                        "Pending"
                )
        );

        assertDoesNotThrow(() -> consoleView.displayTasks(tasks));
    }

    @Test
    void shouldDisplayEmptyTaskListWithoutThrowingException() {

        Scanner scanner = new Scanner(new ByteArrayInputStream(new byte[0]));

        ConsoleView consoleView = new ConsoleView(scanner);

        assertDoesNotThrow(() -> consoleView.displayTasks(List.of()));
    }

    @Test
    void shouldDisplayMessageWithoutThrowingException() {

        Scanner scanner = new Scanner(new ByteArrayInputStream(new byte[0]));

        ConsoleView consoleView = new ConsoleView(scanner);

        assertDoesNotThrow(() ->
                consoleView.displayMessage("Hello"));
    }
    @Test
    void shouldRetryMenuSelectionOnInvalidInput() {
        
    String input = "abc\n3\n";
    Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
    ConsoleView consoleView = new ConsoleView(scanner);

    int choice = consoleView.showMainMenu();

    assertEquals(3, choice);
    }
    @Test
    void shouldRetryTaskIdOnInvalidInput() {
        
    String input = "xyz\n42\n";
    Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
    ConsoleView consoleView = new ConsoleView(scanner);

    Long id = consoleView.readTaskId();

    assertEquals(42L, id); 
    }
    @Test
void shouldRejectPipeCharactersInStringInputs() throws Exception {
    
    String input = 
            "Java|Project\n" + 
            "Java Project\n" + 
            "Programming\n" +
            "Project\n" +
            "2026-07-10\n" +
            "10\n";

    Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
    ConsoleView consoleView = new ConsoleView(scanner);

    Task task = consoleView.readTask();

    assertEquals("Java Project", task.getTaskTitle()); // Tinanggap ang malinis na input pagkatapos tanggihan ang may pipe

}
    @Test
void shouldRejectNegativeEstimatedHours() throws Exception {
    String input = 
            "Java Project\n" +
            "Programming\n" +
            "Project\n" +
            "2026-07-10\n" +
            "-5\n" + 
            "5\n"; 

    Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
    ConsoleView consoleView = new ConsoleView(scanner);

    Task task = consoleView.readTask();

    assertEquals(5, task.getEstimatedHours());
}
@Test
void shouldRetryMenuSelectionOnOutOfRangeInput() {
    
    String input = "100\n5\n";
    Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
    ConsoleView consoleView = new ConsoleView(scanner);

    int choice = consoleView.showMainMenu();

    assertEquals(5, choice);
}
}
    
    