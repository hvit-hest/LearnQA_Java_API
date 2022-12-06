package dataprovider;

import datamodel.CookiesAndHeadersDataModel;

import java.util.HashMap;
import java.util.stream.Stream;

import static lib.DataGenerator.dateGenerator;

public class CookiesHeadersProvider {

    private static CookiesAndHeadersDataModel cookiesTestData = new CookiesAndHeadersDataModel()
            .setTestUrl("https://playground.learnqa.ru/api/homework_cookie")
            .setMethod("get")
            .setWhatToTest("cookies")
            .setCookies(new HashMap<String, String>() {{
                put("HomeWork", "hw_value");
            }});
    private static CookiesAndHeadersDataModel headersTestData = new CookiesAndHeadersDataModel()
            .setTestUrl("https://playground.learnqa.ru/api/homework_header")
            .setMethod("get")
            .setWhatToTest("headers")
            .setHeaders(new HashMap<String, String>() {{
                put("Date", dateGenerator());
                put("Content-Type", "application/json");
                put("Content-Length", "15");
                put("Connection", "keep-alive");
                put("Keep-Alive", "timeout=10");
                put("Server", "Apache");
                put("x-secret-homework-header", "Some secret value");
                put("Cache-Control", "max-age=0");
                put("Expires", dateGenerator());
            }});

    public static Stream<CookiesAndHeadersDataModel> provideTestData() {
        return Stream.of(cookiesTestData, cookiesTestData.setMethod("post"),
                headersTestData, headersTestData.setMethod("post"));
    }
}