package resttests;

import egorov.restfulAPI.dto.UserDto;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;


public class UserTest {

    private final static Random RANDOM = new Random();
    private final static int INT_RANGE = Integer.MAX_VALUE;

    static {
        Config.initConfig(Config.requestSpecification("http://localhost", 8090, "users"),
                Config.responseSpecification());
    }

    @Test
    void postUserTest() {


        UserDto userDto = UserDto.builder()
                .name("name " + RANDOM.nextInt(INT_RANGE))
                .email("user" + RANDOM.nextInt(INT_RANGE) + "@email.com")
                .build();

        UserDto requestUserDto = RestAssured
                .given()
                .when()
                .body(userDto)
                .post()
                .then()
                .statusCode(200)
                .extract().as(UserDto.class);

        UserDto responseUserDto = RestAssured
                .given()
                .when()
                .header("user-id", requestUserDto.getId())
                .get()
                .then()
                .statusCode(200)
                .extract().as(UserDto.class);
        Assertions.assertEquals(requestUserDto, responseUserDto);
    }

    @Test
    void postUserSameEmail() {

        int userEmailNumber = RANDOM.nextInt(INT_RANGE);
        UserDto userDto1 = UserDto.builder()
                .name("name " + RANDOM.nextInt(INT_RANGE))
                .email("user" + userEmailNumber + "@email.com")
                .build();

        UserDto userDto2 = UserDto.builder()
                .name("name " + RANDOM.nextInt(INT_RANGE))
                .email("user" + userEmailNumber + "@email.com")
                .build();

        RestAssured
                .given()
                .when()
                .body(userDto1)
                .post()
                .then()
                .statusCode(200);

        RestAssured
                .given()
                .when()
                .body(userDto2)
                .post()
                .then()
                .statusCode(409);

    }

    @Test
    void postUserInvalidParams() {

        UserDto userDto = UserDto.builder()
                .email("user" + RANDOM.nextInt(INT_RANGE) + "@email.com")
                .build();

        RestAssured
                .given()
                .when()
                .body(userDto)
                .post()
                .then()
                .statusCode(400);

        userDto = UserDto.builder()
                .name(" ")
                .email("user" + RANDOM.nextInt(INT_RANGE) + "@email.com")
                .build();

        RestAssured
                .given()
                .when()
                .body(userDto)
                .post()
                .then()
                .statusCode(400);

        userDto = UserDto.builder()
                .name("name" + RANDOM.nextInt(INT_RANGE))
                .email("user" + RANDOM.nextInt(INT_RANGE) + "email.com")
                .build();

        RestAssured
                .given()
                .when()
                .body(userDto)
                .post()
                .then()
                .statusCode(400);

        userDto = UserDto.builder()
                .name("name" + RANDOM.nextInt(INT_RANGE))
                .email(" ")
                .build();

        RestAssured
                .given()
                .when()
                .body(userDto)
                .post()
                .then()
                .statusCode(400);

        userDto = UserDto.builder()
                .name("name" + RANDOM.nextInt(INT_RANGE))
                .build();

        RestAssured
                .given()
                .when()
                .body(userDto)
                .post()
                .then()
                .statusCode(400);
    }

    @Test
    public void getUsers() {

        int initListSize = RestAssured
                .given()
                .when()
                .get("all?size=" + Integer.MAX_VALUE)
                .then()
                .statusCode(200)
                .extract().as(List.class).size();

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
                .when()
                .body(userDto1)
                .post()
                .then()
                .statusCode(200)
                .extract().as(UserDto.class);

        UserDto requestUserDto2 = RestAssured
                .given()
                .when()
                .body(userDto2)
                .post()
                .then()
                .statusCode(200)
                .extract().as(UserDto.class);

        UserDto requestUserDto3 = RestAssured
                .given()
                .when()
                .body(userDto3)
                .post()
                .then()
                .statusCode(200)
                .extract().as(UserDto.class);

        List<UserDto> userDtoList = RestAssured
                .given()
                .when()
                .param("size", Integer.MAX_VALUE)
                .get("all")
                .then()
                .statusCode(200)
                .extract().jsonPath().getList(".", UserDto.class);


        Assertions.assertEquals(userDtoList.get(initListSize).getId(), requestUserDto1.getId());
        Assertions.assertEquals(userDtoList.get(initListSize + 1).getId(), requestUserDto2.getId());
        Assertions.assertEquals(userDtoList.get(initListSize + 2).getId(), requestUserDto3.getId());
    }


    @Test
    public void patchUser() {

        int userEmailNumber = RANDOM.nextInt(INT_RANGE);
        UserDto userDto1 = UserDto.builder()
                .name("name")
                .email("user" + userEmailNumber + "@email.com")
                .build();

        UserDto userDto2 = UserDto.builder()
                .name("new name")
                .build();

        UserDto requestUserDto = RestAssured
                .given()
                .when()
                .body(userDto1)
                .post()
                .then()
                .statusCode(200)
                .extract().as(UserDto.class);

        UserDto patchedUserDto = RestAssured
                .given()
                .when()
                .body(userDto2)
                .header("user-id", requestUserDto.getId())
                .patch()
                .then()
                .statusCode(200)
                .extract().as(UserDto.class);

        userDto2.setId(requestUserDto.getId());
        userDto2.setEmail(userDto1.getEmail());
        Assertions.assertEquals(userDto2, patchedUserDto);
    }


}
