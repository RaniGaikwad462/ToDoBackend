package com.rani.todo.Controller;

import com.rani.todo.Entity.Task;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface TaskController {

    List<Task> getAllTasks();

    ResponseEntity<Task> getTaskById(Long id);

    ResponseEntity<Task> createTask(Task task);

    ResponseEntity<Task> updateTask(Long id, Boolean completed);

    ResponseEntity<Task> updateDescription(Long id, String desc);

    ResponseEntity<Void> deleteTask(Long id);
}