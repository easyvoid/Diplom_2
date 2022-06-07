package praktikum;

import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class UserClient {

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

    @Step("Запрос на создание пользователя: {user}")
    public static ValidatableResponse registerUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post("/api/auth/register")
                .then().log().all();
    }

    @Step("Запрос на авторизацию пользователя: {user}, который возвращает его accessToken и refreshToken")
    public static ValidatableResponse loginUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post("/api/auth/login")
                .then().log().all();
    }

    @Step("Запрос на удаление пользователя: {user} по его accessToken")
    public static void deleteUser(User user) {
        String accessToken = loginUser(user).and().extract().body().path("accessToken");
        given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .when()
                .delete("/api/auth/user")
                .then().log().all();
    }

    @Step("Запрос на выход из системы пользователя: {user} по его refreshToken")
    public static void logoutUser(User user) {
        String refreshToken = loginUser(user).and().extract().body().path("refreshToken");
        given()
                .spec(getBaseSpec())
                .body("{\"token\": \"" + refreshToken + "\"}")
                .when()
                .post("/api/auth/logout")
                .then().log().all();
    }

    @Step("Запрос на изменение пользователя по его accessToken")
    public static ValidatableResponse updateUser(User user, User updatedUser) {
        String accessToken = loginUser(user).and().extract().body().path("accessToken");
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .body(updatedUser)
                .when()
                .patch("/api/auth/user")
                .then().log().all();
    }
}
