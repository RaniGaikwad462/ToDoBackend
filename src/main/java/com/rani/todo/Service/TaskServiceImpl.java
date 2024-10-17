package com.rani.todo.Service;

import com.rani.todo.Entity.Task;
import com.rani.todo.Exceptions.TaskNotFoundException;
import com.rani.todo.Repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TaskServiceImpl implements TaskService{

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public Task createTask(Task task) {
        return taskRepository.save(task);

    }

    @Override
    public Task updateTask(Long id, Boolean completed) {
        return taskRepository.findById(id)
                .map(task -> {
                    task.setCompleted(completed);
                    return taskRepository.save(task);
                })
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id " + id));
    }

    @Override
    public Task updateDescription(Long id, String desc) {
        return taskRepository.findById(id)
                .map(task -> {
                    task.setDescription(desc);
                    return taskRepository.save(task);
                })
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id " + id));
    }

    @Override
    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public List<Task> findByIdAndTitle(Long id, String title){
        return taskRepository.findByIdAndTitle(id, title);
    }
}
