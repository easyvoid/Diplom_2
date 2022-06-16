package praktikum;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class RegisterNewUserTest {
    UserGeneration gen = new UserGeneration();
    User user = new User(gen.randomEmail(), gen.randomPassword(), gen.randomName());
    UserClient client = new UserClient();

    @Test
    public void registerNewUserTest() {
        ValidatableResponse response = client.registerUser(user);
        response.assertThat().body("success", equalTo(true)).and().statusCode(200);

    }

    @Test
    public void registerSameUserTest() {
        client.registerUser(user);

        ValidatableResponse response = client.registerUser(user);
        response.assertThat().body("message", equalTo("User already exists")).and().body("success", equalTo(false)).and().statusCode(403);

    }

    @Test
    public void registerNewUserWithoutEmailTest() {
        User userWithoutEmail = new User().setPassword(user.getPassword()).setName(user.getName());
        ValidatableResponse response = client.registerUser(userWithoutEmail);
        response.assertThat().body("message", equalTo("Email, password and name are required fields")).and().body("success", equalTo(false)).and().statusCode(403);
        user = userWithoutEmail;
    }

    @Test
    public void registerNewUserWithoutPasswordTest() {
        User userWithoutPassword = new User().setEmail(user.getEmail()).setName(user.getName());
        ValidatableResponse response = client.registerUser(userWithoutPassword);
        response.assertThat().body("message", equalTo("Email, password and name are required fields")).and().body("success", equalTo(false)).and().statusCode(403);
        user = userWithoutPassword;
    }

    @Test
    public void registerNewUserWithoutNameTest() {
        User userWithoutName = new User().setEmail(user.getEmail()).setPassword(user.getPassword());
        ValidatableResponse response = client.registerUser(userWithoutName);
        response.assertThat().body("message", equalTo("Email, password and name are required fields")).and().body("success", equalTo(false)).and().statusCode(403);
        user = userWithoutName;
    }

    @Test
    public void registerNewUserWithoutAllRequiredFieldsTest() {
        User emptyUser = new User();
        ValidatableResponse response = client.registerUser(emptyUser);
        response.assertThat().body("message", equalTo("Email, password and name are required fields")).and().body("success", equalTo(false)).and().statusCode(403);
        user = emptyUser;
    }

    @After
    public void tearDown() throws InterruptedException {
        String body = client.loginUser(user).and().extract().body().path("accessToken");
        Thread.sleep(800);
        if (body != null) {
            client.deleteUser(user);
        }
    }
}