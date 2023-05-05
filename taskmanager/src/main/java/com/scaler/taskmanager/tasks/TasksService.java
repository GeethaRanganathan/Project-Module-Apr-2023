package com.scaler.taskmanager.tasks;

import com.scaler.taskmanager.tasks.exceptions.DateBeforeDueDateException;
import com.scaler.taskmanager.tasks.exceptions.NameInvalidException;
import com.scaler.taskmanager.tasks.exceptions.TaskNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TasksService {
    private List<Task> tasks = new ArrayList<>();
    private Integer id = 0;

    public List<Task> getAllTasks(TaskFilter taskFilter) {
        if (taskFilter == null) {
            return tasks;
        } else {
            var filteredTasks = tasks.stream().filter(task -> {
                if (taskFilter.beforeDate != null && task.getDueDate().after(taskFilter.beforeDate)) {
                    return false;
                }
                if (taskFilter.afterDate != null && task.getDueDate().before(taskFilter.afterDate)) {
                    return false;
                }
                if (taskFilter.completed != null && task.getCompleted() != taskFilter.completed) {
                    return false;
                }

                return true;
            });
            if (taskFilter.sortOrder != null){
                filteredTasks = filteredTasks.sorted(taskFilter.sortOrder.equalsIgnoreCase("dateAsc")
                        ? Comparator.comparing(Task::getDueDate)
                        : Comparator.comparing(Task::getDueDate).reversed());
            }
            return filteredTasks.toList();
        }

    }

    public Task getTaskById(Integer id) {
        for (Task task : tasks) {
            if (task.getId().equals(id)) {
                return task;
            }
        }
        throw new TaskNotFoundException(id);
    }

    // TODO 04: generate error for invalid dueDate (before today)
    // TODO 05: generate error for invalid name (less than 5 char, or more than 100 char)
    public Task createTask(String name, Date dueDate, TaskFilter taskFilter) {
        if (taskFilter != null && taskFilter.beforeDate != null && dueDate.before(taskFilter.beforeDate)) {
            throw new DateBeforeDueDateException(dueDate);
        }
        if (name == null || name.length()<5 || name.length()>100){
            throw new NameInvalidException(name);
        }
        Task task = new Task(id++, name, dueDate, false);
        tasks.add(task);
        return task;
    }

    // TODO 06: generate error for invalid dueDate (before today)
    public Task updateTask(Integer id, Date dueDate, Boolean completed, TaskFilter taskFilter) {
        if (taskFilter != null && taskFilter.beforeDate != null && dueDate.before(taskFilter.beforeDate)) {
            throw new DateBeforeDueDateException(dueDate);
        }
        Task task = getTaskById(id);
        if (dueDate != null) {
            task.setDueDate(dueDate);
        }
        if (completed != null) {
            task.setCompleted(completed);
        }
        return task;
    }

    public void deleteTask(Integer id) {
        Task task = getTaskById(id);
        tasks.remove(task);
    }

    public void deleteTaskByFilter(TaskFilter taskFilter) {
        for (Task task: tasks) {
            if (taskFilter.completed != null && task.getCompleted() == taskFilter.completed) {
                tasks.remove(task);
            }
        }
    }

    public static class TaskFilter {
        Date beforeDate;
        Date afterDate;
        Boolean completed;
        String sortOrder;

        static TaskFilter fromQueryParams(Date beforeDate, Date afterDate, Boolean completed, String sortOrder) {
            if (beforeDate == null && afterDate == null && completed == null && sortOrder == null) {
                return null;
            }
            TaskFilter taskFilter = new TaskFilter();
            taskFilter.beforeDate = beforeDate;
            taskFilter.afterDate = afterDate;
            taskFilter.completed = completed;
            taskFilter.sortOrder = sortOrder;
            return taskFilter;
        }
    }
}
