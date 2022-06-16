package praktikum;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Locale;

public class UserGeneration {

    public UserGeneration() {
    }

    public String randomEmail() {
        String domain = RandomStringUtils.randomAlphabetic(4).toLowerCase(Locale.ROOT);
        String email = RandomStringUtils.randomAlphabetic(6).toLowerCase(Locale.ROOT);
        return email + "@" + domain + ".com";
    }

    public String randomName() {
        String firstLetter = RandomStringUtils.randomAlphabetic(1).toUpperCase(Locale.ROOT);
        String randomString = RandomStringUtils.randomAlphabetic(5).toLowerCase(Locale.ROOT);
        return firstLetter.concat(randomString);    }

    public String randomPassword() {
        return RandomStringUtils.randomAscii(6);
    }

}
