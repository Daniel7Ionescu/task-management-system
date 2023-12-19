package com.teamrocket.tms.repositories;

import com.teamrocket.tms.models.entities.Task;
import com.teamrocket.tms.utils.enums.Priority;

import java.time.LocalDate;
import java.util.List;

public interface CustomTaskRepository {

    List<Task> findFilteredTasks(Long userId, LocalDate dueDate, Priority priority);
}
