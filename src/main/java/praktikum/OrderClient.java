package praktikum;

import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static praktikum.UserClient.loginUser;

public class OrderClient {

    private static final String baseURI = "https://stellarburgers.nomoreparties.site/";

    private static RequestSpecification baseSpec = new RequestSpecBuilder()
            .setBaseUri(baseURI)
            .setContentType("application/json")
            .log(LogDetail.ALL)
            .build();

    public static RequestSpecification getBaseSpec() {
        return baseSpec;
    }

    public static String getBaseURI() {
        return baseURI;
    }

    @Step("Запрос на получение ингредиента с id = {index}")
    public static String getIngredient(int index) {
        String path = String.format("data[%s]._id", index);
        return given()
                .spec(getBaseSpec())
                .when()
                .get("/api/ingredients")
                .then().log().all().extract().body().path(path);
    }


    @Step("Запрос на создание заказа без авторизации")
    public static ValidatableResponse createOrderNotAuth(Order order) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post("/api/orders")
                .then().log().all();
    }

    @Step("Запрос на создание заказа с авторизацией")
    public static ValidatableResponse createOrderWithAuth(User user, Order order) {
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
    public static ValidatableResponse getUserOrdersWithAuth(User user) {
        String accessToken = loginUser(user).and().extract().body().path("accessToken");
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .when()
                .get("/api/orders")
                .then().log().all();
    }

    @Step("Запрос на получение заказов без авторизации")
    public static ValidatableResponse getUserOrdersWithoutAuth() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get("/api/orders")
                .then().log().all();
    }
}
