package egorov.restfulAPI.service;

import egorov.restfulAPI.exceptions.ObjectAlreadyExists;
import egorov.restfulAPI.exceptions.ObjectNotFound;
import egorov.restfulAPI.model.User;
import egorov.restfulAPI.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    /**
     * Добавление нового пользователя с уникальным email
     *
     * @param inputUser сущность добавляемого пользователя
     * @return сущность созданного пользователя
     * @throws ObjectAlreadyExists - добавляемый пользователь имеет неуникальный email
     */
    @Override
    @Transactional
    public User addUser(User inputUser) {
        User createdUser;
        try {
            createdUser = userRepository.save(inputUser);
        } catch (RuntimeException e) {
            throw new ObjectAlreadyExists("unable to create user: user already exists");
        }
        log.info("user {} has been created", createdUser);
        return createdUser;
    }

    /**
     * Изменение параметров пользователя
     *
     * @param inputUser сущность с новыми параметрами пользователя
     * @param userId    идентификатор изменяемого пользователя
     * @return сущность измененного пользователя
     * @throws ObjectAlreadyExists - добавляемый пользователь имеет неуникальный email
     * @throws ObjectNotFound      - пользователь не найден
     */
    @Override
    @Transactional
    public User patchUser(User inputUser, Long userId) {
        User userToUpdate = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFound("unable to update user: user not found")
        );
        updateUserParams(userToUpdate, inputUser);
        try {
            userToUpdate = userRepository.save(userToUpdate);
        } catch (RuntimeException e) {
            throw new ObjectAlreadyExists("unable to update user: user already exists");
        }
        log.info("user {} has been updated", userToUpdate);
        return userToUpdate;
    }

    /**
     * Получение сущности пользователя по идентификатору
     *
     * @param userId идентификатор пользователя
     * @return сущность пользователя
     * @throws ObjectNotFound - пользователь не найден
     */
    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFound("unable to update user: user not found")
        );
    }

    /**
     * Получение списка (страницы) сущностей всех пользователей
     *
     * @param page номер страницы
     * @param size размер страницы
     * @return список (страница) сущностей пользователей
     */
    @Override
    public List<User> getAllUsers(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        List<User> userList = userRepository.findAll(pageable).stream().toList();
        log.info("page of users has been returned {}", userList);
        return userList;
    }

    private void updateUserParams(User userToUpdate, User updatingParams) {
        if (updatingParams.getName() != null) {
            userToUpdate.setName(updatingParams.getName());
        }
        if (updatingParams.getEmail() != null) {
            userToUpdate.setEmail(updatingParams.getEmail());
        }
    }

}
