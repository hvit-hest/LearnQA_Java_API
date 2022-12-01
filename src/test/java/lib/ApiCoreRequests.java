package lib;

import datamodel.UserRegisterDataModel;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiCoreRequests {

    @Step("Make a GET-request with header(token) and auth cookie")
    public Response makeGetRequest(String url, String token, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

    @Step("Make a GET-request with auth cookie only")
    public Response makeGetRequestWithCookie(String url, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

    @Step("Make a GET-request with auth token(header) only")
    public Response makeGetRequestWithToken(String url, String token) {
        return given()
                .filter(new AllureRestAssured())
                .header("auth_sid", token)
                .get(url)
                .andReturn();
    }

    @Step("Make a POST-request")
    public Response makePostRequest(String url, Map<String, String> authData) {
        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                .post(url)
                .andReturn();
    }

    //String requestDescription -> {1}
    @Step("{1}")
    public Response requestGenerator(UserRegisterDataModel testData, String requestDescription) {
        Response response;
        RequestSpecification reqspec = RestAssured.given();

        if (testData.getCookies() != null) reqspec.cookies(testData.getCookies());
        if (testData.getHeaders() != null) reqspec.headers(testData.getHeaders());
        if (testData.getUserData() != null) reqspec.body(DataGenerator.getRegistrationDataAlt(testData.getUserData()));

        switch (testData.getMethod()) {
            case "get":
                response = reqspec.get(testData.getTestUrl());
                break;
            case "post":
                response = reqspec.post(testData.getTestUrl());
                break;
            default:
                throw new IllegalArgumentException(String.format("Method '%s' is not implemented yet", testData.getMethod()));
        }
        return response;
    }
}
