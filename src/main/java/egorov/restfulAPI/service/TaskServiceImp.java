package egorov.restfulAPI.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import egorov.restfulAPI.FormatConstants;
import egorov.restfulAPI.Status;
import egorov.restfulAPI.dto.TaskQueryParams;
import egorov.restfulAPI.exceptions.ConflictException;
import egorov.restfulAPI.exceptions.ObjectNotFound;
import egorov.restfulAPI.exceptions.ValidationException;
import egorov.restfulAPI.model.QTask;
import egorov.restfulAPI.model.Task;
import egorov.restfulAPI.model.User;
import egorov.restfulAPI.repository.TaskRepository;
import egorov.restfulAPI.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskServiceImp implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;


    /**
     * Создать задачу для пользователя, изначально задача имеет статус ожидания
     *
     * @param inputTask сущность создаваемой задачи
     * @param userId    автор задачи
     * @return сущность созданной задачи
     * @throws ObjectNotFound - не найден пользователь
     */
    @Override
    @Transactional
    public Task createTask(Task inputTask, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFound("unable to create task: user not found")
        );
        inputTask.setUser(user);
        inputTask.setStatus(Status.WAITING);
        Task task = taskRepository.save(inputTask);
        log.info("task {} has been created", task);
        return task;
    }

    /**
     * Обновление данных о задаче
     *
     * @param inputTask сущность задачи с новыми параметрами
     * @param taskId    идентификатор изменяемой задачи
     * @param userId    автор задачи
     * @return сущность измененной задачи
     * @throws ObjectNotFound    - пользователь не найден, не найдена задача
     * @throws ConflictException - пользователь не является автором задачи, задача более не активна
     */
    @Override
    @Transactional
    public Task patchTask(Task inputTask, Long taskId, Long userId) {
        userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFound("unable to update task: user not found")
        );
        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new ObjectNotFound("unable to update task: task not found")
        );
        if (!task.getUser().getId().equals(userId)) {
            throw new ConflictException("unable to update task: user has no rights for this task");
        }
        if (task.getStatus().equals(Status.COMPLETED) || task.getStatus().equals(Status.FAILED)) {
            throw new ConflictException("unable to update task: task is no longer active");
        }
        updateTaskParams(task, inputTask);
        task = taskRepository.save(task);
        log.info("task {} has been patched", task);
        return task;
    }

    /**
     * Изменение статуса задачи
     *
     * @param status новый статус задачи
     * @param taskId идентификатор задачи
     * @param userId идентификатор пользователя
     * @return сущность задачи с новым статусом
     * @throws ValidationException - неправильный статус
     * @throws ObjectNotFound      - пользователь не найден, задача не найдена
     * @throws ConflictException   - пользователь не является автором задачи, задача более не активна
     */
    @Override
    @Transactional
    public Task changeTaskStatus(String status, Long taskId, Long userId) {
        if (!Status.isContains(status)) {
            throw new ValidationException("invalid status");
        }
        userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFound("unable to change status: user not found")
        );
        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new ObjectNotFound("unable to change status: task not found")
        );
        if (!task.getUser().getId().equals(userId)) {
            throw new ConflictException("unable to change status: user has no rights for this task");
        }
        if (task.getStatus().equals(Status.COMPLETED) || task.getStatus().equals(Status.FAILED)) {
            throw new ConflictException("unable to change status: task is no longer active");
        }
        task.setStatus(Status.valueOf(status));
        task = taskRepository.save(task);
        log.info("task status has been changed {}", task);
        return task;
    }

    /**
     * Возвращает список задач пользователя по фильтрам
     *
     * @param userId      идентификатор автора задачи
     * @param queryParams параметры фильтрации задач (совпадение текста запроса с именем или описанием задачи,
     *                    статус, временной интервал)
     * @return список задач пользователя, отобранных по фильтрам
     * @throws ObjectNotFound - пользователь не найден
     */
    @Override
    public List<Task> getTasksOfUser(Long userId, TaskQueryParams queryParams) {
        queryParams.validate();
        userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFound("user not found")
        );
        BooleanExpression query = QTask.task.user.eq(userId).and(QTask.task.isNotNull());
        if (queryParams.getQueryString() != null) {
            query = query.and(QTask.task.name.containsIgnoreCase(queryParams.getQueryString()))
                    .or(QTask.task.description.containsIgnoreCase(queryParams.getQueryString()));
        }
        if (queryParams.getStatus() != null) {
            query = query.and(QTask.task.status.eq(Status.valueOf(queryParams.getStatus())));
        }
        if (queryParams.getStartDate() != null && queryParams.getEndDate() != null) {

            ZonedDateTime start = LocalDateTime.parse(queryParams.getStartDate(), FormatConstants.DATE_TIME_FORMATTER)
                    .atZone(ZoneId.systemDefault());
            ZonedDateTime end = LocalDateTime.parse(queryParams.getEndDate(), FormatConstants.DATE_TIME_FORMATTER)
                    .atZone(ZoneId.systemDefault());
            query = query.and(QTask.task.startDate.before(start))
                    .and(QTask.task.endDate.after(end));
        }
        Sort sort = Sort.by(Sort.Direction.ASC, "startDate");
        Pageable pageable = PageRequest.of(queryParams.getPage(), queryParams.getSize(), sort);
        List<Task> taskList = taskRepository.findAll(query, pageable).getContent();
        log.info("list of tasks has been returned {}", taskList);
        return taskList;
    }

    /**
     * Удаление задачи
     *
     * @param userId идентификатор автора задачи
     * @param taskId идентификатор удаляемой задачи
     * @return копию сущности удаленной задачи
     * @throws ObjectNotFound    - пользователь не найден, задача не найдена
     * @throws ConflictException - пользователь не является автором задачи
     */
    @Override
    @Transactional
    public Task deleteTask(Long userId, Long taskId) {
        userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFound("unable to delete task: user not found")
        );
        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new ObjectNotFound("unable to delete task: task not found")
        );
        if (!task.getUser().getId().equals(userId)) {
            throw new ConflictException("unable to delete task: user has no rights for this task");
        }
        taskRepository.delete(task);
        log.info("task has been deleted {}", task);
        return task;
    }

    /**
     * Получение сущности задачи по идентификатору
     *
     * @param userId идентификатор автора задачи
     * @param taskId идентификатор запрашиваемой задачи
     * @return сущность задачи по ее идентификатору
     * @throws ObjectNotFound    - пользователь не найден, задача не найдена
     * @throws ConflictException - пользователь не является автором задачи
     */
    @Override
    public Task getTaskById(Long userId, Long taskId) {
        userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFound("unable to get task: user not found")
        );
        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new ObjectNotFound("unable to get task: task not found")
        );
        if (!task.getUser().getId().equals(userId)) {
            throw new ConflictException("unable to get task: user has no rights for this task");
        }
        return task;
    }

    private void updateTaskParams(Task taskToUpdate, Task updateParams) {
        if (updateParams.getName() != null) {
            taskToUpdate.setName(updateParams.getName());
        }
        if (updateParams.getDescription() != null) {
            taskToUpdate.setDescription(updateParams.getDescription());
        }
        if (updateParams.getStartDate() != null && updateParams.getStartDate().isBefore(taskToUpdate.getEndDate())) {
            taskToUpdate.setStartDate(updateParams.getStartDate());
        }
        if (updateParams.getEndDate() != null && updateParams.getEndDate().isAfter(taskToUpdate.getStartDate())) {
            taskToUpdate.setEndDate(updateParams.getEndDate());
        }
    }
}
