package praktikum;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class UpdateUserParamTest {
    String email;
    String password;
    String name;
    boolean expectedStatus;
    User user = new User("ulanovda@gmail.com", "password", "densky");

    public UpdateUserParamTest(String email, String password, String name, boolean expectedStatus) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.expectedStatus = expectedStatus;
    }

    @Parameterized.Parameters
    public static Object[][] getUpdateUserData() {
        return new Object[][]{
                {
                        "updatedEmail@go.gg",
                        "updatedPw",
                        "updatedName",
                        false
                },
                {
                        "updatedEmail@go.gg",
                        "password",
                        "densky",
                        false
                },
                {
                        "ulanovda@gmail.com",
                        "updatedPw",
                        "densky",
                        false
                },
                {
                        "ulanovda@gmail.com",
                        "password",
                        "updatedName",
                        false
                },
                {
                        "updatedEmail@go.gg",
                        "password",
                        "updatedName",
                        false
                }
        };
    }

    @Before
    public void setUp() {
        UserClient.registerUser(user);
    }

    @Test
    public void updateNegativeTest() {
        User updatedUser = new User(email, password, name);

        ValidatableResponse response = given()
                .baseUri("https://stellarburgers.nomoreparties.site/")
                .header("Content-type", "application/json")
                .log().all()
                .body(updatedUser)
                .when()
                .patch("/api/auth/user")
                .then().log().all();

        response.assertThat().body("message", equalTo("You should be authorised")).and().body("success", equalTo(false)).and().statusCode(401);
        // задаю переменной statusCode значение статус кода из ответа
        int statusCode = response.extract().statusCode();
        // актуальный статус будет false, если статус код = 401
        boolean actualStatus = !(statusCode==401);

        assertEquals(expectedStatus, actualStatus);
    }

    @After
    public void tearDown() throws InterruptedException {
        UserClient.deleteUser(user);
        Thread.sleep(500);
    }
}