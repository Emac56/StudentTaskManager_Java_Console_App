package repository;

import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskRepositoryTest {

    @TempDir
    Path tempDir;

    private TaskRepository repository;
    private File databaseFile;

    @BeforeEach
    void setUp() throws Exception {

        databaseFile = tempDir.resolve("database/tasks.txt").toFile();

        repository = new TaskRepository(databaseFile);
    }

    @Test
    void shouldCreateDatabaseFile() {
        assertTrue(databaseFile.exists());
    }

    @Test
    void shouldSaveTask() throws Exception {

        Task task = new Task(
                1L,
                "Java",
                "Programming",
                "Project",
                "2026",
                5,
                "Pending"
        );

        repository.saveTask(task);

        List<Task> list = repository.loadTask();

        assertEquals(1, list.size());
    }

    @Test
    void shouldLoadSavedTask() throws Exception {

        Task task = new Task(
                1L,
                "Java",
                "Programming",
                "Project",
                "2026",
                5,
                "Pending"
        );

        repository.saveTask(task);

        List<Task> list = repository.loadTask();

        assertEquals("Java", list.get(0).getTaskTitle());
    }

    @Test
    void shouldSaveAllTasks() throws Exception {

        List<Task> tasks = List.of(

                new Task(1L, "Java", "Prog", "Project", "2026", 5, "Pending"),

                new Task(2L, "Math", "Math", "Homework", "2026", 2, "Pending")

        );

        repository.saveAllTask(tasks);

        assertEquals(2, repository.loadTask().size());
    }

    @Test
    void shouldUpdateTask() throws Exception {

        Task task = new Task(
                1L,
                "Old",
                "Prog",
                "Project",
                "2026",
                5,
                "Pending"
        );

        repository.saveTask(task);

        Task updated = new Task(
                1L,
                "New",
                "Prog",
                "Project",
                "2026",
                5,
                "Completed"
        );

        repository.updateTask(updated);

        Task result = repository.findTaskById(1L);

        assertEquals("New", result.getTaskTitle());
    }

    @Test
    void shouldDeleteTask() throws Exception {

        repository.saveTask(

                new Task(
                        1L,
                        "Java",
                        "Prog",
                        "Project",
                        "2026",
                        5,
                        "Pending"
                )

        );

        repository.deleteTask(1L);

        assertTrue(repository.loadTask().isEmpty());
    }

    @Test
    void shouldFindTaskById() throws Exception {

        repository.saveTask(

                new Task(
                        1L,
                        "Java",
                        "Prog",
                        "Project",
                        "2026",
                        5,
                        "Pending"
                )

        );

        Task task = repository.findTaskById(1L);

        assertNotNull(task);
    }

    @Test
    void shouldReturnNullWhenTaskIdDoesNotExist() throws Exception {

        Task task = repository.findTaskById(10L);

        assertNull(task);
    }

    // --- Defensive pipe-rejection tests (repository invariant) ---

    @Test
    void shouldRejectSaveTaskWithPipeInTaskTitle() {
        Task task = new Task(
                1L,
                "Java|Project",   // pipe in title
                "Programming",
                "Project",
                "2026",
                5,
                "Pending"
        );

        assertThrows(IllegalStateException.class, () -> repository.saveTask(task));
    }

    @Test
    void shouldRejectSaveTaskWithPipeInSubject() {
        Task task = new Task(
                1L,
                "Java Project",
                "Pro|gramming",   // pipe in subject
                "Project",
                "2026",
                5,
                "Pending"
        );

        assertThrows(IllegalStateException.class, () -> repository.saveTask(task));
    }

    @Test
    void shouldRejectSaveTaskWithPipeInTaskType() {
        Task task = new Task(
                1L,
                "Java Project",
                "Programming",
                "Pro|ject",       // pipe in taskType
                "2026",
                5,
                "Pending"
        );

        assertThrows(IllegalStateException.class, () -> repository.saveTask(task));
    }

    @Test
    void shouldRejectSaveTaskWithPipeInDueDate() {
        Task task = new Task(
                1L,
                "Java Project",
                "Programming",
                "Project",
                "2026|07",        // pipe in dueDate
                5,
                "Pending"
        );

        assertThrows(IllegalStateException.class, () -> repository.saveTask(task));
    }

    @Test
    void shouldRejectSaveTaskWithPipeInStatus() {
        Task task = new Task(
                1L,
                "Java Project",
                "Programming",
                "Project",
                "2026",
                5,
                "Pend|ing"        // pipe in status
        );

        assertThrows(IllegalStateException.class, () -> repository.saveTask(task));
    }

    @Test
    void shouldNotWriteFileWhenSaveTaskRejectsPipe() throws Exception {
        Task task = new Task(
                1L,
                "Java|Project",   // pipe — must be rejected before file is touched
                "Programming",
                "Project",
                "2026",
                5,
                "Pending"
        );

        assertThrows(IllegalStateException.class, () -> repository.saveTask(task));

        // File must remain empty; no partial record written
        assertTrue(repository.loadTask().isEmpty());
    }

    @Test
    void shouldRejectSaveAllTaskWithPipeInAnyField() {
        List<Task> tasks = List.of(
                new Task(1L, "Java",       "Prog", "Project",  "2026", 5, "Pending"),
                new Task(2L, "Math|Extra", "Math", "Homework", "2026", 2, "Pending")  // pipe in second task
        );

        assertThrows(IllegalStateException.class, () -> repository.saveAllTask(tasks));
    }

    @Test
    void shouldNotTruncateFileWhenSaveAllTaskRejectsPipe() throws Exception {
        // Pre-populate with a clean task so the file is not empty
        repository.saveTask(
                new Task(1L, "Java", "Prog", "Project", "2026", 5, "Pending")
        );

        List<Task> corruptBatch = List.of(
                new Task(1L, "Java",       "Prog", "Project",  "2026", 5, "Pending"),
                new Task(2L, "Math|Extra", "Math", "Homework", "2026", 2, "Pending")
        );

        // Validation runs before FileWriter truncates, so the original record survives
        assertThrows(IllegalStateException.class, () -> repository.saveAllTask(corruptBatch));

        List<Task> survivors = repository.loadTask();
        assertEquals(1, survivors.size());
        assertEquals("Java", survivors.get(0).getTaskTitle());
    }
}
