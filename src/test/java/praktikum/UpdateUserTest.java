package praktikum;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class UpdateUserTest {

    User user = new User("ulanovda@gmail.com", "password", "densky");
    User updatedUser = new User("densky@yandex.ru", "parol","denis");
    User updateEmailUser = new User().setEmail("densky@yandex.ru");
    User updatePasswordUser = new User().setPassword("parol");
    User updateNameUser = new User().setName("denis");

    @Before
    public void setUp() {
        UserClient.registerUser(user);
    }

    @Test
    public void updateAllFieldsPositiveTest() {
        ValidatableResponse response = UserClient.updateUser(user, updatedUser);
        response.assertThat().body("user.email", equalTo("densky@yandex.ru")).and().body("user.name", equalTo("denis")).and().body("success", equalTo(true)).and().statusCode(200);
    }

    @Test
    public void updateOnlyEmailPositiveTest() {
        ValidatableResponse response = UserClient.updateUser(user, updateEmailUser);
        response.assertThat().body("user.email", equalTo("densky@yandex.ru")).and().body("user.name", equalTo("densky")).and().body("success", equalTo(true)).and().statusCode(200);
        updatedUser.setPassword("password").setName("densky");
    }

    @Test
    public void updateOnlyPasswordPositiveTest() {
        ValidatableResponse response = UserClient.updateUser(user, updatePasswordUser);
        response.assertThat().body("user.email", equalTo("ulanovda@gmail.com")).and().body("user.name", equalTo("densky")).and().body("success", equalTo(true)).and().statusCode(200);
        updatedUser.setEmail("ulanovda@gmail.com").setName("densky");
    }

    @Test
    public void updateOnlyNamePositiveTest() {
        ValidatableResponse response = UserClient.updateUser(user, updateNameUser);
        response.assertThat().body("user.email", equalTo("ulanovda@gmail.com")).and().body("user.name", equalTo("denis")).and().body("success", equalTo(true)).and().statusCode(200);
        updatedUser.setEmail("ulanovda@gmail.com").setPassword("password");
    }

    @Test
    public void updateUserWithExistingEmail() {
        User updateUserWithExistingEmail = new User("densky@yandex.ru", "pw123", "Vasiliy");
        UserClient.registerUser(updateUserWithExistingEmail);

        ValidatableResponse response = UserClient.updateUser(user, updateEmailUser);
        response.assertThat().body("message", equalTo("User with such email already exists")).and().body("success", equalTo(false)).and().statusCode(403);
        updatedUser = user;

        UserClient.deleteUser(updateUserWithExistingEmail);
    }

    @After
    public void tearDown() {
        UserClient.deleteUser(updatedUser);
    }
}