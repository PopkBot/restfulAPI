package egorov.restfulAPI.validator;


import egorov.restfulAPI.dto.TaskDto;
import egorov.restfulAPI.exceptions.ValidationException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TaskCreateValidator implements ConstraintValidator<TaskCreate, TaskDto> {

    @Override
    public boolean isValid(TaskDto task, ConstraintValidatorContext constraintValidatorContext) {

        if (task.getName() == null || task.getName().isBlank()) {
            throw new ValidationException("task name cannot be blank");
        }
        if (task.getDescription() == null || task.getDescription().isBlank()) {
            throw new ValidationException("task description cannot be blank");
        }
        if (task.getStartDate() == null || task.getEndDate() == null) {
            throw new ValidationException("task must have date of start and date of end");
        }
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
        if (task.getStartDate().atZone(ZoneId.systemDefault()).isBefore(now)) {
            throw new ValidationException("date of start cannot be in the past");
        }
        if (task.getEndDate().isBefore(task.getStartDate())) {
            throw new ValidationException("date of end cannot be before start date");
        }
        return true;
    }
}


