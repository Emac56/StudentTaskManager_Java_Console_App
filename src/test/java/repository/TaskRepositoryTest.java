package repository;

import model.Task;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskRepositoryTest {

    private File testFile;
    private TaskRepository repository;

    @BeforeEach
    void setUp() throws IOException {
        testFile = File.createTempFile("task-test", ".txt");
        repository = new TaskRepository(testFile);
    }

    @AfterEach
    void tearDown() {
        testFile.delete();
    }

    @Test
    void shouldSaveAndLoadTask() throws IOException {

        Task task = new Task(
                1L,
                "Java Assignment",
                "Programming",
                "Homework",
                "2026-07-10",
                3,
                "Pending"
        );

        repository.saveTask(task);

        List<Task> tasks = repository.loadTask();

        assertEquals(1, tasks.size());

        Task savedTask = tasks.get(0);

        assertEquals(task.getTaskId(), savedTask.getTaskId());
        assertEquals(task.getTaskTitle(), savedTask.getTaskTitle());
        assertEquals(task.getSubject(), savedTask.getSubject());
        assertEquals(task.getTaskType(), savedTask.getTaskType());
        assertEquals(task.getDueDate(), savedTask.getDueDate());
        assertEquals(task.getEstimatedHours(), savedTask.getEstimatedHours());
        assertEquals(task.getStatus(), savedTask.getStatus());
    }

    @Test
    void shouldFindTaskById() throws IOException {

        Task task = new Task(
                1L,
                "Math Quiz",
                "Math",
                "Quiz",
                "2026-07-11",
                2,
                "Pending"
        );

        repository.saveTask(task);

        Task result = repository.findTaskById(1L);

        assertNotNull(result);
        assertEquals("Math Quiz", result.getTaskTitle());
    }

    @Test
    void shouldFindTaskByTitle() throws IOException {

        Task task = new Task(
                1L,
                "Science Project",
                "Science",
                "Project",
                "2026-07-12",
                5,
                "Pending"
        );

        repository.saveTask(task);

        Task result = repository.findTaskByTitle("science project");

        assertNotNull(result);
        assertEquals(1L, result.getTaskId());
    }

    @Test
    void shouldUpdateTask() throws IOException {

        Task oldTask = new Task(
                1L,
                "Old Title",
                "Programming",
                "Assignment",
                "2026-07-10",
                2,
                "Pending"
        );

        repository.saveTask(oldTask);

        Task updatedTask = new Task(
                1L,
                "New Title",
                "Programming",
                "Assignment",
                "2026-07-15",
                4,
                "Completed"
        );

        repository.updateTask(updatedTask);

        Task result = repository.findTaskById(1L);

        assertNotNull(result);
        assertEquals("New Title", result.getTaskTitle());
        assertEquals("Completed", result.getStatus());
    }

    @Test
    void shouldDeleteTask() throws IOException {

        Task task = new Task(
                1L,
                "Delete Me",
                "Programming",
                "Assignment",
                "2026-07-10",
                2,
                "Pending"
        );

        repository.saveTask(task);

        repository.deleteTask(1L);

        assertNull(repository.findTaskById(1L));
    }
    @Test
    void shouldReturnNullWhenIdDoesNotExist() throws IOException {

        Task task = repository.findTaskById(100L);

        assertNull(task);
    }
    @Test
    void shouldReturnNullWhenTitleDoesNotExist() throws IOException {

        Task task = repository.findTaskByTitle("Unknown Task");

        assertNull(task);
    }
}