package com.scaler.taskmanager.tasks.exceptions;

import java.util.Date;

public class DateBeforeDueDateException extends IllegalStateException {
    public DateBeforeDueDateException(Date dueDate) {
        super("Task created with Invalid before date " + dueDate);
    }
}