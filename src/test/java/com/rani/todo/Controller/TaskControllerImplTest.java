package com.rani.todo.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rani.todo.Entity.Task;
import com.rani.todo.Service.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskControllerImpl.class)
class TaskControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskServiceImpl taskService;

    @Autowired
    private ObjectMapper objectMapper;

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(1L);
        task.setDescription("Test Task");
        task.setCompleted(false);
    }

    @Test
    void getAllTasks_ShouldReturnTaskList() throws Exception {
        when(taskService.getAllTasks()).thenReturn(Arrays.asList(task));

        mockMvc.perform(get("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(task.getId()))
                .andExpect(jsonPath("$[0].description").value(task.getDescription()));
    }

    @Test
    void getTaskById_ShouldReturnTask_WhenTaskExists() throws Exception {
        when(taskService.getTaskById(1L)).thenReturn(Optional.of(task));

        mockMvc.perform(get("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(task.getId()))
                .andExpect(jsonPath("$.description").value(task.getDescription()));
    }

    @Test
    void getTaskById_ShouldReturnNotFound_WhenTaskDoesNotExist() throws Exception {
        when(taskService.getTaskById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTask_ShouldReturnCreatedTask() throws Exception {
        when(taskService.createTask(any(Task.class))).thenReturn(task);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(task.getId()))
                .andExpect(jsonPath("$.description").value(task.getDescription()));
    }

    @Test
    void updateTaskCompletion_ShouldReturnUpdatedTask_WhenTaskExists() throws Exception {
        task.setCompleted(true);
        when(taskService.updateTask(eq(1L), eq(true))).thenReturn(task);

        mockMvc.perform(patch("/api/tasks/completed/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(true)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed").value(true));
    }

/*    @Test
    void updateDescription_ShouldReturnUpdatedTask_WhenTaskExists() throws Exception {
        task.setDescription("Updated Description");
        when(taskService.updateDescription(eq(1L), eq("Updated Description"))).thenReturn(task);

        mockMvc.perform(put("/api/tasks/desc/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString("Updated Description")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Updated Description"));
    }*/

    @Test
    void deleteTask_ShouldReturnNoContent_WhenTaskExists() throws Exception {
        mockMvc.perform(delete("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
