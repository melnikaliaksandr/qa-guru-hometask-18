package tests;

import request.Request;
import request.TestProvider;
import io.restassured.response.Response;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith(TestProvider.class)
public class ReqresTests extends TestBase {

    @Test
    @DisplayName("Check user with id = 12 via Groovy")
    void checkUserFromList() {
        Response response = Request.get("/api/users?page=2");

        response.then().body("data.findAll{it.id = 12}.email.flatten()",
                        hasItem("rachel.howell@reqres.in"))
                .body("data.findAll{it.id = 12}.first_name.flatten()",
                        hasItem("Rachel"))
                .body("data.findAll{it.id = 12}.last_name.flatten()",
                        hasItem("Howell"))
                .body("data.findAll{it.id = 12}.avatar.flatten()",
                        hasItem("https://reqres.in/img/faces/12-image.jpg"));
    }

    @Test
    @DisplayName("Successful register user")
    void successfulRegisterUser() {
        User body = User.builder()
                .email("eve.holt@reqres.in")
                .password("pistol")
                .build();

        User user = userSteps.register(body);

        assertThat(user.getId(), notNullValue());
        assertThat(user.getToken(), notNullValue());
    }

    @Test
    @DisplayName("Successful login user")
    void successfulLoginUser() {
        User body = User.builder()
                .email("eve.holt@reqres.in")
                .password("cityslicka")
                .build();

        User user = userSteps.login(body);

        assertThat(user.getToken(), notNullValue());
    }

    @Test
    @DisplayName("Successful update user")
    void successfulUpdateUser() {
        User body = User.builder()
                .name("morpheus")
                .job("zion resident")
                .build();

        User user = userSteps.update(body);

        assertThat(user.getName(), equalTo("morpheus"));
        assertThat(user.getJob(), equalTo("zion resident"));
        assertThat(user.getUpdatedAt(), notNullValue());
    }

    @Test
    @DisplayName("Successful create user")
    void successfulCrateUser() {
        User body = User.builder()
                .name("morpheus")
                .job("leader")
                .build();

        User user = userSteps.create(body);

        assertThat(user.getName(), equalTo("morpheus"));
        assertThat(user.getJob(), equalTo("leader"));
        assertThat(user.getId(), notNullValue());
        assertThat(user.getCreatedAt(), notNullValue());
    }

    @Test
    @DisplayName("Successful delete user")
    void successfulDeleteUser() {
        Response response = userSteps.delete();

        assertThat(response.getStatusCode(), equalTo(204));
    }

}
