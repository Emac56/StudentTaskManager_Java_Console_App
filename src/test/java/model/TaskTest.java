package model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskTest {

    @Test
    void shouldCreateTaskSuccessfully() {

        Task task = new Task(
                1L,
                "Java Mvc Project",
                "Programming",
                "Project",
                "2026-07-15",
                5,
                "Pending"
        );

        Long taskId = task.getTaskId();
        String taskTitle = task.getTaskTitle();
        String subject = task.getSubject();
        String taskType = task.getTaskType();
        String dueDate = task.getDueDate();
        int estimatedHours = task.getEstimatedHours();
        String status = task.getStatus();

        assertEquals(1L,taskId);
        assertEquals("Java Mvc Project",taskTitle);
        assertEquals("Programming",subject);
        assertEquals("Project",taskType);
        assertEquals("2026-07-15",dueDate);
        assertEquals(5,estimatedHours);
        assertEquals("Pending",status);
    }

}
