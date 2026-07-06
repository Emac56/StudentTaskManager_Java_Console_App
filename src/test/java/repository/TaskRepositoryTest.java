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

            new Task(1L,"Java","Prog","Project","2026",5,"Pending"),

            new Task(2L,"Math","Math","Homework","2026",2,"Pending")

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
    
}