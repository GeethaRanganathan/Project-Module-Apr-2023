package com.scaler.taskmanager;

import com.scaler.taskmanager.tasks.dtos.Error;
import com.scaler.taskmanager.tasks.exceptions.DateBeforeDueDateException;
import com.scaler.taskmanager.tasks.exceptions.NameInvalidException;
import com.scaler.taskmanager.tasks.exceptions.TaskNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    /*
    This will handle RuntimeException and IllegalArgumentException
    generated from **ALL** controllers
     */
    @ExceptionHandler(
            {
                    RuntimeException.class,
                    IllegalArgumentException.class
            }
    )
    String multiExceptionHandler(Exception e) {
        if (e instanceof IllegalArgumentException) {
            return "Illegal argument";
        } else if (e instanceof RuntimeException) {
            return "Something went wrong";
        } else {
            return "idk - something wrong happened";
        }
    }

    @ExceptionHandler(value = {DateBeforeDueDateException.class, NameInvalidException.class,
            TaskNotFoundException.class})
    public ResponseEntity<Object> handlerDueDate(IllegalStateException e, WebRequest request){
        var error = new Error();
        error.setErrorId("Invalid");
        error.setErrorDescription(e.getMessage());
        return handleExceptionInternal(e, error, new HttpHeaders(), HttpStatus.NOT_FOUND,request);
    }
}
