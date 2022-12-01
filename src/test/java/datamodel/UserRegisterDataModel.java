package datamodel;

import java.util.Map;

public class UserRegisterDataModel {
    private String testID;
    private String testDescription;
    private String testType;
    private String requestDescription;
    private String testUrl;
    private String method;
    private String[] whatToTest;
    private Map<String, String> headers;
    private Map<String, String> cookies;
    private Map<String, String> userData;
    private Map<String, String> expectedValues;

    public String getTestID() {
        return testID;
    }

    public String getTestUrl() {
        return testUrl;
    }

    public String getTestDescription() {
        return testDescription;
    }

    public String getMethod() {
        return method;
    }

    public String getTestType() {
        return testType;
    }

    public String[] getWhatToTest() {
        return whatToTest;
    }

    public String getRequestDescription() {
        return requestDescription;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    ;

    public Map<String, String> getCookies() {
        return cookies;
    }

    ;

    public Map<String, String> getUserData() {
        return userData;
    }

    ;

    public Map<String, String> getExpectedValues() {
        return expectedValues;
    }

    @Override
    public String toString() {
        return String.format("TestID: '%s'. Description: '%s'", testID, testDescription);
    }
}

/*[
        {
        "testID": "0001_createUserWrongEmailNegative",
        "testDescription": "User with incorrect email/without @ can not be created. Email ''",
        "testType": "negative",
        "requestDescription": ""
        "testUrl": "https://playground.learnqa.ru/api/user/",
        "method": "post",
        "userData": {
        "email": "",
        "password": "generate",
        "username": "generate",
        "firstName": "generate",
        "lastName": "generate"
        },
        "whatToTest": ["textMessageInBody", "responseCode"],
        "expectedValues": {
        "textMessageInBody": "The value of 'email' field is too short"
        "responseCode": 400
        }*/
