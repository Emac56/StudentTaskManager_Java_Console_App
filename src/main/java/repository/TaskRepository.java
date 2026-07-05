package repository;

import model.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TaskRepository {

    private File databaseFile;

    public TaskRepository(File databaseFile) {
        this.databaseFile = databaseFile;
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

    public void saveTask(Task task) throws IOException {
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
            writer.close();
        }
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
            }
        } saveAllTask(taskList);
    }

    public Task findTaskById(Long taskId) throws IOException {

        List<Task> taskList = loadTask();

        for (Task task : taskList) {
            if (task.getTaskId().equals(taskId)) {
                return task;
            }
        } return null;
    }

    public Task findTaskByTitle(String title) throws IOException {

        List<Task> taskList = loadTask();

        for (Task task : taskList) {

            if (task.getTaskTitle().equalsIgnoreCase(title)) {
                return task;
            }
        } return null;
    }
}

