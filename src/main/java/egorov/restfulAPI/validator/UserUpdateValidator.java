package egorov.restfulAPI.validator;


import egorov.restfulAPI.dto.UserDto;
import egorov.restfulAPI.exceptions.ValidationException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.validator.routines.EmailValidator;

public class UserUpdateValidator implements ConstraintValidator<UserUpdate, UserDto> {

    @Override
    public boolean isValid(UserDto user, ConstraintValidatorContext constraintValidatorContext) {

        if (user.getName() != null && user.getName().isBlank()) {
            throw new ValidationException("user name cannot be blank");
        }
        if (user.getEmail() != null && !EmailValidator.getInstance().isValid(user.getEmail())) {
            throw new ValidationException("invalid email");
        }
        return true;
    }
}


