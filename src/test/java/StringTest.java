import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class StringTest {

    String assertMessage = "The string is '%s'. Length of the string has to be more than 15 symbols";

    @ParameterizedTest
    @MethodSource("dataprovider.StringProvider#stringProvider")
    public void stringTest(String stringToTest) {

        assertTrue(stringToTest != null && stringToTest.length() > 15,
                String.format(assertMessage, stringToTest));
    }
}