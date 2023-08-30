package egorov.restfulAPI.validator;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = TaskUpdateValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface TaskUpdate {
    String message() default "{Task is invalid for updating}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}


