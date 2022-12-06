package lib;

import datamodel.User;
import datamodel.UserRegisterDataModel;
import dataprovider.UrlMap;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
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

    @Step("Make a GET-request with headers(token) and cookies (auth cookie)")
    public Response makeGetRequest(String url, Map<String, String> headers, Map<String, String> cookies) {
        return given()
                .filter(new AllureRestAssured())
                .headers(headers)
                .cookies(cookies)
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

    @Step("Make a DELETE-request")
    public Response makeDeleteRequest(String url, Map<String, String> headers, Map<String, String> cookies) {
        return given()
                .filter(new AllureRestAssured())
                .cookies(cookies)
                .headers(headers)
                .delete(url)
                .andReturn();
    }

    @Step("Make a PUT-request")
    public Response makePutRequest(String urlEditedUser, Map<String, String> dataToEdit,
                                   Map<String, String> headers, Map<String, String> cookies) {
    return  RestAssured
            .given()
            .headers(headers)
            .cookies(cookies)
            .body(dataToEdit)
            .put(urlEditedUser)
            .andReturn();
}
    @Step("Make a PUT-request w/o authorization")
    public Response makePutRequest(String urlEditedUser, Map<String, String> dataToEdit) {
        return  RestAssured
                .given()
                .body(dataToEdit)
                .put(urlEditedUser)
                .andReturn();
    }

    //String requestDescription -> {1}
    @Step("{1}")
    public Response requestGenerator(UserRegisterDataModel testData, String requestDescription) {
        Response response;
        RequestSpecification reqspec = RestAssured.given().filter(new AllureRestAssured());

        if (testData.getCookies() != null) reqspec.cookies(testData.getCookies());
        if (testData.getHeaders() != null) reqspec.headers(testData.getHeaders());
        if (testData.getUserData() != null) {
            Map<String, String> userDataGenerated = DataGenerator.getRegistrationDataAlt(testData.getUserData());
            testData.setUserData(userDataGenerated);
            reqspec.body(userDataGenerated);
        }

        switch (testData.getMethod()) {
            case "get":
                response = reqspec.get(testData.getTestUrl());
                break;
            case "post":
                response = reqspec.post(testData.getTestUrl());
                break;
            case "put":
                response = reqspec.put(testData.getTestUrl());
                break;
            case "delete":
                response = reqspec.delete(testData.getTestUrl());
                break;
            default:
                throw new IllegalArgumentException(String.format("Method '%s' is not implemented yet", testData.getMethod()));
        }
        return response;
    }

    @Step("Create user")
    public User createUser() {
        String urlCreateUser = UrlMap.URLS.get("create")[0];
        User user = new User();
        user.addResponse("createResponse", makePostRequest(urlCreateUser, user.getUserData()));
        return user;
    }

    @Step("Login user")
    public User loginUser(User user) {
        String urlLoginUser = UrlMap.URLS.get("login")[0];
        Response response = makePostRequest(urlLoginUser, user.getLoginData());
        user.addResponse("loginResponse", response);
        return user;
    }

    @Step("Delete user")
    public User deleteUser(User user) {
        String urlDeleteUser = String.format(UrlMap.URLS.get("delete")[0], user.getUserID());
        Response response = makeDeleteRequest(urlDeleteUser, user.getTokenHeader(),  user.getAuthCookie());
        user.addResponse("deleteResponse", response);
        return user;
    }

    @Step("Delete user using auth cookie and token of another user")
    public User deleteUser1ByUser2(User userToDetete, User userWhoDeletes) {
        String urlDeleteUser = String.format(UrlMap.URLS.get("delete")[0], userToDetete.getUserID());
        Response response = makeDeleteRequest(urlDeleteUser, userWhoDeletes.getTokenHeader(),  userWhoDeletes.getAuthCookie());
        userWhoDeletes.addResponse("deleteResponse", response);
        return userWhoDeletes;
    }

    @Step("Get user data")
    public User getUser(User user) {
        String urlGetUser = String.format(UrlMap.URLS.get("getData")[0], user.getUserID());
        Response response = makeGetRequest(urlGetUser, user.getTokenHeader(),  user.getAuthCookie());
        user.addResponse("getResponse", response);
        return user;
    }

    @Step("Edit user data")
    public User editUser(User user, Map<String, String> dataToChange) {
        String urlEditedUser = String.format(UrlMap.URLS.get("edit")[0], user.getUserID());
        Response response = makePutRequest(urlEditedUser, dataToChange, user.getTokenHeader(),  user.getAuthCookie());
        user.addResponse("putResponse", response);
        return user;
    }

    @Step("Try to edit user using auth cookie and token of another user")
    public User editUser1ByUser2(User userToEdit, User userWhoEdits, Map<String, String> dataToChange) {
        String urlEditedUser = String.format(UrlMap.URLS.get("edit")[0], userToEdit.getUserID());
        Response response = makePutRequest(urlEditedUser, dataToChange, userWhoEdits.getTokenHeader(),  userWhoEdits.getAuthCookie());
        userWhoEdits.addResponse("putResponse", response);
        return userWhoEdits;
    }

    @Step("Try to edit user data w/o authorization")
    public User editUserWithoutAuth(User user, Map<String, String> dataToChange) {
        String urlEditedUser = String.format(UrlMap.URLS.get("edit")[0], user.getUserID());
        Response response = makePutRequest(urlEditedUser, dataToChange);
        user.addResponse("putResponse", response);
        return user;
    }

    @Step("Try to get user data by another user")
    public User getUser1ByUser2(User userToGet, User userWhoGets) {
        String urlUserToGet = String.format(UrlMap.URLS.get("getData")[0], userToGet.getUserID());
        Response response = makeGetRequest(urlUserToGet, userWhoGets.getTokenHeader(), userWhoGets.getAuthCookie());
        userWhoGets.addResponse("getResponse", response);
        return userWhoGets;
    }
}
