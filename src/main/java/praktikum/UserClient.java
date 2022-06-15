package praktikum;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static praktikum.RequestSpec.getBaseSpec;

public class UserClient {

    @Step("Запрос на создание пользователя: {user}")
    public ValidatableResponse registerUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post("/api/auth/register")
                .then().log().all();
    }

    @Step("Запрос на авторизацию пользователя: {user}, который возвращает его accessToken и refreshToken")
    public ValidatableResponse loginUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post("/api/auth/login")
                .then().log().all();
    }

    @Step("Запрос на удаление пользователя: {user} по его accessToken")
    public void deleteUser(User user) {
        String accessToken = loginUser(user).and().extract().body().path("accessToken");
        given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .when()
                .delete("/api/auth/user")
                .then().log().all();
    }

    @Step("Запрос на выход из системы пользователя: {user} по его refreshToken")
    public void logoutUser(User user) {
        String refreshToken = loginUser(user).and().extract().body().path("refreshToken");
        HashMap<String,Object> dataBody = new HashMap<>();
        dataBody.put("token", refreshToken);

        given()
                .spec(getBaseSpec())
                .body(dataBody)
                .when()
                .post("/api/auth/logout")
                .then().log().all();
    }

    @Step("Запрос на изменение пользователя по его accessToken")
    public ValidatableResponse updateUser(User user, User updatedUser) {
        String accessToken = loginUser(user).and().extract().body().path("accessToken");
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .body(updatedUser)
                .when()
                .patch("/api/auth/user")
                .then().log().all();
    }

    @Step("Запрос на изменение пользователя без его accessToken")
    public ValidatableResponse updateUserWithoutLogin(User updatedUser) {
        return given()
                .spec(getBaseSpec())
                .body(updatedUser)
                .when()
                .patch("/api/auth/user")
                .then().log().all();
    }
}
