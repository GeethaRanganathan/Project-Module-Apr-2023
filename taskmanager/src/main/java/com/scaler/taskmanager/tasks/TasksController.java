package com.scaler.taskmanager.tasks;

import com.scaler.taskmanager.tasks.dtos.CreateTaskDTO;
import com.scaler.taskmanager.tasks.dtos.TaskResponseDTO;
import com.scaler.taskmanager.tasks.dtos.UpdateTaskDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
public class TasksController {
    private final TasksService tasksService;
    @Autowired
    private final ModelMapper modelMapper;

    public TasksController(TasksService tasksService, ModelMapper modelMapper) {
        this.tasksService = tasksService;
        this.modelMapper = modelMapper;
    }

    // TODO 03: create a TaskReponseDTO and do not return Task entity directly
    private TaskResponseDTO convertToDTO(Task task){
        TaskResponseDTO taskResponseDTO = modelMapper.map(task,TaskResponseDTO.class);
        return taskResponseDTO;
    }

    @GetMapping("")
    List<TaskResponseDTO> getAllTasks(
            @RequestParam(value = "beforeDate", required = false) Date beforeDate,
            @RequestParam(value = "afterDate", required = false) Date afterDate,
            @RequestParam(value = "completed", required = false) Boolean completed,
            @RequestParam(value = "sort", required = false) String sortOrder) {
        var taskFilter = TasksService
                .TaskFilter.fromQueryParams(beforeDate, afterDate, completed, sortOrder);
        var tasks = tasksService.getAllTasks(taskFilter);
        return tasks.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    TaskResponseDTO getTaskById(@PathVariable("id") Integer id) {
        var task = tasksService.getTaskById(id);
        return convertToDTO(task);
    }

    @PostMapping("")
    TaskResponseDTO createTask(@RequestParam(value = "beforeDate", required = false) Date beforeDate,@RequestBody CreateTaskDTO createTaskDTO) {
        var taskFilter = TasksService.TaskFilter.fromQueryParams(beforeDate, null, null, null);

        var task = tasksService.createTask(createTaskDTO.getName(), createTaskDTO.getDueDate(), taskFilter);
        return convertToDTO(task);
    }

//      TODO 01: implement PATCH task
    @PatchMapping("/{id}")
    TaskResponseDTO updateTask(@RequestParam(value = "beforeDate", required = false) Date beforeDate,
                               @PathVariable("id") Integer id,
                               @RequestBody UpdateTaskDTO updateTaskDTO) {
        var taskFilter = TasksService.TaskFilter.fromQueryParams(beforeDate, null, null, null);
        var task = tasksService.updateTask(id, updateTaskDTO.getDueDate(), updateTaskDTO.getCompleted(), taskFilter);
        return convertToDTO(task);
    }

//      TODO 02: implement DELETE task
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteTask(@PathVariable("id") Integer id) {
        tasksService.deleteTask(id);
        return new ResponseEntity<Void>(HttpStatus.valueOf(200));
    }
    @DeleteMapping("")
    ResponseEntity<Void> deleteTaskByCompleted(@RequestParam(value = "completed", required = false) Boolean completed) {
        var taskFilter = TasksService.TaskFilter.fromQueryParams(null, null, completed, null);
        tasksService.deleteTaskByFilter(taskFilter);
        return new ResponseEntity<Void>(HttpStatus.valueOf(200));
    }

    // TODO 07: also handle IllegalArgumentException (due date, name etc)
    // TODO 08: in error responses send the error message in a JSON object
    /*
    Figure out how to handle 2 or 3 types of exceptions in the same @ExceptionHandler
     */

}
