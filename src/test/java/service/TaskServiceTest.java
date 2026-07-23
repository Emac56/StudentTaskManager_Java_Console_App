package service;

import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.TaskRepository;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService(taskRepository);
    }

    @Test
    void shouldAddTaskSuccessfully() throws IOException {
        Task task = new Task(
                null,
                "Java Project",
                "Programming",
                "Project",
                "2026-07-10",
                10,
                null
        );

        when(taskRepository.loadTask()).thenReturn(List.of());

        String result = taskService.addTask(task);

        assertEquals("Task added successfully.", result.stripLeading());
        assertEquals(1L, task.getTaskId());
        assertEquals("Pending", task.getStatus());

        verify(taskRepository).saveTask(task);
    }

    @Test
    void shouldNotAddDuplicateTask() throws IOException {
        Task task = new Task(
                null,
                "Java Project",
                "Programming",
                "Project",
                "2026-07-10",
                10,
                null
        );

        when(taskRepository.loadTask()).thenReturn(List.of(task));

        String result = taskService.addTask(task);

        assertEquals("Task already exist.", result.stripLeading());

        verify(taskRepository, never()).saveTask(any(Task.class));
    }

    @Test
    void shouldSearchTask() throws IOException {
        Task task = new Task(
                1L,
                "Java Project",
                "Programming",
                "Project",
                "2026-07-10",
                10,
                "Pending"
        );

        when(taskRepository.findTaskById(1L)).thenReturn(task);

        Task result = taskService.searchTask(1L);

        assertNotNull(result);
        assertEquals(task, result);
    }

    @Test
    void shouldUpdateTaskSuccessfully() throws IOException {
        Task task = new Task(
                1L,
                "Updated Task",
                "Programming",
                "Project",
                "2026-07-20",
                8,
                "Pending"
        );

        when(taskRepository.findTaskById(1L)).thenReturn(task);

        String result = taskService.updateTask(task);

        assertEquals("Task updated successfully.", result.stripLeading());

        verify(taskRepository).updateTask(task);
    }

    @Test
    void shouldReturnTaskNotFoundWhenUpdating() throws IOException {
        Task task = new Task(
                1L,
                "Updated Task",
                "Programming",
                "Project",
                "2026-07-20",
                8,
                "Pending"
        );

        when(taskRepository.findTaskById(1L)).thenReturn(null);

        String result = taskService.updateTask(task);

        assertEquals("Task not found.", result.stripLeading());

        verify(taskRepository, never()).updateTask(any(Task.class));
    }

    @Test
    void shouldDeleteTaskSuccessfully() throws IOException {
        Task task = new Task(
                1L,
                "Java",
                "Programming",
                "Project",
                "2026",
                5,
                "Pending"
        );

        when(taskRepository.findTaskById(1L)).thenReturn(task);

        String result = taskService.deleteTask(1L);

        assertEquals("Task deleted Successfully.", result.stripLeading());

        verify(taskRepository).deleteTask(1L);
    }

    @Test
    void shouldReturnTaskNotFoundWhenDeleting() throws IOException {
        when(taskRepository.findTaskById(1L)).thenReturn(null);

        String result = taskService.deleteTask(1L);

        assertEquals("Task not found.", result.stripLeading());

        verify(taskRepository, never()).deleteTask(anyLong());
    }

    @Test
    void shouldMarkTaskCompletedSuccessfully() throws IOException {
        Task task = new Task(
                1L,
                "Java",
                "Programming",
                "Project",
                "2026",
                5,
                "Pending"
        );

        when(taskRepository.findTaskById(1L)).thenReturn(task);

        String result = taskService.markTaskCompleted(1L);

        assertEquals("Task mark as completed.", result.stripLeading());
        assertEquals("Completed", task.getStatus());

        verify(taskRepository).updateTask(task);
    }

    @Test
    void shouldReturnTaskNotFoundWhenMarkingCompleted() throws IOException {
        when(taskRepository.findTaskById(1L)).thenReturn(null);

        String result = taskService.markTaskCompleted(1L);

        assertEquals("Task not found", result.stripLeading());

        verify(taskRepository, never()).updateTask(any(Task.class));
    }

    @Test
    void shouldReturnPendingTasks() throws IOException {
        List<Task> tasks = List.of(
                new Task(1L, "Java", "Programming", "Project", "2026", 5, "Pending"),
                new Task(2L, "Math", "Math", "Homework", "2026", 2, "Completed"),
                new Task(3L, "English", "English", "Essay", "2026", 3, "Pending")
        );

        when(taskRepository.loadTask()).thenReturn(tasks);

        List<Task> result = taskService.viewPendingTask();

        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnCompletedTasks() throws IOException {
        List<Task> tasks = List.of(
                new Task(1L, "Java", "Programming", "Project", "2026", 5, "Pending"),
                new Task(2L, "Math", "Math", "Homework", "2026", 2, "Completed"),
                new Task(3L, "English", "English", "Essay", "2026", 3, "Completed")
        );

        when(taskRepository.loadTask()).thenReturn(tasks);

        List<Task> result = taskService.viewCompletedTask();

        assertEquals(2, result.size());
    }

    @Test
    void shouldGenerateNextTaskId() throws IOException {
        List<Task> tasks = List.of(
                new Task(1L, "A", "A", "A", "2026", 1, "Pending"),
                new Task(5L, "B", "B", "B", "2026", 1, "Pending"),
                new Task(3L, "C", "C", "C", "2026", 1, "Pending")
        );

        when(taskRepository.loadTask()).thenReturn(tasks);

        Long result = taskService.generateTaskId();

        assertEquals(6L, result);
    }

    @Test
    void shouldGenerateOneWhenNoTaskExists() throws IOException {
        when(taskRepository.loadTask()).thenReturn(List.of());

        Long result = taskService.generateTaskId();

        assertEquals(1L, result);
    }

    @Test
    void shouldReturnAllTasks() throws IOException {
        List<Task> tasks = List.of(
                new Task(1L, "Java", "Programming", "Project", "2026", 5, "Pending")
        );

        when(taskRepository.loadTask()).thenReturn(tasks);

        List<Task> result = taskService.getAllTasks();

        assertEquals(tasks, result);
    }
}

