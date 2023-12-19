package com.teamrocket.tms.models.dtos;

import com.teamrocket.tms.utils.enums.Priority;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskFilterDTO {

    private Long id;

    private Integer objectives;

    private LocalDate dueDate;

    private Priority priority;
}
