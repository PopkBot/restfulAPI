package egorov.restfulAPI.service;

import egorov.restfulAPI.dto.TaskQueryParams;
import egorov.restfulAPI.model.Task;

import java.util.List;

public interface TaskService {

    Task createTask(Task inputTask, Long userId);

    Task patchTask(Task inputTask, Long taskId, Long userId);

    Task changeTaskStatus(String status, Long taskId, Long userId);

    List<Task> getTasksOfUser(Long userId, TaskQueryParams queryParams);

    Task deleteTask(Long userId, Long taskId);

    Task getTaskById(Long userId, Long taskId);
}
