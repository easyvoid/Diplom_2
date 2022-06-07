package praktikum;

import io.restassured.response.ValidatableResponse;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderWithoutAuthTest {

    @Test
    public void createNewOrderWithoutIngredientsTest() {
        ValidatableResponse response = OrderClient.createOrderNotAuth(new Order().setIngredients(new String[]{}));
        response.assertThat().body("message", equalTo("Ingredient ids must be provided")).and().body("success", equalTo(false)).and().statusCode(400);
    }

    @Test
    public void createNewOrderWithInvalidIngredientsIdTest() {
        ValidatableResponse response = OrderClient.createOrderNotAuth(new Order().setIngredients(new String[]{"123", "321"}));
        response.assertThat().statusCode(500);
    }

    @Test
    public void createNewOrderPositiveTest() {
        String firstIngredient = OrderClient.getFirstIngredient();
        String secondIngredient = OrderClient.getSecondIngredient();

        ValidatableResponse response = OrderClient.createOrderNotAuth(new Order().setIngredients(new String[]{firstIngredient, secondIngredient}));
        response.assertThat().body("order.number", notNullValue()).and().body("success", equalTo(true)).and().statusCode(200);
    }

}