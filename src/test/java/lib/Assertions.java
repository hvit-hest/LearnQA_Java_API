package lib;

import datamodel.UserRegisterDataModel;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
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

    public static void mapContains(Response response, String whereToGetActualMap, Map<String, String> expectedMap, boolean compareSize) {
        String assertMessageIfFailToCompare = "Actual value '%s'does not equal expected value '%s' for key '%s'";
        String assertMessageIfKeyIsAbsent = "Key '%s' is not found";
        String assertMessageIfSizeIsDifferent = "Actual map size '%s' vs. expected map size '%s'";
        Map<String, String> actualMap = response.body().jsonPath().getMap(whereToGetActualMap);

        //add-on to JUnit
        SoftAssertions softAssertions = new SoftAssertions();

        if (compareSize) {
            softAssertions.assertThat(actualMap.size() == expectedMap.size())
                    .overridingErrorMessage(String.format(assertMessageIfSizeIsDifferent, actualMap.size(), expectedMap.size()))
                    .isTrue();
        }

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

    public static void mapContains(Map<String, String> actualMap, Map<String, String> expectedMap, boolean compareSize) {
        String assertMessageIfFailToCompare = "Actual value '%s'does not equal expected value '%s' for key '%s'";
        String assertMessageIfKeyIsAbsent = "Key '%s' is not found";
        String assertMessageIfSizeIsDifferent = "Actual map size '%s' vs. expected map size '%s'";

        //add-on to JUnit
        SoftAssertions softAssertions = new SoftAssertions();

        if (compareSize) {
            softAssertions.assertThat(actualMap.size() == expectedMap.size())
                    .overridingErrorMessage(String.format(assertMessageIfSizeIsDifferent, actualMap.size(), expectedMap.size()))
                    .isTrue();
        }

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

    public static void assertJsonByName(Response response, String name, int expectedValue) {
        response.then().assertThat().body("$", hasKey(name));
        int value = response.jsonPath().getInt(name);
        assertEquals(expectedValue, value, "JSON value is not equal to expected value");
    }

    public static void assertJsonByName(Response response, String name, String expectedValue) {
        response.then().assertThat().body("$", hasKey(name));
        String value = response.jsonPath().getString(name);
        assertEquals(expectedValue, value, "JSON value is not equal to expected value");
    }

    public static void assertResponseTextEquals(Response response, String expectedAnswer) {
        assertEquals(expectedAnswer, response.asString(),
                String.format("Response is different. Expected '%s' vs. Actual '%s'", expectedAnswer, response.asString()));
    }

    public static void assertResponseCodeEquals(Response response, int expectedStatusCode) {
        assertEquals(expectedStatusCode, response.statusCode(),
                String.format("Status Code is different. Expected '%s' vs. Actual '%s'", expectedStatusCode, response.statusCode()));
    }

    public static void assertJsonHasField(Response response, String expectedFieldName) {
        response.then().assertThat().body("$", hasKey(expectedFieldName));
    }

    public static void assertJsonHasFields(Response response, String[] expectedFieldNames) {
        for (String expectedFieldName : expectedFieldNames) {
            assertJsonHasField(response, expectedFieldName);
        }
    }

    public static void assertJsonHasNotField(Response response, String unexpectedFieldName) {
        response.then().assertThat().body("$", not(hasKey(unexpectedFieldName)));
    }

    public static void assertResponse(Response response, UserRegisterDataModel testData) {
        if (testData.getWhatToTest() != null && testData.getWhatToTest().length != 0) {
            //add-on to JUnit
            SoftAssertions softAssertions = new SoftAssertions();
            Arrays.stream(testData.getWhatToTest()).forEach(s -> {
                //WhatToTest - array from json
                switch (s) {
                    case "textMessageInBody":
             /* Try to use it one day
             response.then().assertThat().body(Matchers.hasXPath(String.format("//html/body[text()='%s']",testData.getExpectedValues().get(s))));
            response.then().assertThat().body(Matchers.hasXPath("//html/body", containsString(testData.getExpectedValues().get(s))));*/

                        String bodyText = response.getBody().htmlPath().getString("//html/body");
                        softAssertions.assertThat(!bodyText.equals("null") ? bodyText: null)
                                .isEqualTo(testData.getExpectedValues().get(s));
                        break;
                     case "responseCode":
                        softAssertions.assertThat(response.statusCode())
                                .isEqualTo(Integer.parseInt(testData.getExpectedValues().get(s)));
                        break;
                    case "hasFieldNames":
                        String [] fieldsToCheck =  testData.getExpectedValues().get(s)
                                .replaceAll("\\s*,\\s*",",").split(",");
                        for (String expectedFieldName : fieldsToCheck) {
                            softAssertions.assertThat(response.jsonPath().getMap("$").containsKey(expectedFieldName))
                                    .overridingErrorMessage(String.format("Field '%s' not found", expectedFieldName))
                                    .isTrue();
                        }
                        break;
                    case "numberOfFieldsTheSame":
                        int expectedNumberOfFields =  testData.getExpectedValues().get("hasFieldNames")
                                .replaceAll("\\s*,\\s*",",").split(",").length;
                        int actualNumberOfFields = response.jsonPath().getMap("$").size();
                        softAssertions.assertThat(actualNumberOfFields)
                        .overridingErrorMessage(String.format("Actual number of fields '%s' vs. expected '%s'",
                                actualNumberOfFields, expectedNumberOfFields))
                        .isEqualTo(expectedNumberOfFields);
                        break;
                    case "username":
                        String userNameActual = response.jsonPath().get("username");
                        String userNameExpected = testData.getExpectedValues().get("username");
                        softAssertions.assertThat(userNameActual)
                                .overridingErrorMessage(String.format("Actual username '%s' vs. expected '%s'",
                                        userNameActual, userNameExpected))
                                .isEqualTo(userNameExpected);
                        break;
                    default:
                        throw new IllegalArgumentException(String.format("Test '%s' is not implemented yet", testData.getExpectedValues().get(s)));
                }
            });
            softAssertions.assertAll();
        }
    }
 }