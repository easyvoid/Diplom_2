package praktikum;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


public class GetOrdersTest {
    UserGeneration gen = new UserGeneration();
    User user = new User(gen.randomEmail(), gen.randomPassword(), gen.randomName());
    UserClient client = new UserClient();
    OrderUserClient order = new OrderUserClient();

    @Before
    public void setUp() {
        client.registerUser(user);
    }

    @Test
    public void getOrdersWithAuthTest() {
        String firstIngredient = order.getIngredient(0);
        String secondIngredient = order.getIngredient(1);

        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add(firstIngredient);
        ingredients.add(secondIngredient);

        order.createOrderWithAuth(user, new Order().setIngredients(ingredients));

        ValidatableResponse response = order.getUserOrdersWithAuth(user);
        response.assertThat().body("orders[0].ingredients", notNullValue()).and().body("success", equalTo(true)).and().statusCode(200);
    }

    @Test
    public void getOrdersWithoutAuthTest() {
        String firstIngredient = order.getIngredient(0);
        String secondIngredient = order.getIngredient(1);

        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add(firstIngredient);
        ingredients.add(secondIngredient);

        order.createOrderWithAuth(user, new Order().setIngredients(ingredients));

        ValidatableResponse response = order.getUserOrdersWithoutAuth();
        response.assertThat().body("message", equalTo("You should be authorised")).and().body("success", equalTo(false)).and().statusCode(401);
    }

    @After
    public void tearDown() throws InterruptedException {
        client.deleteUser(user);
        Thread.sleep(800);
    }
}