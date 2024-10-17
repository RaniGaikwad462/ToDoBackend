package com.rani.todo.Repository;

import com.rani.todo.Entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByIdGreaterThan(Long id);

    List<Task> findByIdAndTitle(Long id, String title);
}