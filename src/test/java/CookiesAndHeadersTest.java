import datamodel.DataModel;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static lib.Assertions.assertCookieSSSByNameAndValue;
import static lib.Assertions.assertHeaderSSSByNameAndValue;


public class CookiesAndHeadersTest {


    private Response responseForCheck;

    @ParameterizedTest
    @MethodSource("dataprovider.CookiesHeadersProvider#provideTestData")
    public void checkCookiesAndHeadersTest(DataModel testData) {
        RequestSpecification spec = RestAssured.given();
        spec.baseUri(testData.getTestUrl());
        switch (testData.getMethod()) {
            case "get":
                responseForCheck = spec.get();
                break;
            case "post":
                responseForCheck = spec.post();
                break;
            default:
                throw new IllegalArgumentException(String.format("Method '%s' is not implemented yet", testData.getMethod()));
        }

        switch (testData.getWhatToTest()) {
            case "cookies":
                assertCookieSSSByNameAndValue(responseForCheck, testData.getCookies());
                break;
            case "headers":
                assertHeaderSSSByNameAndValue(responseForCheck, testData.getHeaders());
                break;
            default:
                throw new IllegalArgumentException(String.format("'%s' is not implemented yet", testData.getWhatToTest()));
        }
    }
}

