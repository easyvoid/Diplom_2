package praktikum;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;

public class RequestSpec {

    private static final String baseURI = "https://stellarburgers.nomoreparties.site/";

    private static final RequestSpecification baseSpec = new RequestSpecBuilder()
            .setBaseUri(baseURI)
            .setContentType("application/json")
            .log(LogDetail.ALL)
            .build();

    public static RequestSpecification getBaseSpec() {
        return baseSpec;
    }

}
