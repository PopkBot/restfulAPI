package egorov.restfulAPI.facade;

import egorov.restfulAPI.dto.TaskDto;
import egorov.restfulAPI.dto.TaskQueryParams;

import java.util.List;

public interface TaskFacade {

    TaskDto createTask(TaskDto taskDto, Long userId);

    TaskDto patchTask(TaskDto taskDto, Long taskId, Long userId);

    TaskDto changeTaskStatus(String status, Long taskId, Long userId);

    List<TaskDto> getTasksOfUser(Long userId, TaskQueryParams queryParams);

    TaskDto deleteTask(Long userId, Long taskId);

    TaskDto getTaskById(Long userId, Long taskId);
}
