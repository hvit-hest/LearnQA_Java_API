import datamodel.UserAgentDataModel;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static lib.Assertions.mapContains;

public class UserAgentTest {


    private Response responseForCheck;

    @ParameterizedTest
    @MethodSource("dataprovider.UserAgentProvider#provideTestData")
    public void userAgentTestTest(UserAgentDataModel testData) {
        RequestSpecification spec = RestAssured.given();
        spec.baseUri(testData.getTestUrl());
        spec.headers(testData.getHeaders());
        switch (testData.getMethod()) {
            case "get":
                //we have 'get' for the test only
                responseForCheck = spec.get();
                break;
            case "post":
                //we have 'get' for the test only but just in case
                responseForCheck = spec.post();
                break;
            default:
                throw new IllegalArgumentException(String.format("Method '%s' is not implemented yet", testData.getMethod()));
        }
        responseForCheck.prettyPrint();
        mapContains(responseForCheck.jsonPath().getMap("$"), testData.getExpectedValues());
    }
}