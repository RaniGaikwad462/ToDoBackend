/**
 * Copyright (c) 2024. Rani. All rights reserved.
 *
 * This software is the confidential and proprietary information of Rani.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with Rani.
 */

package com.rani.todo.Controller;

import com.rani.todo.Entity.Task;
import com.rani.todo.Exceptions.TaskNotFoundException;
import com.rani.todo.Service.TaskServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for managing Task-related operations.
 * This class handles API requests and delegates the business logic to {@link TaskServiceImpl}.
 */
@RestController
@RequestMapping("/api/tasks/")
@Slf4j
@Tag(name = "Task Management", description = "API for managing tasks")
public class TaskControllerImpl implements TaskController {

    @Autowired
    private TaskServiceImpl taskService;

    /**
     * Greets a user.
     *
     * @return ResponseEntity with a greeting message for the user.
     */
    @Operation(summary = "Greet the user", description = "Returns a greeting message for the user")
    @GetMapping("/user")
    public ResponseEntity<String> hiUser() {
        log.info("Greeting user");
        return ResponseEntity.ok("Hello from user");
    }

    /**
     * Greets an admin.
     *
     * @return ResponseEntity with a greeting message for the admin.
     */
    @Operation(summary = "Greet the admin", description = "Returns a greeting message for the admin")
    @GetMapping("/admin")
    public ResponseEntity<String> hiAdmin() {
        log.info("Greeting admin");
        return ResponseEntity.ok("Hello from Admin");
    }

    /**
     * Retrieves all tasks.
     *
     * @return List of all tasks.
     */
    @Operation(summary = "Get all tasks", description = "Fetches all tasks from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all tasks")
    })
    @Override
    @GetMapping
    public List<Task> getAllTasks() {
        log.info("Fetching all tasks");
        return taskService.getAllTasks();
    }

    /**
     * Retrieves a task by its ID.
     *
     * @param id the ID of the task to retrieve. Defaults to 1.
     * @return ResponseEntity with the task details if found, or an error if not.
     */
    @Operation(summary = "Get a task by ID", description = "Fetches a task by its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the task"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(
            @Parameter(description = "ID of the task", example = "1")
            @PathVariable Long id) {
        log.info("Fetching task with ID: {}", id);
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new TaskNotFoundException(id + " not found"));
    }

    /**
     * Creates a new task.
     *
     * @param task the task to create.
     * @return ResponseEntity with the created task.
     */
    @Operation(summary = "Create a new task", description = "Creates a new task in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created the task")
    })
    @Override
    @PostMapping
    public ResponseEntity<Task> createTask(
            @Parameter(description = "Task object to be created", required = true)
            @Valid @RequestBody Task task) {
        log.info("Creating a new task: {}", task);
        return ResponseEntity.ok(taskService.createTask(task));
    }

    /**
     * Updates the completion status of a task.
     *
     * @param id the ID of the task to update. Defaults to 1.
     * @param completed the new completion status. Defaults to true.
     * @return ResponseEntity with the updated task if found, or an error if not.
     */
    @Operation(summary = "Update task completion", description = "Updates the completion status of a task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the task"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @Override
    @PatchMapping("/completed/{id}")
    public ResponseEntity<Task> updateTask(
            @Parameter(description = "ID of the task", example = "1") @PathVariable Long id,
            @Parameter(description = "Completion status of the task", example = "true") @RequestBody Boolean completed) {
        log.info("Updating task completion status for task ID: {}", id);
        Task updatedTask = taskService.updateTask(id, completed);
        return updatedTask != null ? ResponseEntity.ok(updatedTask) : ResponseEntity.notFound().build();
    }

    /**
     * Updates the description of a task.
     *
     * @param id the ID of the task to update. Defaults to 1.
     * @param desc the new description. Defaults to "Updated task description".
     * @return ResponseEntity with the updated task if found, or an error if not.
     */
    @Operation(summary = "Update task description", description = "Updates the description of a task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the task"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @Override
    @PutMapping("/desc/{id}")
    public ResponseEntity<Task> updateDescription(
            @Parameter(description = "ID of the task", example = "1") @PathVariable Long id,
            @Parameter(description = "New description of the task", example = "Updated task description") @RequestBody String desc) {
        log.info("Updating task description for task ID: {}", id);
        Task updatedTask = taskService.updateDescription(id, desc);
        return updatedTask != null ? ResponseEntity.ok(updatedTask) : ResponseEntity.notFound().build();
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id the ID of the task to delete. Defaults to 1.
     * @return ResponseEntity with no content.
     */
    @Operation(summary = "Delete a task", description = "Deletes a task by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the task"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "ID of the task", example = "1") @PathVariable Long id) {
        log.info("Deleting task with ID: {}", id);
        taskService.deleteTaskById(id);
        return ResponseEntity.noContent().build();
    }
}
