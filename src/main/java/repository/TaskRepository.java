package repository;

import model.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TaskRepository {

    private File databaseFile;

    public TaskRepository(File databaseFile) throws IOException {

        this.databaseFile = databaseFile;

        File parent = databaseFile.getParentFile();

        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        if (!databaseFile.exists()) {
            databaseFile.createNewFile();
        }
    }

    public List<Task> loadTask() throws IOException {
        List<Task> taskList = new ArrayList<>();

        var reader = new BufferedReader(new FileReader(databaseFile));

        String line;

        while ((line = reader.readLine()) != null) {

            String[] data = line.split("\\|");

            Task task = new Task(
                    Long.parseLong(data[0]),
                    data[1],
                    data[2],
                    data[3],
                    data[4],
                    Integer.parseInt(data[5]),
                    data[6]
            );
            taskList.add(task);
        }
        reader.close();
        return taskList;
    }

    /**
     * Defensive persistence invariant: verifies that no string field in the task
     * contains the pipe character '|' before any write reaches the file.
     *
     * This is the repository's own last-line-of-defence check.  The primary
     * enforcement lives in TaskService.validateNoPipe(), but this guard catches
     * any call that bypasses the service layer (tests, future code paths, etc.).
     *
     * Throws IllegalStateException rather than IllegalArgumentException to signal
     * that reaching this point is a programming error — the service layer should
     * have already caught it.
     *
     * @throws IllegalStateException if any string field contains '|'
     */
    private void assertNoPipe(Task task) {
        assertFieldNoPipe("taskTitle", task.getTaskTitle());
        assertFieldNoPipe("subject",   task.getSubject());
        assertFieldNoPipe("taskType",  task.getTaskType());
        assertFieldNoPipe("dueDate",   task.getDueDate());
        assertFieldNoPipe("status",    task.getStatus());
    }

    private void assertFieldNoPipe(String fieldName, String value) {
        if (value != null && value.contains("|")) {
            throw new IllegalStateException(
                "Persistence invariant violated: field '" + fieldName +
                "' contains the pipe delimiter '|'. Offending value: " + value
            );
        }
    }

    public void saveTask(Task task) throws IOException {
        assertNoPipe(task);

        var writer = new BufferedWriter(new FileWriter(databaseFile, true));

        writer.write(
                task.getTaskId() + "|" +
                        task.getTaskTitle() + "|" +
                        task.getSubject() + "|" +
                        task.getTaskType() + "|" +
                        task.getDueDate() + "|" +
                        task.getEstimatedHours() + "|" +
                        task.getStatus()
        );
        writer.newLine();
        writer.close();
    }

    public void saveAllTask(List<Task> taskList) throws IOException {
        // Validate every task BEFORE opening the file for writing.
        // FileWriter(databaseFile) truncates on open, so a pipe found mid-loop
        // would leave the file partially overwritten with no way to recover.
        for (Task task : taskList) {
            assertNoPipe(task);
        }

        var writer = new BufferedWriter(new FileWriter(databaseFile));

        for (Task task : taskList) {
            writer.write(
                    task.getTaskId() + "|" +
                            task.getTaskTitle() + "|" +
                            task.getSubject() + "|" +
                            task.getTaskType() + "|" +
                            task.getDueDate() + "|" +
                            task.getEstimatedHours() + "|" +
                            task.getStatus()
            );
            writer.newLine();
        }
        writer.close();
    }

    public void updateTask(Task updatedTask) throws IOException {

        List<Task> taskList = loadTask();

        for (int i = 0; i < taskList.size(); i++) {

            Task task = taskList.get(i);

            if (task.getTaskId().equals(updatedTask.getTaskId())) {
                taskList.set(i, updatedTask);
                break;
            }
        }
        saveAllTask(taskList);
    }

    public void deleteTask(Long taskId) throws IOException {

        List<Task> taskList = loadTask();

        for (int i = 0; i < taskList.size(); i++) {

            Task task = taskList.get(i);

            if (task.getTaskId().equals(taskId)) {
                taskList.remove(i);
                break;
            }
        }
        saveAllTask(taskList);
    }

    public Task findTaskById(Long taskId) throws IOException {

        List<Task> taskList = loadTask();

        for (Task task : taskList) {
            if (task.getTaskId().equals(taskId)) {
                return task;
            }
        }
        return null;
    }
}
