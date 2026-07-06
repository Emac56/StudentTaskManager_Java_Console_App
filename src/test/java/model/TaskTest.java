package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void shouldCreateTaskUsingConstructor() {

        Task task = new Task(
                1L,
                "Java Project",
                "Programming",
                "Project",
                "2026-07-10",
                10,
                "Pending"
        );

        assertEquals(1L, task.getTaskId());
        assertEquals("Java Project", task.getTaskTitle());
        assertEquals("Programming", task.getSubject());
        assertEquals("Project", task.getTaskType());
        assertEquals("2026-07-10", task.getDueDate());
        assertEquals(10, task.getEstimatedHours());
        assertEquals("Pending", task.getStatus());
    }

    @Test
    void shouldUpdateTaskUsingSetters() {

        Task task = new Task(
                null,
                "",
                "",
                "",
                "",
                0,
                null
        );

        task.setTaskId(2L);
        task.setTaskTitle("Math Assignment");
        task.setSubject("Math");
        task.setTaskType("Homework");
        task.setDueDate("2026-07-08");
        task.setEstimatedHours(3);
        task.setStatus("Completed");

        assertEquals(2L, task.getTaskId());
        assertEquals("Math Assignment", task.getTaskTitle());
        assertEquals("Math", task.getSubject());
        assertEquals("Homework", task.getTaskType());
        assertEquals("2026-07-08", task.getDueDate());
        assertEquals(3, task.getEstimatedHours());
        assertEquals("Completed", task.getStatus());
    }

    @Test
    void shouldReturnTrueWhenTasksAreEqual() {

        Task task1 = new Task(
                1L,
                "Java Project",
                "Programming",
                "Project",
                "2026-07-10",
                10,
                "Pending"
        );

        Task task2 = new Task(
                2L,
                "Java Project",
                "Programming",
                "Project",
                "2026-07-10",
                10,
                "Completed"
        );

        assertTrue(task1.equals(task2));
    }

    @Test
    void shouldReturnFalseWhenTasksAreDifferent() {

        Task task1 = new Task(
                1L,
                "Java Project",
                "Programming",
                "Project",
                "2026-07-10",
                10,
                "Pending"
        );

        Task task2 = new Task(
                1L,
                "Math Assignment",
                "Math",
                "Homework",
                "2026-07-08",
                2,
                "Pending"
        );

        assertFalse(task1.equals(task2));
    }

    @Test
    void shouldReturnFalseWhenComparedWithNull() {

        Task task = new Task(
                1L,
                "Java Project",
                "Programming",
                "Project",
                "2026-07-10",
                10,
                "Pending"
        );

        assertFalse(task.equals(null));
    }

    @Test
    void shouldReturnTrueWhenComparedWithItself() {

        Task task = new Task(
                1L,
                "Java Project",
                "Programming",
                "Project",
                "2026-07-10",
                10,
                "Pending"
        );

        assertTrue(task.equals(task));
    }
}