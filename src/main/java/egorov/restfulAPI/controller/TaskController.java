package egorov.restfulAPI.controller;

import egorov.restfulAPI.dto.TaskDto;
import egorov.restfulAPI.dto.TaskQueryParams;
import egorov.restfulAPI.facade.TaskFacade;
import egorov.restfulAPI.validator.TaskCreate;
import egorov.restfulAPI.validator.TaskUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@Validated
@RequiredArgsConstructor
public class TaskController {

    private final TaskFacade taskFacade;

    @PostMapping
    public TaskDto addTask(@TaskCreate @RequestBody TaskDto taskDto,
                           @RequestHeader("user-id") Long userId) {
        return taskFacade.createTask(taskDto, userId);
    }

    @PatchMapping("/{taskId}")
    public TaskDto patchTask(@TaskUpdate @RequestBody TaskDto taskDto,
                             @PathVariable Long taskId,
                             @RequestHeader("user-id") Long userId) {
        return taskFacade.patchTask(taskDto, taskId, userId);
    }

    @PatchMapping("/{taskId}/{status}")
    public TaskDto changeTaskStatus(@PathVariable Long taskId,
                                    @PathVariable String status,
                                    @RequestHeader("user-id") Long userId) {
        return taskFacade.changeTaskStatus(status, taskId, userId);
    }

    @GetMapping("/{taskId}")
    public TaskDto getTaskById(@PathVariable Long taskId,
                               @RequestHeader("user-id") Long userId) {
        return taskFacade.getTaskById(userId, taskId);
    }

    @GetMapping
    public List<TaskDto> getTasksOfUser(@RequestHeader("user-id") Long userId,
                                        @RequestParam(required = false) String query,
                                        @RequestParam(required = false) String status,
                                        @RequestParam(required = false) String startDate,
                                        @RequestParam(required = false) String endDate,
                                        @RequestParam(defaultValue = "0") Integer page,
                                        @RequestParam(defaultValue = "10") Integer size) {
        TaskQueryParams taskQueryParams = TaskQueryParams.builder()
                .queryString(query)
                .status(status)
                .startDate(startDate)
                .endDate(endDate)
                .page(page)
                .size(size)
                .build();
        return taskFacade.getTasksOfUser(userId, taskQueryParams);
    }

    @DeleteMapping("/{taskId}")
    public TaskDto deleteTask(@PathVariable Long taskId,
                              @RequestHeader("user-id") Long userId) {
        return taskFacade.deleteTask(userId, taskId);
    }
}
