package com.rani.todo.Service;

import com.rani.todo.Entity.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    List<Task> getAllTasks();

    Optional<Task> getTaskById(Long id);

    Task createTask(Task task);

    Task updateTask(Long id, Boolean completed);

    Task updateDescription(Long id, String desc);

    void deleteTaskById(Long id);

    List<Task> findByIdAndTitle(Long id, String title);
}