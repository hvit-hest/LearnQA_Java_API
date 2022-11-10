import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RedirectTest {

    private String urlToTest = "https://playground.learnqa.ru/api/long_redirect";
    private String messageIfFail = "URL redirection is not correct";
    String firstLocationHeaderExpected = "https://playground.learnqa.ru/";

    @Test
    public void redirectTest() {

        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get(urlToTest)
                .andReturn();

        //In 6th assignment it was asked to print location header for GET with given URL "https://playground.learnqa.ru/api/long_redirect" only
        String firstLocationHeaderActual = response.getHeader("location");
        System.out.println(firstLocationHeaderActual);

        //assert since it has to be a test
        assertEquals(firstLocationHeaderExpected, firstLocationHeaderActual, messageIfFail);
    }
}