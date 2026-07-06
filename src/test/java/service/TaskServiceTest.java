package service;

import model.Task;
import org.junit.jupiter.api.*;
import repository.TaskRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {

    private File testFile;
    private TaskRepository repository;
    private TaskService service;

    @BeforeEach
    void setUp() throws IOException {
        testFile = File.createTempFile("task-service-test", ".txt");
        repository = new TaskRepository(testFile);
        service = new TaskService(repository);
    }

    @AfterEach
    void tearDown() {
        testFile.delete();
    }

    @Test
    void shouldAddTask() throws IOException {

        Task task = new Task(
                null,
                "Java Assignment",
                "Programming",
                "Homework",
                "2026-07-10",
                3,
                "Pending"
        );

        String message = service.addTask(task);

        assertEquals("Task added successfully.", message);

        List<Task> tasks = service.viewAllTask();

        assertEquals(1, tasks.size());
        assertEquals("Java Assignment", tasks.get(0).getTaskTitle());
    }

    @Test
    void shouldSearchTask() throws IOException {

        Task task = new Task(
                null,
                "Math Quiz",
                "Math",
                "Quiz",
                "2026-07-11",
                2,
                "Pending"
        );

        service.addTask(task);

        Task result = service.searchTask(1L);

        assertNotNull(result);
        assertEquals("Math Quiz", result.getTaskTitle());
    }

    @Test
    void shouldUpdateTask() throws IOException {

        Task task = new Task(
                null,
                "Old Title",
                "Programming",
                "Assignment",
                "2026-07-10",
                2,
                "Pending"
        );

        service.addTask(task);

        Task updated = new Task(
                1L,
                "New Title",
                "Programming",
                "Assignment",
                "2026-07-15",
                4,
                "Completed"
        );

        String message = service.updateTask(updated);

        assertEquals("Task updated successfully.", message);

        Task result = service.searchTask(1L);

        assertEquals("New Title", result.getTaskTitle());
        assertEquals("Completed", result.getStatus());
    }

    @Test
    void shouldDeleteTask() throws IOException {

        Task task = new Task(
                null,
                "Delete Me",
                "Programming",
                "Assignment",
                "2026-07-10",
                2,
                "Pending"
        );

        service.addTask(task);

        String message = service.deleteTask(1L);

        assertEquals("Task deleted Successfully.", message);
        assertNull(service.searchTask(1L));
    }

    @Test
    void shouldMarkTaskCompleted() throws IOException {

        Task task = new Task(
                null,
                "Finish Project",
                "Capstone",
                "Project",
                "2026-07-20",
                8,
                "Pending"
        );

        service.addTask(task);

        String message = service.markTaskComplete(1L);

        assertEquals("Task mark as completed.", message);

        Task result = service.searchTask(1L);

        assertEquals("Completed", result.getStatus());
    }

    @Test
    void shouldViewPendingTasks() throws IOException {

        service.addTask(new Task(
                null,
                "Pending Task",
                "Programming",
                "Homework",
                "2026-07-10",
                2,
                "Pending"
        ));

        service.addTask(new Task(
                null,
                "Completed Task",
                "Programming",
                "Homework",
                "2026-07-10",
                2,
                "Completed"
        ));

        List<Task> pending = service.viewPendingTask();

        assertEquals(1, pending.size());
        assertEquals("Pending Task", pending.get(0).getTaskTitle());
    }

    @Test
    void shouldViewCompletedTasks() throws IOException {

        service.addTask(new Task(
                null,
                "Pending Task",
                "Programming",
                "Homework",
                "2026-07-10",
                2,
                "Pending"
        ));

        service.addTask(new Task(
                null,
                "Completed Task",
                "Programming",
                "Homework",
                "2026-07-10",
                2,
                "Completed"
        ));

        List<Task> completed = service.viewCompletedTask();

        assertEquals(1, completed.size());
        assertEquals("Completed Task", completed.get(0).getTaskTitle());
    }

    @Test
    void shouldGenerateNextTaskId() throws IOException {

        service.addTask(new Task(
                null,
                "Task 1",
                "Programming",
                "Homework",
                "2026-07-10",
                2,
                "Pending"
        ));

        service.addTask(new Task(
                null,
                "Task 2",
                "Programming",
                "Homework",
                "2026-07-10",
                2,
                "Pending"
        ));

        Long nextId = service.generateTaskId();

        assertEquals(3L, nextId);
    }

    @Test
    void shouldReturnNullWhenTaskNotFound() throws IOException {

        Task task = service.searchTask(999L);

        assertNull(task);
    }

    @Test
    void shouldNotDeleteNonExistingTask() throws IOException {

        String message = service.deleteTask(999L);

        assertEquals("Task not found.", message);
    }
    @Test
    void shouldNotUpdateNonExistingTask() throws IOException {

        Task task = new Task(
                999L,
                "Unknown",
                "Programming",
                "Homework",
                "2026-07-10",
                2,
                "Pending"
        );

        String message = service.updateTask(task);

        assertEquals("Task not found.", message);
    }
    @Test
    void shouldNotMarkCompletedWhenTaskNotFound() throws IOException {

        String message = service.markTaskComplete(999L);

        assertEquals("Task not found", message);
    }

    @Test
    void shouldReturnEmptyTaskListWhenDatabaseIsEmpty() throws IOException {

        List<Task> tasks = service.viewAllTask();

        assertTrue(tasks.isEmpty());
    }
    @Test
    void shouldReturnEmptyPendingTaskList() throws IOException {

        List<Task> pending = service.viewPendingTask();

        assertTrue(pending.isEmpty());
    }
    @Test
    void shouldReturnEmptyCompletedTaskList() throws IOException {

        List<Task> completed = service.viewCompletedTask();

        assertTrue(completed.isEmpty());
    }
    @Test
    void shouldGenerateTaskIdOneWhenDatabaseIsEmpty() throws IOException {

        Long id = service.generateTaskId();

        assertEquals(1L, id);
    }
}