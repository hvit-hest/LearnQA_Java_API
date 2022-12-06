package datamodel;

import io.restassured.http.Headers;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static lib.DataGenerator.*;

public class User {
    private Map<String, String> userData = new HashMap<>();
    private Map<String, Response> lastResponses = new HashMap<>();
    String userID;

    public User() {
        this.userData = new HashMap() {{
            put("email", getRandomEmail());
            put("password", generatePassword());
            put("username", generateUserName());
            put("firstName", randomFirstName());
            put("lastName", randomLastName());
        }};
    }

    public User(String userID) {
        switch (userID) {
            case "2":
                this.userID = "2";
                this.userData = new HashMap() {{
                    put("email", "vinkotov@example.com");
                    put("password", "1234");
                    put("username", "Vitaliy");
                    put("firstName", "Vitalii");
                    put("lastName", "Kotov");
                }};
                break;
                default:
                throw new IllegalArgumentException(String.format("UserID '%s' is not implemented yet", userID));
        }
    }


    public Map<String, String> getUserData() {
        return userData;
    }

    public Map<String, String> getLoginData() {
        return new HashMap() {{
            put("email", userData.get("email"));
            put("password", userData.get("password"));
        }};
    }

    public void setLoginData(Map<String, String> loginData) {
        userData.put("email", loginData.get("email"));
        userData.put("password", loginData.get("password"));
    }

    public void addResponse(String action, Response response) {
        lastResponses.put(action, response);
    }

    public Response getResponse(String action) {
        return lastResponses.get(action);
    }

    public Response getCreateResponse() {
        return lastResponses.get("createResponse");
    }

    public Response getLoginResponse() {
        return lastResponses.get("loginResponse");
    }

    public Map<String, String> getCookiesMapFromLogin() {
        return lastResponses.get("loginResponse").cookies();
    }

    public Map<String, String> getHeadersMapFromLogin() {
        Map<String, String> headersMap = new HashMap();
        lastResponses.get("loginResponse").getHeaders().forEach(h -> {
            headersMap.put(h.getName(), h.getValue());
        });
        return headersMap;
    }

    public Headers getHeadersFromLogin() {
        return lastResponses.get("loginResponse").getHeaders();
    }

    public Map<String, String> getAuthCookie() {
        return new HashMap<String, String>() {{
            put("auth_sid", getCookiesMapFromLogin().get("auth_sid"));
        }};
    }

    public Map<String, String> getTokenHeader() {
        return new HashMap<String, String>() {{
            put("x-csrf-token", getHeadersMapFromLogin().get("x-csrf-token"));
        }};
    }

    public String getUserID() {
        return userID == null ? lastResponses.get("createResponse").body().jsonPath().get("id") : userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUserID() {
        this.userID = lastResponses.get("createResponse").body().jsonPath().get("id");
    }
 }
