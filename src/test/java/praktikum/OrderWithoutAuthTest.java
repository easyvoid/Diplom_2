package praktikum;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderWithoutAuthTest {

    @Test
    public void createNewOrderWithoutIngredientsTest() {
        ValidatableResponse response = OrderClient.createOrderNotAuth(new Order().setIngredients(new ArrayList<>()));
        response.assertThat().body("message", equalTo("Ingredient ids must be provided")).and().body("success", equalTo(false)).and().statusCode(400);
    }

    @Test
    public void createNewOrderWithInvalidIngredientsIdTest() {
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("123");
        ingredients.add("321");

        ValidatableResponse response = OrderClient.createOrderNotAuth(new Order().setIngredients(ingredients));
        response.assertThat().statusCode(500);
    }

    @Test
    public void createNewOrderPositiveTest() {
        String firstIngredient = OrderClient.getIngredient(0);
        String secondIngredient = OrderClient.getIngredient(1);

        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add(firstIngredient);
        ingredients.add(secondIngredient);

        ValidatableResponse response = OrderClient.createOrderNotAuth(new Order().setIngredients(ingredients));
        response.assertThat().body("order.number", notNullValue()).and().body("success", equalTo(true)).and().statusCode(200);
    }

    @After
    public void tearDown() throws InterruptedException {
        Thread.sleep(800);
    }

}