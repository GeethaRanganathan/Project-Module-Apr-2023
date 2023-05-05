package com.scaler.taskmanager.tasks.exceptions;

public class NameInvalidException  extends IllegalStateException {
    public NameInvalidException(String name) {
        super("Task created with Invalid name " + name);
    }
}
