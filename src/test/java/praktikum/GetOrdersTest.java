package praktikum;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;

public class GetOrdersTest {
    User user = new User("ulanovda@gmail.com", "password", "densky");

    @Before
    public void setUp() {
        UserClient.registerUser(user);
    }

    @Test
    public void getOrdersWithAuthTest() {
        String firstIngredient = OrderClient.getFirstIngredient();
        String secondIngredient = OrderClient.getSecondIngredient();

        OrderClient.createOrderWithAuth(user, new Order().setIngredients(new String[]{firstIngredient, secondIngredient}));

        ValidatableResponse response = OrderClient.getUserOrdersWithAuth(user);
        response.assertThat().body("orders[0].ingredients", notNullValue()).and().body("success", equalTo(true)).and().statusCode(200);
    }

    @Test
    public void getOrdersWithoutAuthTest() {
        String firstIngredient = OrderClient.getFirstIngredient();
        String secondIngredient = OrderClient.getSecondIngredient();

        OrderClient.createOrderWithAuth(user, new Order().setIngredients(new String[]{firstIngredient, secondIngredient}));

        ValidatableResponse response = OrderClient.getUserOrdersWithoutAuth();
        response.assertThat().body("message", equalTo("You should be authorised")).and().body("success", equalTo(false)).and().statusCode(401);
    }

    @After
    public void tearDown() {
        UserClient.deleteUser(user);
    }
}