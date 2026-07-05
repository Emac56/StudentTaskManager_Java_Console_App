package service;

import model.Task;
import repository.TaskRepository;

import java.io.IOException;
import java.util.List;

public class TaskService {
    private TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
}