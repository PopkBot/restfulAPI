package egorov.restfulAPI.validator;


import egorov.restfulAPI.dto.TaskDto;
import egorov.restfulAPI.exceptions.ValidationException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TaskUpdateValidator implements ConstraintValidator<TaskUpdate, TaskDto> {

    @Override
    public boolean isValid(TaskDto task, ConstraintValidatorContext constraintValidatorContext) {

        if (task.getName() != null && task.getName().isBlank()) {
            throw new ValidationException("task name cannot be blank");
        }
        if (task.getDescription() != null && task.getDescription().isBlank()) {
            throw new ValidationException("task description cannot be blank");
        }
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
        if (task.getStartDate() != null && task.getStartDate().atZone(ZoneId.systemDefault()).isBefore(now)) {
            throw new ValidationException("date of start cannot be in the past");
        }
        if (task.getEndDate() != null && task.getEndDate().atZone(ZoneId.systemDefault()).isBefore(now)) {
            throw new ValidationException("date of end cannot be in the past");
        }
        return true;
    }
}


