package egorov.restfulAPI.facade;

import egorov.restfulAPI.dto.UserDto;

import java.util.List;

public interface UserFacade {

    UserDto addUser(UserDto userDto);

    UserDto patchUser(UserDto userDto, Long userId);

    UserDto getUserById(Long userId);

    List<UserDto> getAllUsers(Integer page, Integer size);
}
