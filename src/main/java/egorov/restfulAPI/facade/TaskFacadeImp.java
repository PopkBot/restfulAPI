package egorov.restfulAPI.facade;

import egorov.restfulAPI.dto.TaskDto;
import egorov.restfulAPI.dto.TaskQueryParams;
import egorov.restfulAPI.mapper.TaskMapper;
import egorov.restfulAPI.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TaskFacadeImp implements TaskFacade {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    public TaskDto createTask(TaskDto taskDto, Long userId) {
        return taskMapper.convertToDto(taskService.createTask(taskMapper.convertToModel(taskDto), userId));
    }

    public TaskDto patchTask(TaskDto taskDto, Long taskId, Long userId) {
        return taskMapper.convertToDto(taskService.patchTask(taskMapper.convertToModel(taskDto), taskId, userId));
    }

    public TaskDto changeTaskStatus(String status, Long taskId, Long userId) {
        return taskMapper.convertToDto(taskService.changeTaskStatus(status, taskId, userId));
    }

    public List<TaskDto> getTasksOfUser(Long userId, TaskQueryParams queryParams) {
        return taskService.getTasksOfUser(userId, queryParams).stream().map(taskMapper::convertToDto)
                .collect(Collectors.toList());
    }

    public TaskDto deleteTask(Long userId, Long taskId) {
        return taskMapper.convertToDto(taskService.deleteTask(userId, taskId));
    }

    public TaskDto getTaskById(Long userId, Long taskId) {
        return taskMapper.convertToDto(taskService.getTaskById(userId, taskId));
    }
}
