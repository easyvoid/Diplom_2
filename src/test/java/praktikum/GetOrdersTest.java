package praktikum;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


public class GetOrdersTest {
    User user = new User("ulanovda@gmail.com", "password", "densky");

    @Before
    public void setUp() {
        UserClient.registerUser(user);
    }

    @Test
    public void getOrdersWithAuthTest() {
        String firstIngredient = OrderClient.getIngredient(0);
        String secondIngredient = OrderClient.getIngredient(1);

        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add(firstIngredient);
        ingredients.add(secondIngredient);

        OrderClient.createOrderWithAuth(user, new Order().setIngredients(ingredients));

        ValidatableResponse response = OrderClient.getUserOrdersWithAuth(user);
        response.assertThat().body("orders[0].ingredients", notNullValue()).and().body("success", equalTo(true)).and().statusCode(200);
    }

    @Test
    public void getOrdersWithoutAuthTest() {
        String firstIngredient = OrderClient.getIngredient(0);
        String secondIngredient = OrderClient.getIngredient(1);

        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add(firstIngredient);
        ingredients.add(secondIngredient);

        OrderClient.createOrderWithAuth(user, new Order().setIngredients(ingredients));

        ValidatableResponse response = OrderClient.getUserOrdersWithoutAuth();
        response.assertThat().body("message", equalTo("You should be authorised")).and().body("success", equalTo(false)).and().statusCode(401);
    }

    @After
    public void tearDown() throws InterruptedException {
        UserClient.deleteUser(user);
        Thread.sleep(500);
    }
}