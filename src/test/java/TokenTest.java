import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

public class TokenTest {

    private String urlToTest = "https://playground.learnqa.ru/ajax/api/longtime_job";
    private String errorMessageExpected = "No job linked to this token";
    private String statusReadyMessageExpected = "Job is ready";
    private String statusNotReadyMessageExpected = "Job is NOT ready";
    private String statusAssertMessageIfFail = "Status is not correct";
    private String resultAssertMessageIfFail = "Result is absent";
    private String errorAssertMessageIfFail = "Error message is not correct";


    @Test
    public void tokenTest() {
        //assertj is JUnit add-on
        SoftAssertions softAssertions = new SoftAssertions();

        //Check 'error' without task. Just in case.

        JsonPath responseJsonWithoutTask = RestAssured
                .given()
                .queryParam("token", "Yo ho ho and a bottle of rum")
                .when()
                .get(urlToTest)
                .andReturn()
                .jsonPath();
        //print the json response to study it
        responseJsonWithoutTask.prettyPrint();
        //assert since it has to be a test
        softAssertions.assertThat(responseJsonWithoutTask.get("error").toString())
                .overridingErrorMessage(errorAssertMessageIfFail)
                .isEqualTo(errorMessageExpected);

        //Create task

        JsonPath responseJsonCreateTask = RestAssured.get(urlToTest).jsonPath();
        //print the json response to study it
        responseJsonCreateTask.prettyPrint();
        int secondsToWait = responseJsonCreateTask.get("seconds");
        String token = responseJsonCreateTask.get("token");
        System.out.println(String.format("Seconds to wait '%s' and token to use '%s'", secondsToWait, token));

        //When task is not ready: check 'status' is correct

        JsonPath responseJsonNotReadyTask = RestAssured
                .given()
                .queryParam("token", token)
                .when()
                .get(urlToTest)
                .andReturn()
                .jsonPath();
        //print the json response to study it
        responseJsonNotReadyTask.prettyPrint();
        //assert since it has to be a test
        softAssertions.assertThat(responseJsonNotReadyTask.get("status").toString())
                .overridingErrorMessage(statusAssertMessageIfFail)
                .isEqualTo(statusNotReadyMessageExpected);

        //When task is ready: 1. check 'status' is correct 2. check 'result' is present

        try {
            Thread.sleep(secondsToWait * 1000 + 500);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        JsonPath responseJsonReadyTask = RestAssured
                .given()
                .queryParam("token", token)
                .get(urlToTest)
                .andReturn()
                .jsonPath();

        //print the json response to study it
        responseJsonReadyTask.prettyPrint();
        //asserts since it has to be a test
        softAssertions.assertThat(responseJsonReadyTask.getString("status"))
                .overridingErrorMessage(statusAssertMessageIfFail)
                .isEqualTo(statusReadyMessageExpected);
        softAssertions.assertThat(responseJsonReadyTask.getString("result"))
                .overridingErrorMessage(resultAssertMessageIfFail)
                .isNotNull();
        //assertj is JUnit add-on
        softAssertions.assertAll();
    }
}