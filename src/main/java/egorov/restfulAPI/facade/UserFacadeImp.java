package egorov.restfulAPI.facade;

import egorov.restfulAPI.dto.UserDto;
import egorov.restfulAPI.mapper.UserMapper;
import egorov.restfulAPI.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor

public class UserFacadeImp implements UserFacade {


    private final UserService userService;
    private final UserMapper userMapper;

    public UserDto addUser(UserDto userDto) {
        return userMapper.convertToDto(userService.addUser(userMapper.convertToModel(userDto)));
    }

    public UserDto patchUser(UserDto userDto, Long userId) {
        return userMapper.convertToDto(userService.patchUser(userMapper.convertToModel(userDto), userId));
    }

    public UserDto getUserById(Long userId) {
        return userMapper.convertToDto(userService.getUserById(userId));
    }

    public List<UserDto> getAllUsers(Integer page, Integer size) {
        return userService.getAllUsers(page, size).stream().map(userMapper::convertToDto).collect(Collectors.toList());
    }

}
