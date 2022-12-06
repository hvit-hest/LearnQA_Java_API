package tests;

import datamodel.User;
import datamodel.UserRegisterDataModel;
import io.qameta.allure.Allure;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;

@Feature("Change user data cases")
public class UserPutTest extends BaseTestCase {
    private ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @DisplayName("Negative tests. Try to change a user data. ")
    @ParameterizedTest(name = "[{index}] {argumentsWithNames} ")
    @MethodSource("dataprovider.DataProvider#provideChangeUserInfoNegativeData")
    public void changeUserInfoNegativeTest(UserRegisterDataModel testData) {
        Allure.description(testData.getTestDescription());
        User user1;
        User user2;
        Response response;
        Map<String, String> expectedUserData;

        //CREATE TWO USERS
        /*create  two users for the test
        Sometimes we need to create two users. It wil be the test precondition since we assume our DB in the test environment has been cleaned
        before test run
         */

        //create user1
        user1 = apiCoreRequests.createUser();
        user1.setUserID();
        //login user1
        apiCoreRequests.loginUser(user1);
        //create user2
        user2 = apiCoreRequests.createUser();
        user2.setUserID();
        //get data to change from test's json
        Map<String, String> dataToChange = DataGenerator.getRegistrationDataAlt(testData.getUserData());

        //action depends upon test data from json
        switch (testData.getTestPrecondition()) {
            case "withoutAuthorization":
                //test without authorization
                apiCoreRequests.editUserWithoutAuth(user1, dataToChange);
                break;
            case "requestToChangeFromOtherUser":
                //user1 put request to change user2
                apiCoreRequests.editUser1ByUser2(user2, user1, dataToChange);
                break;
            case "requestToChangeFromCurrentUser":
                //user1 put request to change himself
                apiCoreRequests.editUser(user1, dataToChange);
                break;
            default:
                throw new IllegalArgumentException(String.format("Test '%s' is not implemented yet", testData.getTestPrecondition()));
        }

        //the assertion uses instructions and data from json
        Assertions.assertResponse(user1.getResponse("putResponse"), testData);

        //check that user was not changed
        if (testData.getTestPrecondition().equals("requestToChangeFromOtherUser")) {
            apiCoreRequests.loginUser(user2);
            apiCoreRequests.getUser(user2);
            expectedUserData = user2.getUserData();
            expectedUserData.remove("password");
            expectedUserData.put("id", user2.getUserID());
            response = user2.getResponse("getResponse");
        } else {
            apiCoreRequests.getUser(user1);
            expectedUserData = user1.getUserData();
            expectedUserData.remove("password");
            expectedUserData.put("id", user1.getUserID());
            response = user1.getResponse("getResponse");
        }
        //check that data was not changed
        Assertions.mapContains(response, "$", expectedUserData, true);
    }
}