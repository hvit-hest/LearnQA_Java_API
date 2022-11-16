package dataprovider;
import org.apache.commons.lang3.RandomStringUtils;
import java.util.stream.Stream;

public class StringProvider {

    public static Stream<String> stringProvider() {
        return Stream.of(null, RandomStringUtils.randomAlphabetic(0,15), RandomStringUtils.randomAlphabetic(16,255));
    }
}