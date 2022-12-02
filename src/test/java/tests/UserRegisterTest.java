package tests;

import datamodel.UserRegisterDataModel;
import dataprovider.DataProvider;
import io.qameta.allure.Allure;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.Map;

@Feature("Create user cases")
public class UserRegisterTest extends BaseTestCase {
    ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    public void testCreateUserWithExistingEmail() {
        String email = "vinkotov@example.com";
        Map<String, String> userData = new HashMap<String, String>() {{
            put("email", email);
        }};

        userData = DataGenerator.getRegistrationData(userData);
        Response responseCreateAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/", userData);
        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth,
                String.format("Users with email '%s' already exists", email));
    }

    @Test
    public void testCreateUserSuccessfully() {
        Map<String, String> userData = DataGenerator.getRegistrationData();
        Response responseCreateAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/", userData);
        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
        Assertions.assertJsonHasField(responseCreateAuth, "id");
    }

    @DisplayName("Negative tests. Create user.")
    @ParameterizedTest(name="[{index}] {argumentsWithNames} ")
    @MethodSource("dataprovider.DataProvider#provideUserRegisterNegativeData")
    public void userRegisterNegativeTest(UserRegisterDataModel testData) {

        Allure.description(testData.getTestDescription());
        Response response = apiCoreRequests.requestGenerator(testData, testData.getRequestDescription());
        Assertions.assertResponse(response, testData);
    }

    @DisplayName("Negative tests. Try to take a user data with wrong authorization.")
    @ParameterizedTest(name="[{index}] {argumentsWithNames} ")
    @MethodSource("dataprovider.DataProvider#provideTakeUserInfoNegativeData")
    public void takeUserInfoNegativeTest(UserRegisterDataModel testData) {
        Allure.description(testData.getTestDescription());


        //CREATE TWO USERS
        /*create  two users for the test
        We need to create two users. It is the test precondition since we assume our DB in the test environment has been cleaned
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
            put("email",createSecondUserData.getUserData().get("email"));
            put("password",createSecondUserData.getUserData().get("password"));
        }});

        Response responseLoginUser = apiCoreRequests.requestGenerator(loginUserData, loginUserData.getRequestDescription());

        //SECOND USER TRIES TO GET DATA OF FIRST ONE
        testData.setCookies(new HashMap<String, String>() {{put("auth_sid", responseLoginUser.cookie("auth_sid"));}});
        testData.setHeaders(new HashMap<String, String>() {{put("x-csrf-token",responseLoginUser.header("x-csrf-token"));}});
        testData.getExpectedValues().put("username", username);
        testData.setTestUrl(String.format(testData.getTestUrl(),userID));
        Response responseGetUserData = apiCoreRequests.requestGenerator(testData, testData.getRequestDescription());

        Assertions.assertResponse(responseGetUserData, testData);
    }
}