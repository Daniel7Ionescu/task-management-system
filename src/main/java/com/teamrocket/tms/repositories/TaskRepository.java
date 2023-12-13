package com.teamrocket.tms.repositories;

import com.teamrocket.tms.models.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Task findByTitle(String title);

    List<Task> findByUserId(Long userId);
}
