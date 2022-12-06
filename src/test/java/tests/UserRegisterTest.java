package tests;

import datamodel.UserRegisterDataModel;
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
    @ParameterizedTest(name = "[{index}] {argumentsWithNames} ")
    @MethodSource("dataprovider.DataProvider#provideUserRegisterNegativeData")
    public void userRegisterNegativeTest(UserRegisterDataModel testData) {

        Allure.description(testData.getTestDescription());
        Response response = apiCoreRequests.requestGenerator(testData, testData.getRequestDescription());
        Assertions.assertResponse(response, testData);
    }
}