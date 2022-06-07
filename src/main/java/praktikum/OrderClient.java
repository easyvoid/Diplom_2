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

    @Step("Запрос на получение первого ингредиента")
    public static String getFirstIngredient() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get("/api/ingredients")
                .then().log().all().extract().body().path("data[0]._id");
    }

    @Step("Запрос на получение второго ингредиента")
    public static String getSecondIngredient() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get("/api/ingredients")
                .then().log().all().extract().body().path("data[1]._id");
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
}
