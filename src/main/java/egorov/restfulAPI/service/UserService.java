package egorov.restfulAPI.service;

import egorov.restfulAPI.model.User;

import java.util.List;

public interface UserService {

    User addUser(User inputUser);

    User patchUser(User inputUser, Long userId);

    User getUserById(Long userId);

    List<User> getAllUsers(Integer page, Integer size);

}
