import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParsingJsonTest {

    private String urlToTest = "https://playground.learnqa.ru/api/get_json_homework";
    private String textOfSecondMessageExpected = "And this is a second message";
    private String messageIfFail = "Content is not correct";

    @Test
    public void parsingJsonTest() {
        JsonPath responseJson = RestAssured.get(urlToTest).jsonPath();
        //print the json response to study it
        responseJson.prettyPrint();
        //it was asked to print "the text of the second message" only
        String actualTextOfSecondMessage = responseJson.get("messages[1].message");
        System.out.println(actualTextOfSecondMessage);
        //assert since it has to be a test
        assertEquals(textOfSecondMessageExpected, actualTextOfSecondMessage, messageIfFail);
    }
}