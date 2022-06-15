package praktikum;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginTest {
    UserGeneration gen = new UserGeneration();
    User user = new User(gen.randomEmail(), gen.randomPassword(), gen.randomName());
    UserClient client = new UserClient();

    @Before
    public void setUp() {
        client.registerUser(user);
    }

    @Test
    public void loginPositiveTest() {
        ValidatableResponse response = client.loginUser(new User().setPassword(user.getPassword()).setEmail(user.getEmail()));
        response.assertThat().body("accessToken", notNullValue()).and().body("success", equalTo(true)).and().statusCode(200);
    }

    @Test
    public void loginWithIncorrectEmailAndPasswordTest() {
        ValidatableResponse response = client.loginUser(new User().setEmail("blabla@bla.com").setPassword("parol"));
        response.assertThat().body("message", equalTo("email or password are incorrect")).and().body("success", equalTo(false)).and().statusCode(401);
    }

    @Test
    public void loginWithIncorrectEmailTest() {
        ValidatableResponse response = client.loginUser(new User().setEmail("blabla@bla.com").setPassword(user.getPassword()));
        response.assertThat().body("message", equalTo("email or password are incorrect")).and().body("success", equalTo(false)).and().statusCode(401);
    }

    @Test
    public void loginWithIncorrectPasswordTest() {
        ValidatableResponse response = client.loginUser(new User().setEmail(user.getEmail()).setPassword("parol"));
        response.assertThat().body("message", equalTo("email or password are incorrect")).and().body("success", equalTo(false)).and().statusCode(401);
    }

    @Test
    public void loginWithoutAllRequiredFieldsTest() {
        ValidatableResponse response = client.loginUser(new User());
        response.assertThat().body("message", equalTo("email or password are incorrect")).and().body("success", equalTo(false)).and().statusCode(401);
    }

    @After
    public void tearDown() throws InterruptedException {
        client.deleteUser(user);
        Thread.sleep(800);
    }

}