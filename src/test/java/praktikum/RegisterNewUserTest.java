package praktikum;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class RegisterNewUserTest {

    User user = new User("ulanovda@gmail.com", "password", "densky");

    @Test
    public void registerNewUser() {
        ValidatableResponse response = UserClient.registerUser(user);
        response.assertThat().body("success", equalTo(true)).and().statusCode(200);

        UserClient.deleteUser(user);
    }

    @Test
    public void registerSameUser() {
        UserClient.registerUser(user);

        ValidatableResponse response = UserClient.registerUser(user);
        response.assertThat().body("message", equalTo("User already exists")).and().body("success", equalTo(false)).and().statusCode(403);

        UserClient.deleteUser(user);
    }

    @Test
    public void registerNewUserWithoutEmail() {
        ValidatableResponse response = UserClient.registerUser(new User().setPassword("password").setName("densky"));
        response.assertThat().body("message", equalTo("Email, password and name are required fields")).and().body("success", equalTo(false)).and().statusCode(403);
    }

    @Test
    public void registerNewUserWithoutPassword() {
        ValidatableResponse response = UserClient.registerUser(new User().setEmail("ulanovda@gmail.com").setName("densky"));
        response.assertThat().body("message", equalTo("Email, password and name are required fields")).and().body("success", equalTo(false)).and().statusCode(403);
    }

    @Test
    public void registerNewUserWithoutName() {
        ValidatableResponse response = UserClient.registerUser(new User().setEmail("ulanovda@gmail.com").setPassword("password"));
        response.assertThat().body("message", equalTo("Email, password and name are required fields")).and().body("success", equalTo(false)).and().statusCode(403);
    }

    @Test
    public void registerNewUserWithoutAllRequiredFields() {
        ValidatableResponse response = UserClient.registerUser(new User());
        response.assertThat().body("message", equalTo("Email, password and name are required fields")).and().body("success", equalTo(false)).and().statusCode(403);
    }
}