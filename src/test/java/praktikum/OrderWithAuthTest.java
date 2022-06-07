package praktikum;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderWithAuthTest {

    User user = new User("ulanovda@gmail.com", "password", "densky");

    @Before
    public void setUp() {
        UserClient.registerUser(user);
    }

    @Test
    public void createNewOrderWithoutIngredientsTest() {
        ValidatableResponse response = OrderClient.createOrderWithAuth(user ,new Order().setIngredients(new String[]{}));
        response.assertThat().body("message", equalTo("Ingredient ids must be provided")).and().body("success", equalTo(false)).and().statusCode(400);
    }

    @Test
    public void createNewOrderWithInvalidIngredientsIdTest() {
        ValidatableResponse response = OrderClient.createOrderWithAuth(user, new Order().setIngredients(new String[]{"123", "321"}));
        response.assertThat().statusCode(500);
    }

    @Test
    public void createNewOrderPositiveTest() {
        String firstIngredient = OrderClient.getFirstIngredient();
        String secondIngredient = OrderClient.getSecondIngredient();

        ValidatableResponse response = OrderClient.createOrderWithAuth(user, new Order().setIngredients(new String[]{firstIngredient, secondIngredient}));
        response.assertThat().body("order.number", notNullValue()).and().body("success", equalTo(true)).and().body("order.owner.name", equalTo("densky")).and().statusCode(200);
    }

    @After
    public void tearDown() {
        UserClient.deleteUser(user);
    }
}