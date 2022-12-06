package lib;

import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DataGenerator {

/*    //Java 6 style
    public String dateGenerator()() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
    }*/

    //Java 8 style
    public static String dateGenerator() {
        return ZonedDateTime.now(TimeZone.getTimeZone("GMT").toZoneId())
                .format(DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm:ss z", Locale.US));
    }

    public static String dateGenerator(String pattern) {
        return ZonedDateTime.now(TimeZone.getTimeZone("GMT").toZoneId())
                .format(DateTimeFormatter.ofPattern(pattern, Locale.US));
    }

    public static String getRandomEmail() {
        String timeStamp = dateGenerator("EEEdMMMyyyyHHmmssSSSz");
        return String.format("learnqa%s@example.com", timeStamp);
    }

    public static Map<String, String> getRegistrationData() {
        return new HashMap<String, String>() {{
            put("email", getRandomEmail());
            put("password", "123");
            put("username", "learnqa");
            put("firstName", "learnqa");
            put("lastName", "learnqa");
        }};
    }

    public static Map<String, String> getRegistrationData(Map<String, String> nonDefaultValues) {
        Map<String, String> userData = getRegistrationData();
/*        userData.keySet().forEach(key -> {
            if (nonDefaultValues.containsKey(key))
                userData.put(key, nonDefaultValues.get(key));
        });*/
        for (String key : userData.keySet()) {
            if ((nonDefaultValues.containsKey(key)))
                userData.put(key, nonDefaultValues.get(key));
        }
        return userData;
    }

    public static Map<String, String> getRegistrationDataAlt(Map<String, String> userData) {
        for (Map.Entry<String, String> entry : userData.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();;
            switch (value) {
                    case "generateUserName":
                        userData.put(key, generateUserName());
                        break;
                    case "generateFirstName":
                        userData.put(key, randomFirstName());
                        break;
                    case "generateLastName":
                        userData.put(key, randomLastName());
                        break;
                    case "generateEmail":
                        userData.put(key, randomEmail());
                        break;
                    case "generateLongString251":
                        //251 - inclusive
                        userData.put(key, RandomStringUtils.randomAlphanumeric(251));
                        break;
                    case "generatePassword":
                        userData.put(key, generatePassword());
                        break;
                }
         }
        userData.entrySet().forEach(entry -> System.out.println("key: " + entry.getKey() + " value: " + entry.getValue()));
        return userData;
    }

    public static String randomEmail() {
        return String.format("%s@%s.%s",  getUniqueIdLight(),
                RandomStringUtils.randomAlphabetic(1,10),
                RandomStringUtils.randomAlphabetic(1,7));
    }

    public static String randomFirstName() {
        Faker faker = new Faker();
        return faker.name().firstName();
    }

    public static String randomLastName() {
        Faker faker = new Faker();
        return faker.name().lastName();
    }

    private static String getUniqueId() {
        return String.format("%s_%s", UUID.randomUUID().toString().substring(0, 5), System.currentTimeMillis());
    }
    private static String getUniqueIdLight() {
        return String.format("%s_%s", randomFirstName(), System.currentTimeMillis());
    }

    public static String generatePassword() {
        //2 -inclusive,251 -exclusive
        return RandomStringUtils.randomAlphanumeric(2,251);
    }

    public static String generateUserName() {
        //2 -inclusive,251 -exclusive
        return RandomStringUtils.randomAlphanumeric(2,251);
    }
}