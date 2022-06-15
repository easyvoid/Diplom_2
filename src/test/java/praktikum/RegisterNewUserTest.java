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

        client.deleteUser(user);
    }

    @Test
    public void registerSameUserTest() {
        client.registerUser(user);

        ValidatableResponse response = client.registerUser(user);
        response.assertThat().body("message", equalTo("User already exists")).and().body("success", equalTo(false)).and().statusCode(403);

        client.deleteUser(user);
    }

    @Test
    public void registerNewUserWithoutEmailTest() {
        ValidatableResponse response = client.registerUser(new User().setPassword(user.getPassword()).setName(user.getName()));
        response.assertThat().body("message", equalTo("Email, password and name are required fields")).and().body("success", equalTo(false)).and().statusCode(403);
    }

    @Test
    public void registerNewUserWithoutPasswordTest() {
        ValidatableResponse response = client.registerUser(new User().setEmail(user.getEmail()).setName(user.getName()));
        response.assertThat().body("message", equalTo("Email, password and name are required fields")).and().body("success", equalTo(false)).and().statusCode(403);
    }

    @Test
    public void registerNewUserWithoutNameTest() {
        ValidatableResponse response = client.registerUser(new User().setEmail(user.getEmail()).setPassword(user.getPassword()));
        response.assertThat().body("message", equalTo("Email, password and name are required fields")).and().body("success", equalTo(false)).and().statusCode(403);
    }

    @Test
    public void registerNewUserWithoutAllRequiredFieldsTest() {
        ValidatableResponse response = client.registerUser(new User());
        response.assertThat().body("message", equalTo("Email, password and name are required fields")).and().body("success", equalTo(false)).and().statusCode(403);
    }

    @After
    public void tearDown() throws InterruptedException {
        Thread.sleep(800);
    }
}