package com.teamrocket.tms.repositories;

import com.teamrocket.tms.models.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Task findByTitle(String title);
}
