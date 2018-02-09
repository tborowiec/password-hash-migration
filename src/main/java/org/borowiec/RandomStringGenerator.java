package org.borowiec;

import java.security.SecureRandom;
import java.util.Random;

public final class RandomStringGenerator {

    private static final String LOWER_CASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER_CASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";

    private static final String ALPHANUMERIC = LOWER_CASE_LETTERS + UPPER_CASE_LETTERS + DIGITS;

    private static final Random random = new SecureRandom();

    private RandomStringGenerator() {
        /* intentionally empty */
    }

    public static String generateRandomAlphanumeric(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("Invalid length provided " + length + ". Must be greater than 0.");
        }

        char[] result = new char[length];
        for (int i = 0; i < length; ++i) {
            result[i] = generateChar(ALPHANUMERIC);
        }

        return new String(result);
    }

    private static char generateChar(String characterSet) {
        int randomIndex = random.nextInt(characterSet.length());
        return characterSet.charAt(randomIndex);
    }

}
