package com.rani.todo.Service;

import com.rani.todo.Entity.Task;
import com.rani.todo.Repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class TaskServiceImplTest {

    public static final String TASK_1 = "task 1";
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task task1;
    private Task task2;
    private Task task3;

    @BeforeEach
    void setUp() {
        task1 = getdummyTask();

        task2 = new Task();
        task2.setId(2L);
        task2.setDescription("Task 2");
        task2.setCompleted(false);

        task3 = Task.builder().id(3L).description("Task 3").completed(true).title("task 3").build();
    }

    private Task getdummyTask() {
        return Task.builder().id(1L).description(TASK_1).completed(false).title(TASK_1).build();
    }

    @Test
    void testGetAllTasks() {
        // Arrange
        when(taskRepository.findAll()).thenReturn(getTasks());

        // Act
        List<Task> tasks = taskService.getAllTasks();

        // Assert
        assertEquals(3, tasks.size());
        assertEquals(3L,tasks.get(2).getId());
        assertNotNull(tasks);
        assertTrue(tasks.get(2).getCompleted());
        verify(taskRepository, times(1)).findAll();
    }

    private List<Task> getTasks() {
        return Arrays.asList(task1, task2, task3);
    }

    @Test
    void testGetTaskById_Found() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task1));

        // Act
        Optional<Task> task = taskService.getTaskById(1L);

        // Assert
        assertTrue(task.isPresent());
        assertEquals(TASK_1, task.get().getDescription());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTaskById_NotFound() {
        // Arrange
        when(taskRepository.findById(3L)).thenReturn(Optional.empty());

        // Act
        Optional<Task> task = taskService.getTaskById(3L);

        // Assert
        assertFalse(task.isPresent());
        verify(taskRepository, times(1)).findById(3L);
    }

    @Test
    void testCreateTask() {
        // Arrange
        when(taskRepository.save(task1)).thenReturn(task1);

        // Act
        Task createdTask = taskService.createTask(task1);

        // Assert
        assertEquals(TASK_1, createdTask.getDescription());
        verify(taskRepository, times(1)).save(task1);
    }

    @Test
    void testUpdateTask_Success() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task1));
        when(taskRepository.save(task1)).thenReturn(task1);

        // Act
        Task updatedTask = taskService.updateTask(1L, true);

        // Assert
        assertTrue(updatedTask.getCompleted());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(task1);
    }

    @Test
    void testUpdateTask_NotFound() {
        // Arrange
        when(taskRepository.findById(3L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> taskService.updateTask(3L, true));
        assertEquals("Task not found with id 3", exception.getMessage());
        verify(taskRepository, times(1)).findById(3L);
    }

    @Test
    void testUpdateDescription_Success() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task1));
        when(taskRepository.save(task1)).thenReturn(task1);

        // Act
        Task updatedTask = taskService.updateDescription(1L, "Updated description");

        // Assert
        assertEquals("Updated description", updatedTask.getDescription());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(task1);
    }

    @Test
    void testUpdateDescription_NotFound() {
        // Arrange
        when(taskRepository.findById(3L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> taskService.updateDescription(3L, "Updated description"));
        assertEquals("Task not found with id 3", exception.getMessage());
        verify(taskRepository, times(1)).findById(3L);
    }

    @Test
    void testDeleteTaskById() {
        // Act
        taskService.deleteTaskById(1L);

        // Assert
        verify(taskRepository, times(1)).deleteById(1L);
    }
}
