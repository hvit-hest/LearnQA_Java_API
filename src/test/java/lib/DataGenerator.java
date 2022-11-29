package lib;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

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
}
