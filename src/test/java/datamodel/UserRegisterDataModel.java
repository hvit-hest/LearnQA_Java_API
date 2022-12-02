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

    public Map<String, String> getCookies() {
        return cookies;
    }
    public Map<String, String> getUserData() {
        return userData;
    }
    public Map<String, String> getExpectedValues() {
        return expectedValues;
    }

    public void setUserData(Map<String, String> userData) {
        this.userData = userData;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public void setTestUrl(String testUrl) {
        this.testUrl = testUrl;
    }

    @Override
    public String toString() {
        return String.format("TestID: '%s'. Description: '%s'", testID, testDescription);
    }
}