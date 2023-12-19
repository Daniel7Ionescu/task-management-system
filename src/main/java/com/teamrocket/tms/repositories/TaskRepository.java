package com.teamrocket.tms.repositories;

import com.teamrocket.tms.models.entities.Project;
import com.teamrocket.tms.models.entities.Task;
import com.teamrocket.tms.utils.enums.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, CustomTaskRepository {

    Task findByTitle(String title);

    List<Task> findByUserId(Long userId);

    List<Task> findByDueDate(LocalDate dueDate);

    List<Task> findByPriority(Priority priority);

    List<Task> findByProject(Project project);
}
