package tests;

import datamodel.UserRegisterDataModel;
import dataprovider.DataProvider;
import io.qameta.allure.Allure;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.Map;

@Feature("Change user data cases")
public class UserPutTest extends BaseTestCase {
    ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @DisplayName("Negative tests. Try to change a user data. ")
    @ParameterizedTest(name = "[{index}] {argumentsWithNames} ")
    @MethodSource("dataprovider.DataProvider#provideChangeUserInfoNegativeData")
    public void changeUserInfoNegativeTest(UserRegisterDataModel testData) {
        Allure.description(testData.getTestDescription());


        //CREATE TWO USERS
        /*create  two users for the test
        Sometimes we need to create two users. It wil be the test precondition since we assume our DB in the test environment has been cleaned
        before test run
         */

        //create user1 to try to change his data
        UserRegisterDataModel createFirstUserData = DataProvider.provideUserRegisterPositiveData().findFirst().get();
        UserRegisterDataModel loginUserData = DataProvider.provideUserLoginPositiveData().findFirst().get();
        Response responseFirstUser = apiCoreRequests
                .requestGenerator(createFirstUserData, "POST: Create first user whose data will be under attempt to change ");
        String userID = responseFirstUser.body().jsonPath().get("id");
        Response responseLoginUser = null;
        String cookie;
        String header;

        //AUTHORIZATION/LOGIN
        switch (testData.getTestPrecondition()) {
            case "withoutAuthorization":
                //test without authorization
                //do nothing here
                break;
            case "requestToChangeFromOtherUser":
                //create user2 who will try to change user1 data
                UserRegisterDataModel createSecondUserData = DataProvider.provideUserRegisterPositiveData().findFirst().get();
                apiCoreRequests.requestGenerator(createSecondUserData, "POST: Create second user");
                //change login test data: here add real email and password
                loginUserData.setUserData(new HashMap<String, String>() {{
                    put("email", createSecondUserData.getUserData().get("email"));
                    put("password", createSecondUserData.getUserData().get("password"));
                }});
                //put authorization/login user2;
                responseLoginUser = apiCoreRequests.requestGenerator(loginUserData, loginUserData.getRequestDescription());
                cookie = responseLoginUser.cookie("auth_sid");
                header = responseLoginUser.header("x-csrf-token");
                testData.setCookies(new HashMap<String, String>() {{
                    put("auth_sid", cookie);
                }});
                testData.setHeaders(new HashMap<String, String>() {{
                    put("x-csrf-token", header);
                }});
                break;
            case "requestToChangeFromCurrentUser":
                //change login test data: here add real email and password
                loginUserData.setUserData(new HashMap<String, String>() {{
                    put("email", createFirstUserData.getUserData().get("email"));
                    put("password", createFirstUserData.getUserData().get("password"));
                }});
                //authorization/login user1;
                responseLoginUser = apiCoreRequests.requestGenerator(loginUserData, loginUserData.getRequestDescription());
                cookie = responseLoginUser.cookie("auth_sid");
                header = responseLoginUser.header("x-csrf-token");
                testData.setCookies(new HashMap<String, String>() {{
                    put("auth_sid", cookie);
                }});
                testData.setHeaders(new HashMap<String, String>() {{
                    put("x-csrf-token", header);
                }});
                break;
            default:
                throw new IllegalArgumentException(String.format("Test '%s' is not implemented yet", testData.getMethod()));
        }

        //try to change user1's data in testData: edit testData url adding userID
        //form put request
        testData.setTestUrl(String.format(testData.getTestUrl(), userID));
        Response response = apiCoreRequests.requestGenerator(testData, testData.getRequestDescription());
        //the assertion uses instructions and data from json
        Assertions.assertResponse(response, testData);

        //login as user1 if it is necessary (maybe we have gotten cookie and header above) to get his data
        if (testData.getTestPrecondition().equals("requestToChangeFromOtherUser") ||
                testData.getTestPrecondition().equals("withoutAuthorization")) {
            responseLoginUser = apiCoreRequests.makePostRequest(loginUserData.getTestUrl(),
                    new HashMap<String, String>() {{
                        put("email", createFirstUserData.getUserData().get("email"));
                        put("password", createFirstUserData.getUserData().get("password"));
                    }});
        }
        //form get request, compare  1-st user's data before and after attempt to change them

        responseFirstUser = apiCoreRequests.makeGetRequest(String.format(testData.getTestUrl(), userID),
                responseLoginUser.getHeader("x-csrf-token"), responseLoginUser.getCookie("auth_sid"));

        //check that all fields of user 1 are without change
        Map<String, String> firstActualUserData = responseFirstUser.body().jsonPath().getMap("$");
        Map<String, String> firstExpectedUserData = createFirstUserData.getUserData();
        firstExpectedUserData.remove("password");
        firstExpectedUserData.put("id", userID);

        Assertions.mapContains(createFirstUserData.getUserData(), firstActualUserData, true);
    }
}