package tests;

import datamodel.UserRegisterDataModel;
import dataprovider.DataProvider;
import io.qameta.allure.Allure;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.Map;


@Feature("Get user data cases")
public class UserGetTest extends BaseTestCase {
    ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    public void testGetUserDataNotAuth() {
        Response responseUserData = RestAssured
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();
        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNotField(responseUserData, "firstName");
        Assertions.assertJsonHasNotField(responseUserData, "lastName");
        Assertions.assertJsonHasNotField(responseUserData, "email");
    }

    @Test
    public void testGetUserDetailsAuthAsSameUser() {

        Map<String, String> userData = new HashMap<String, String>() {{
            put("email", "vinkotov@example.com");
            put("password", "1234");
        }};
        Response responseGetAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token", header)
                .cookie("auth_sid", cookie)
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();

        String[] expectedFields = {"username", "firstName", "lastName", "email"};
        Assertions.assertJsonHasFields(responseUserData, expectedFields);
    }

    @DisplayName("Negative tests. Try to take a user data with wrong authorization.")
    @ParameterizedTest(name = "[{index}] {argumentsWithNames} ")
    @MethodSource("dataprovider.DataProvider#provideTakeUserInfoNegativeData")
    public void takeUserInfoNegativeTest(UserRegisterDataModel testData) {
        Allure.description(testData.getTestDescription());


        //CREATE TWO USERS
        /*create  two users for the test
        We need to create two users. It is the test precondition since we assume our DB in the test environment can be cleaned
        before test run
         */
        UserRegisterDataModel createFirstUserData = DataProvider.provideUserRegisterPositiveData().findFirst().get();
        Response responseFirstUser = apiCoreRequests.requestGenerator(createFirstUserData, "POST Create first user");
        String userID = responseFirstUser.body().jsonPath().get("id");
        String username = createFirstUserData.getUserData().get("username");
        UserRegisterDataModel createSecondUserData = DataProvider.provideUserRegisterPositiveData().findFirst().get();
        apiCoreRequests.requestGenerator(createSecondUserData, "POST Create second user");

        //SECOND USER LOGIN
        //createUserData changed after work of requestGenerator

        UserRegisterDataModel loginUserData = DataProvider.provideUserLoginPositiveData().findFirst().get();
        loginUserData.setUserData(new HashMap<String, String>() {{
            put("email", createSecondUserData.getUserData().get("email"));
            put("password", createSecondUserData.getUserData().get("password"));
        }});

        Response responseLoginUser = apiCoreRequests.requestGenerator(loginUserData, loginUserData.getRequestDescription());

        //SECOND USER TRIES TO GET DATA OF FIRST ONE
        testData.setCookies(new HashMap<String, String>() {{
            put("auth_sid", responseLoginUser.cookie("auth_sid"));
        }});
        testData.setHeaders(new HashMap<String, String>() {{
            put("x-csrf-token", responseLoginUser.header("x-csrf-token"));
        }});
        //we know response code expected but don't know username of the created user so we add it to our data json
        testData.getExpectedValues().put("username", username);
        testData.setTestUrl(String.format(testData.getTestUrl(), userID));
        Response responseGetUserData = apiCoreRequests.requestGenerator(testData, testData.getRequestDescription());

        Assertions.assertResponse(responseGetUserData, testData);
    }
}