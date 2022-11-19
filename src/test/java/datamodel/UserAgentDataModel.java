package datamodel;

import java.util.Map;

public class UserAgentDataModel {
    private String testUrl;
    private String method;
    private Map<String, String> headers;
    private Map<String, String> expectedValues;

    public String getTestUrl() { return testUrl; }
    public String getMethod() { return method; }
    public Map<String, String> getHeaders() { return headers;};
    public Map<String, String> getExpectedValues() { return expectedValues; }
}

/*    "urlToTest": "https://playground.learnqa.ru/ajax/api/user_agent_check",
            "method": "get",
            "userAgent": "Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30",
            "expectedValues": {
            "platform": "Mobile",
            "browser": "No",
            "device": "Android"*/
