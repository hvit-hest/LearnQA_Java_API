package lib;

import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class Assertions {

    public static void assertHeaderSSSByNameAndValue(Response response, Map<String, String> headersExpected) {
        Headers headersActual = response.getHeaders();
        //add-on to JUnit
        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(headersActual.size())
                .overridingErrorMessage(String.format("Expected number of headers: '%s' Actual number: '%s'",
                        headersExpected.size(), headersActual.size()))
                .isEqualTo(headersExpected.size());

        headersExpected.entrySet().forEach(header -> {
            String headerNameExpected = header.getKey();
            String headerValueExpected = header.getValue();

            softAssertions.assertThat(headersActual.hasHeaderWithName(headerNameExpected))
                    .overridingErrorMessage(String.format("Response doesn't have '%s' header", headerNameExpected))
                    .isTrue();

            if (headersActual.hasHeaderWithName(headerNameExpected)) {
                String headerValueActual = headersActual.get(headerNameExpected).getValue();

                if (headerNameExpected.equals("Date") || headerNameExpected.equals("Expires")) {
                    headerValueActual = truncateSeconds(headersActual.get(headerNameExpected).getValue());
                    headerValueExpected = truncateSeconds(headerValueExpected);
                }

                softAssertions.assertThat(headerValueActual)
                        .overridingErrorMessage(String.format("Header's '%s' value expected '%s' actual is '%s'",
                                headerNameExpected, headerValueExpected, headerValueActual))
                        .isEqualTo(headerValueExpected);
            }
        });

        softAssertions.assertAll();
    }

    public static void assertCookieSSSByNameAndValue(Response response, Map<String, String> cookiesExpected) {
        Map<String, String> cookiesActual = response.getCookies();
        //add-on to JUnit
        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(cookiesActual.size())
                .overridingErrorMessage(String.format("Expected number of cookies: '%s' Actual number: '%s'",
                        cookiesExpected.size(), cookiesActual.size()))
                .isEqualTo(cookiesExpected.size());

        cookiesActual.entrySet().stream().forEach(cookie -> {
            String cookieNameExpected = cookie.getKey();
            String cookieValueExpected = cookie.getValue();

            softAssertions.assertThat(cookiesActual.containsKey(cookieNameExpected))
                    .overridingErrorMessage(String.format("Response doesn't have '%s' cookie", cookieNameExpected))
                    .isTrue();

            if (cookiesActual.containsKey(cookieNameExpected))
            softAssertions.assertThat(cookiesActual.get(cookieNameExpected))
                        .overridingErrorMessage(String.format("'%s' cookie has wrong value '%s'",
                                cookieNameExpected, cookiesActual.get(cookieNameExpected)))
                        .isEqualTo(cookieValueExpected);
        });
        softAssertions.assertAll();
    }

    private static String truncateSeconds(String dateToTruncate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm:ss z", Locale.US);
        //without minutes and seconds
        return ZonedDateTime.from(formatter.parse(dateToTruncate)).truncatedTo(ChronoUnit.MINUTES).toString();
    }

    //Simple, one cookie assertion -just template for future
    public static void assertCookieByNameAndValue(Response response, String cookieNameExpected, String cookieValueExpected) {

        Map<String, String> cookiesActual = response.getCookies();
        assertTrue(cookiesActual.containsKey(cookieNameExpected),
                String.format("Response doesn't have '%s' cookie", cookieNameExpected));
        assertEquals(cookieValueExpected, cookiesActual.get(cookieNameExpected),
                String.format("'%s' cookie has wrong value", cookieNameExpected));
    }

    //Simple, one header assertion  -just template for future
    public static void assertHeaderByNameAndValue(Response response, String headerNameExpected, String headerValueExpected) {
        Headers headersActual = response.getHeaders();

        assertTrue(headersActual.hasHeaderWithName(headerNameExpected),
                String.format("Response doesn't have '%s' header", headerNameExpected));

        assertEquals(headerValueExpected, headersActual.get(headerNameExpected).getValue(),
                String.format("Header value expected '%s' actual is '%s'",
                        headerValueExpected, headersActual.get(headerNameExpected).getValue()));
    }

    public static void mapContains(Map<String, String> actualMap, Map<String, String> expectedMap) {
        String assertMessageIfFailToCompare = "Actual value '%s'does not equal expected value '%s' for key '%s'";
        String assertMessageIfKeyIsAbsent = "Key '%s' is not found";
        //add-on to JUnit
        SoftAssertions softAssertions = new SoftAssertions();
        expectedMap.entrySet().forEach(entry -> {
            String expectedKey = entry.getKey();
            String expectedValue = entry.getValue();

            softAssertions.assertThat(actualMap.containsKey(expectedKey))
                    .overridingErrorMessage(String.format(assertMessageIfKeyIsAbsent, expectedKey))
                    .isTrue();

            if (actualMap.containsKey(expectedKey))
            softAssertions.assertThat(actualMap.get(expectedKey))
                    .overridingErrorMessage(String.format(assertMessageIfFailToCompare,
                            actualMap.get(expectedKey), expectedValue, expectedKey))
                    .isEqualTo(expectedValue);
        });
        softAssertions.assertAll();
    }
}
