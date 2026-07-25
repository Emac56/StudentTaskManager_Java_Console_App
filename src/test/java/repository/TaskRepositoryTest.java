package repository;

import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Files;
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

    // --- loadTask() resilience to malformed data (see repository/README.md) ---

    @Test
    void shouldSkipBlankLinesWhenLoadingTasks() throws Exception {
        String content =
                "1|Java|Prog|Project|2026|5|Pending\n"
                        + "\n"                                   // blank line in the middle
                        + "2|Math|Math|Homework|2026|2|Pending\n";

        Files.writeString(databaseFile.toPath(), content);

        List<Task> tasks = repository.loadTask();

        assertEquals(2, tasks.size());
    }

    @Test
    void shouldSkipLineWithFewerThanSevenFields() throws Exception {
        String content =
                "1|Java|Prog|Project|2026|5|Pending\n"
                        + "2|Math|Homework\n"                     // only 3 fields, not 7
                        + "3|Science|Sci|Homework|2026|1|Pending\n";

        Files.writeString(databaseFile.toPath(), content);

        List<Task> tasks = repository.loadTask();

        assertEquals(2, tasks.size());
        assertEquals("Java", tasks.get(0).getTaskTitle());
        assertEquals("Science", tasks.get(1).getTaskTitle());
    }

    @Test
    void shouldSkipLineWithMoreThanSevenFields() throws Exception {
        String content =
                "1|Java|Prog|Project|2026|5|Pending\n"
                        + "2|Math|Homework|Assignment|2026|2|Pending|Extra\n" // 8 fields, extra pipe
                        + "3|Science|Sci|Homework|2026|1|Pending\n";

        Files.writeString(databaseFile.toPath(), content);

        List<Task> tasks = repository.loadTask();

        assertEquals(2, tasks.size());
        assertEquals("Java", tasks.get(0).getTaskTitle());
        assertEquals("Science", tasks.get(1).getTaskTitle());
    }

    @Test
    void shouldPreserveEmptyTrailingFieldInsteadOfDroppingIt() throws Exception {
        // Status field left empty on purpose — this used to shorten the
        // split() array from 7 to 6 elements and throw
        // ArrayIndexOutOfBoundsException before the fix.
        String content = "1|Java|Prog|Project|2026|5|\n";

        Files.writeString(databaseFile.toPath(), content);

        List<Task> tasks = repository.loadTask();

        assertEquals(1, tasks.size());
        assertEquals("", tasks.get(0).getStatus());
    }

    @Test
    void shouldSkipLineWithInvalidNumberFormat() throws Exception {
        String content =
                "1|Java|Prog|Project|2026|5|Pending\n"
                        + "abc|Math|Math|Homework|2026|five|Pending\n" // bad taskId + bad hours
                        + "3|Science|Sci|Homework|2026|1|Pending\n";

        Files.writeString(databaseFile.toPath(), content);

        List<Task> tasks = repository.loadTask();

        assertEquals(2, tasks.size());
    }

    @Test
    void shouldNotThrowWhenFileContainsOnlyMalformedLines() throws Exception {
        String content =
                "\n"
                        + "not|enough|fields\n"
                        + "abc|Java|Prog|Project|2026|5|Pending\n";

        Files.writeString(databaseFile.toPath(), content);

        List<Task> tasks = assertDoesNotThrow(() -> repository.loadTask());

        assertTrue(tasks.isEmpty());
    }
}


