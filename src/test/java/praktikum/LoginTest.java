package praktikum;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginTest {

    User user = new User("ulanovda@gmail.com", "password", "densky");

    @Before
    public void setUp() {
        UserClient.registerUser(user);
    }

    @Test
    public void loginPositiveTest() {
        ValidatableResponse response = UserClient.loginUser(new User().setPassword("password").setEmail("ulanovda@gmail.com"));
        response.assertThat().body("accessToken", notNullValue()).and().body("success", equalTo(true)).and().statusCode(200);
    }

    @Test
    public void loginWithIncorrectEmailAndPasswordTest() {
        ValidatableResponse response = UserClient.loginUser(new User().setEmail("blabla@bla.com").setPassword("parol"));
        response.assertThat().body("message", equalTo("email or password are incorrect")).and().body("success", equalTo(false)).and().statusCode(401);
    }

    @Test
    public void loginWithIncorrectEmailTest() {
        ValidatableResponse response = UserClient.loginUser(new User().setEmail("blabla@bla.com").setPassword("password"));
        response.assertThat().body("message", equalTo("email or password are incorrect")).and().body("success", equalTo(false)).and().statusCode(401);
    }

    @Test
    public void loginWithIncorrectPasswordTest() {
        ValidatableResponse response = UserClient.loginUser(new User().setEmail("ulanovda@gmail.com").setPassword("parol"));
        response.assertThat().body("message", equalTo("email or password are incorrect")).and().body("success", equalTo(false)).and().statusCode(401);
    }

    @Test
    public void loginWithoutAllRequiredFieldsTest() {
        ValidatableResponse response = UserClient.loginUser(new User());
        response.assertThat().body("message", equalTo("email or password are incorrect")).and().body("success", equalTo(false)).and().statusCode(401);
    }

    @After
    public void tearDown() throws InterruptedException {
        UserClient.deleteUser(user);
        Thread.sleep(1000);
    }

}