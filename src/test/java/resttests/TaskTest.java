package resttests;

import egorov.restfulAPI.FormatConstants;
import egorov.restfulAPI.Status;
import egorov.restfulAPI.dto.TaskDto;
import egorov.restfulAPI.dto.UserDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TaskTest {

    private final static Random RANDOM = new Random();
    private final static int INT_RANGE = Integer.MAX_VALUE;
    private final static int PORT = 8090;
    private final static List<UserDto> userDtoList = new ArrayList<>();

    @BeforeAll
    public static void initializeUsers() {
        UserDto userDto1 = UserDto.builder()
                .name("name " + RANDOM.nextInt(INT_RANGE))
                .email("user" + RANDOM.nextInt(INT_RANGE) + "@email.com")
                .build();

        UserDto userDto2 = UserDto.builder()
                .name("name " + RANDOM.nextInt(INT_RANGE))
                .email("user" + RANDOM.nextInt(INT_RANGE) + "@email.com")
                .build();

        UserDto userDto3 = UserDto.builder()
                .name("name " + RANDOM.nextInt(INT_RANGE))
                .email("user" + RANDOM.nextInt(INT_RANGE) + "@email.com")
                .build();

        UserDto requestUserDto1 = RestAssured
                .given()
                .log().all()
                .when()
                .body(userDto3)
                .contentType(ContentType.JSON)
                .body(userDto1)
                .post("http://localhost:" + PORT + "/users")
                .then()
                .statusCode(200)
                .extract().as(UserDto.class);

        UserDto requestUserDto2 = RestAssured
                .given()
                .log().all()
                .when()
                .body(userDto3)
                .contentType(ContentType.JSON)
                .body(userDto2)
                .post("http://localhost:" + PORT + "/users")
                .then()
                .statusCode(200)
                .extract().as(UserDto.class);

        UserDto requestUserDto3 = RestAssured
                .given()
                .log().all()
                .when()
                .body(userDto3)
                .contentType(ContentType.JSON)
                .post("http://localhost:" + PORT + "/users")
                .then()
                .statusCode(200)
                .extract().as(UserDto.class);

        userDtoList.add(requestUserDto1);
        userDtoList.add(requestUserDto2);
        userDtoList.add(requestUserDto3);

        Config.initConfig(Config.requestSpecification("http://localhost", PORT, "tasks"),
                Config.responseSpecification());
    }

    @Test
    public void postTask() {
        TaskDto taskDto = TaskDto.builder()
                .name("task name")
                .description("task description")
                .startDate(LocalDateTime.now().plusDays(1))
                .endDate(LocalDateTime.now().plusDays(10))
                .build();

        TaskDto requestTaskDto = RestAssured
                .given()
                .when()
                .body(taskDto)
                .header("user-id", userDtoList.get(0).getId())
                .post()
                .then()
                .statusCode(200)
                .extract().as(TaskDto.class);

        TaskDto responseTaskDto = RestAssured
                .given()
                .when()
                .header("user-id", userDtoList.get(0).getId())
                .get(requestTaskDto.getId().toString())
                .then()
                .statusCode(200)
                .extract().as(TaskDto.class);

        Assertions.assertEquals(requestTaskDto.getId(), responseTaskDto.getId());
    }

    @Test
    public void patchTask() {

        TaskDto taskDto0 = TaskDto.builder()
                .name("task name0")
                .description("task description0")
                .startDate(LocalDateTime.now().plusDays(1))
                .endDate(LocalDateTime.now().plusDays(10))
                .build();

        TaskDto taskDto1 = TaskDto.builder()
                .name("task name1")
                .description("task description1")
                .startDate(LocalDateTime.now().plusDays(1))
                .endDate(LocalDateTime.now().plusDays(20))
                .build();

        TaskDto requestTaskDto = RestAssured
                .given()
                .when()
                .body(taskDto0)
                .header("user-id", userDtoList.get(0).getId())
                .post()
                .then()
                .statusCode(200)
                .extract().as(TaskDto.class);

        TaskDto pathResponseTaskDto = RestAssured
                .given()
                .when()
                .body(taskDto1)
                .header("user-id", userDtoList.get(0).getId())
                .patch(requestTaskDto.getId().toString())
                .then()
                .statusCode(200)
                .extract().as(TaskDto.class);
        taskDto1.setId(requestTaskDto.getId());
        taskDto1.setStartDate(LocalDateTime.parse(taskDto1.getStartDate().format(FormatConstants.DATE_TIME_FORMATTER),
                FormatConstants.DATE_TIME_FORMATTER));
        taskDto1.setEndDate(LocalDateTime.parse(taskDto1.getEndDate().format(FormatConstants.DATE_TIME_FORMATTER),
                FormatConstants.DATE_TIME_FORMATTER));
        taskDto1.setStatus(pathResponseTaskDto.getStatus());

        Assertions.assertEquals(taskDto1, pathResponseTaskDto);


    }

    @Test
    public void changeTaskStatus() {

        TaskDto taskDto0 = TaskDto.builder()
                .name("task name0")
                .description("task description0")
                .startDate(LocalDateTime.now().plusDays(1))
                .endDate(LocalDateTime.now().plusDays(10))
                .build();

        TaskDto requestTaskDto = RestAssured
                .given()
                .when()
                .body(taskDto0)
                .header("user-id", userDtoList.get(0).getId())
                .post()
                .then()
                .statusCode(200)
                .extract().as(TaskDto.class);

        RestAssured
                .given()
                .when()
                .body(taskDto0)
                .header("user-id", userDtoList.get(0).getId())
                .patch(requestTaskDto.getId().toString() + "/WRONG_STATUS")
                .then()
                .statusCode(400);

        TaskDto changedTaskStatusDto = RestAssured
                .given()
                .when()
                .body(taskDto0)
                .header("user-id", userDtoList.get(0).getId())
                .patch(requestTaskDto.getId().toString() + "/IN_PROGRESS")
                .then()
                .statusCode(200)
                .extract().as(TaskDto.class);

        Assertions.assertEquals(Status.IN_PROGRESS.toString(), changedTaskStatusDto.getStatus());
    }

    @Test
    public void deleteTask() {

        TaskDto taskDto0 = TaskDto.builder()
                .name("task name")
                .description("task description")
                .startDate(LocalDateTime.now().plusDays(1))
                .endDate(LocalDateTime.now().plusDays(10))
                .build();

        TaskDto requestTaskDto = RestAssured
                .given()
                .when()
                .body(taskDto0)
                .header("user-id", userDtoList.get(0).getId())
                .post()
                .then()
                .statusCode(200)
                .extract().as(TaskDto.class);

        RestAssured
                .given()
                .when()
                .header("user-id", userDtoList.get(1).getId())
                .delete(requestTaskDto.getId().toString())
                .then()
                .statusCode(409);

        RestAssured
                .given()
                .when()
                .header("user-id", userDtoList.get(0).getId())
                .delete("-1")
                .then()
                .statusCode(404);

        TaskDto deletedTaskDto = RestAssured
                .given()
                .when()
                .header("user-id", userDtoList.get(0).getId())
                .delete(requestTaskDto.getId().toString())
                .then()
                .statusCode(200)
                .extract().as(TaskDto.class);

        Assertions.assertEquals(requestTaskDto, deletedTaskDto);

        RestAssured
                .given()
                .when()
                .header("user-id", userDtoList.get(0).getId())
                .get(requestTaskDto.getId().toString())
                .then()
                .statusCode(404);


    }

    @Test
    public void getTaskById() {

        TaskDto taskDto0 = TaskDto.builder()
                .name("task name")
                .description("task description")
                .startDate(LocalDateTime.now().plusDays(1))
                .endDate(LocalDateTime.now().plusDays(10))
                .build();

        TaskDto requestTaskDto = RestAssured
                .given()
                .when()
                .body(taskDto0)
                .header("user-id", userDtoList.get(0).getId())
                .post()
                .then()
                .statusCode(200)
                .extract().as(TaskDto.class);

        TaskDto responseTaskDto = RestAssured
                .given()
                .when()
                .header("user-id", userDtoList.get(0).getId())
                .get(requestTaskDto.getId().toString())
                .then()
                .statusCode(200)
                .extract().as(TaskDto.class);

        Assertions.assertEquals(requestTaskDto, responseTaskDto);

    }

    @Test
    public void getTasksOfUser() {

        int tasksOfUser1Size = RestAssured
                .given()
                .when()
                .header("user-id", userDtoList.get(1).getId())
                .param("size", Integer.MAX_VALUE)
                .get()
                .then()
                .statusCode(200)
                .extract().jsonPath().getList(".").size();

        int tasksOfUser2Size = RestAssured
                .given()
                .when()
                .header("user-id", userDtoList.get(2).getId())
                .param("size", Integer.MAX_VALUE)
                .get()
                .then()
                .statusCode(200)
                .extract().jsonPath().getList(".", TaskDto.class).size();

        TaskDto taskDto0 = TaskDto.builder()
                .name("task name")
                .description("task description")
                .startDate(LocalDateTime.now().plusDays(1))
                .endDate(LocalDateTime.now().plusDays(10))
                .build();

        TaskDto taskDto1 = TaskDto.builder()
                .name("task name")
                .description("task description")
                .startDate(LocalDateTime.now().plusDays(1))
                .endDate(LocalDateTime.now().plusDays(10))
                .build();

        TaskDto requestTaskDto0 = RestAssured
                .given()
                .when()
                .body(taskDto0)
                .header("user-id", userDtoList.get(1).getId())
                .post()
                .then()
                .statusCode(200)
                .extract().as(TaskDto.class);

        TaskDto requestTaskDto1 = RestAssured
                .given()
                .when()
                .body(taskDto1)
                .header("user-id", userDtoList.get(1).getId())
                .post()
                .then()
                .statusCode(200)
                .extract().as(TaskDto.class);

        List<TaskDto> taskDtoList1 = RestAssured
                .given()
                .when()
                .header("user-id", userDtoList.get(1).getId())
                .param("size", Integer.MAX_VALUE)
                .get()
                .then()
                .statusCode(200)
                .extract().jsonPath().getList(".", TaskDto.class);

        List<TaskDto> taskDtoList2 = RestAssured
                .given()
                .when()
                .header("user-id", userDtoList.get(2).getId())
                .param("size", Integer.MAX_VALUE)
                .get()
                .then()
                .statusCode(200)
                .extract().jsonPath().getList(".", TaskDto.class);

        Assertions.assertEquals(tasksOfUser1Size + 2, taskDtoList1.size());
        Assertions.assertEquals(tasksOfUser2Size, taskDtoList2.size());
    }

}
