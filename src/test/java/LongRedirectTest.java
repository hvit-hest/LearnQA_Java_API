import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LongRedirectTest {

    private String urlToTest = "https://playground.learnqa.ru/api/long_redirect";
    private String messageIfFail = "Location Header is not correct";
    private String lastLocationHeaderExpected = "https://www.learnqa.ru/";
    private Response response;

    @Test
    public void longRedirectTest() {
        int responseCodeActual = -1;
        String lastLocationHeaderActual = null;

        do {
            response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(urlToTest)
                    .andReturn();
            responseCodeActual = response.statusCode();
            urlToTest = response.getHeader("location");
            if (urlToTest != null) {
                lastLocationHeaderActual = urlToTest;
                System.out.println(urlToTest);
            }
        }
        //Better to use with a counter in case we never get the 200 code
        while (responseCodeActual != 200);

        //assert since it has to be a test
        assertEquals(lastLocationHeaderExpected, lastLocationHeaderActual, messageIfFail);
    }
}