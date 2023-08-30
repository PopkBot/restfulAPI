package egorov.restfulAPI.controller;

import egorov.restfulAPI.dto.UserDto;
import egorov.restfulAPI.facade.UserFacade;
import egorov.restfulAPI.validator.UserCreate;
import egorov.restfulAPI.validator.UserUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Validated
@RequiredArgsConstructor
public class UserController {

    private final UserFacade userFacade;

    @PostMapping
    public UserDto addUser(@UserCreate @RequestBody UserDto userDto) {
        return userFacade.addUser(userDto);
    }

    @PatchMapping
    public UserDto patchUser(@UserUpdate @RequestBody UserDto userDto,
                             @RequestHeader("user-id") Long userId) {
        return userFacade.patchUser(userDto, userId);
    }

    @GetMapping
    public UserDto getUserById(@RequestHeader("user-id") Long userId) {
        return userFacade.getUserById(userId);
    }

    @GetMapping("/all")
    public List<UserDto> getAllUsers(@RequestParam(defaultValue = "0") Integer page,
                                     @RequestParam(defaultValue = "10") Integer size) {
        return userFacade.getAllUsers(page, size);
    }
}
