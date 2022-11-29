package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

@Epic("Authorization cases")
@Feature("Authorization")
public class UserAuthTest extends BaseTestCase {
    private String cookie;
    private String header;
    private int userIdOnAuth;
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    String baseUriAuth = "https://playground.learnqa.ru/api/user/auth";

    @BeforeEach
    public void loginUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");
        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        cookie = getCookie(responseGetAuth, "auth_sid");
        header = getHeader(responseGetAuth, "x-csrf-token");
        userIdOnAuth = getIntFromJson(responseGetAuth, "user_id");
    }

    @Test
    @Description("This test successfully authorizes an user by email and password")
    @DisplayName("Test positive auth user")
    public void testAuthUser() {
        Response responseCheckAuth = apiCoreRequests
                .makeGetRequest(baseUriAuth, header, cookie);
        Assertions.assertJsonByName(responseCheckAuth, "user_id", userIdOnAuth);
    }

    @Description("This test checks authorization status w/o sending cookie or token")
    @DisplayName("Test negative auth user")
    @ParameterizedTest
    @ValueSource(strings = {"cookie", "headers"})
    public void testNegativeAuthUser(String condition) {
/*        RequestSpecification spec = RestAssured.given();
        spec.baseUri("https://playground.learnqa.ru/api/user/auth");
        if (condition.equals("cookie")) {
            spec.cookie("auth_sid", cookie);
        } else if (condition.equals("headers")) {
            spec.header("x-csrf-token", header);
        } else {
            throw new IllegalArgumentException("Condition value is known: " + condition);
        }
        Response responseForCheck = spec.get().andReturn();*/

        Response responseForCheck;
        if (condition.equals("cookie")) {
            responseForCheck = apiCoreRequests.makeGetRequestWithCookie(baseUriAuth, cookie);
        } else if (condition.equals("headers")) {
            responseForCheck = apiCoreRequests.makeGetRequestWithToken(baseUriAuth, header);
        } else {
            throw new IllegalArgumentException("Condition value is known: " + condition);
        }
        Assertions.assertJsonByName(responseForCheck, "user_id", 0);
    }
}
