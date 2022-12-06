package datamodel;

import java.util.HashMap;
import java.util.Map;

public class CookiesAndHeadersDataModel {
    private String testUrl;
    private String method;
    private String whatToTest;
    private Map<String, String> cookies = new HashMap();
    private Map<String, String> headers = new HashMap();


    public CookiesAndHeadersDataModel() {
    }

    public CookiesAndHeadersDataModel(String testUrl, String method, Map<String, String> cookies, Map<String, String> headers) {
        this.testUrl = testUrl;
        this.method = method;
        this.cookies = cookies;
        this.headers = headers;
    }

    public CookiesAndHeadersDataModel setTestUrl(String testUrl) {
        this.testUrl = testUrl;
        return this;
    }

    public String getTestUrl() {
        return this.testUrl;
    }

    public CookiesAndHeadersDataModel setMethod(String method) {
        this.method = method;
        return this;
    }

    public String getMethod() {
        return this.method;
    }

    public CookiesAndHeadersDataModel setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
        return this;
    }

    public Map<String, String> getCookies() {
        return this.cookies;
    }

    public CookiesAndHeadersDataModel setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public CookiesAndHeadersDataModel setWhatToTest(String whatToTest) {
        this.whatToTest = whatToTest;
        return this;
    }

    public String getWhatToTest() {
        return this.whatToTest;
    }
}