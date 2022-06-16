package praktikum;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class UpdateUserTest {
    UserClient client = new UserClient();
    UserGeneration gen = new UserGeneration();
    User user = new User(gen.randomEmail(), gen.randomPassword(), gen.randomName());
    User updatedUser = new User(gen.randomEmail(), gen.randomPassword(), gen.randomName());

    @Before
    public void setUp() {
        client.registerUser(user);
    }

    @Test
    public void updateAllFieldsPositiveTest() {
        ValidatableResponse response = client.updateUser(user, updatedUser);
        response.assertThat().body("user.email", equalTo(updatedUser.getEmail())).and().body("user.name", equalTo(updatedUser.getName())).and().body("success", equalTo(true)).and().statusCode(200);
    }

    @Test
    public void updateOnlyEmailPositiveTest() {
        User updateEmailUser = new User().setEmail(updatedUser.getEmail());
        ValidatableResponse response = client.updateUser(user, updateEmailUser);
        response.assertThat().body("user.email", equalTo(updatedUser.getEmail())).and().body("user.name", equalTo(user.getName())).and().body("success", equalTo(true)).and().statusCode(200);
        updatedUser.setPassword(user.getPassword()).setName(user.getName());
    }

    @Test
    public void updateOnlyPasswordPositiveTest() {
        User updatePasswordUser = new User().setPassword(updatedUser.getPassword());
        ValidatableResponse response = client.updateUser(user, updatePasswordUser);
        response.assertThat().body("user.email", equalTo(user.getEmail())).and().body("user.name", equalTo(user.getName())).and().body("success", equalTo(true)).and().statusCode(200);
        updatedUser.setEmail(user.getEmail()).setName(user.getName());
    }

    @Test
    public void updateOnlyNamePositiveTest() {
        User updateNameUser = new User().setName(updatedUser.getName());
        ValidatableResponse response = client.updateUser(user, updateNameUser);
        response.assertThat().body("user.email", equalTo(user.getEmail())).and().body("user.name", equalTo(updatedUser.getName())).and().body("success", equalTo(true)).and().statusCode(200);
        updatedUser.setEmail(user.getEmail()).setPassword(user.getPassword());
    }

    @Test
    public void updateUserWithExistingEmail() {
        User updateEmailUser = new User().setEmail(updatedUser.getEmail());
        User updateUserWithExistingEmail = new User(updatedUser.getEmail(), "pw123", "Vasiliy");
        client.registerUser(updateUserWithExistingEmail);

        ValidatableResponse response = client.updateUser(user, updateEmailUser);
        response.assertThat().body("message", equalTo("User with such email already exists")).and().body("success", equalTo(false)).and().statusCode(403);
        updatedUser = user;

        client.deleteUser(updateUserWithExistingEmail);
    }

    @After
    public void tearDown() throws InterruptedException {
        Thread.sleep(800);
        client.deleteUser(updatedUser);
    }
}