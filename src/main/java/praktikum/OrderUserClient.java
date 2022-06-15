package praktikum;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;


import static io.restassured.RestAssured.given;
import static praktikum.RequestSpec.getBaseSpec;

public class OrderUserClient extends UserClient {

    private static final String baseURI = "https://stellarburgers.nomoreparties.site/";

    @Step("Запрос на получение ингредиента с id = {index}")
    public String getIngredient(int index) {
        String path = String.format("data[%s]._id", index);
        return given()
                .spec(getBaseSpec())
                .when()
                .get("/api/ingredients")
                .then().log().all().extract().body().path(path);
    }


    @Step("Запрос на создание заказа без авторизации")
    public ValidatableResponse createOrderNotAuth(Order order) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post("/api/orders")
                .then().log().all();
    }

    @Step("Запрос на создание заказа с авторизацией")
    public ValidatableResponse createOrderWithAuth(User user, Order order) {
        String accessToken = loginUser(user).and().extract().body().path("accessToken");
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post("/api/orders")
                .then().log().all();
    }

    @Step("Запрос на получение заказов конкретного пользователя {user}")
    public ValidatableResponse getUserOrdersWithAuth(User user) {
        String accessToken = loginUser(user).and().extract().body().path("accessToken");
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .when()
                .get("/api/orders")
                .then().log().all();
    }

    @Step("Запрос на получение заказов без авторизации")
    public ValidatableResponse getUserOrdersWithoutAuth() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get("/api/orders")
                .then().log().all();
    }
}
