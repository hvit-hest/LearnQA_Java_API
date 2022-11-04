import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CheckTextContent {

    private String urlToTest = "https://playground.learnqa.ru/api/get_text";
    private String textExpected = "Hello, world";
    private String messageIfFail = "Content is not correct";

    @Test
    public void checkTextContent() {
        Response response = RestAssured.get(urlToTest).andReturn();
        //print the response
        response.prettyPrint();
        //print a text in the body of the response (since it was asked about "text content" in the 4th assignment)
        String actualContent = response.htmlPath().getString("html.body");
        System.out.println(actualContent);
        //assert since it has to be a test
        assertEquals(textExpected, actualContent, messageIfFail);
    }
}
