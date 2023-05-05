package com.scaler.taskmanager.tasks.exceptions;

public class TaskNotFoundException extends IllegalStateException {
    public TaskNotFoundException(Integer id) {
        super("Task with id " + id + " not found");
    }
}