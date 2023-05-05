package com.scaler.taskmanager.tasks.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class TaskResponseDTO {
    Integer id;
    String name;
    Date dueDate;
    Boolean completed;
}
