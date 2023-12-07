package com.teamrocket.tms.repositories;

import com.teamrocket.tms.models.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Task findByTitle(String title);
}
