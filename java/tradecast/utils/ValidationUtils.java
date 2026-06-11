package tradecast.utils;

import java.util.regex.Pattern;

public final class ValidationUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[\\w!#$%&'*+/=?^`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^`{|}~-]+)*@"
                    + "(?:[\\w-]+\\.)+[A-Za-z]{2,}$");

    private ValidationUtils() {
    }

    public static boolean isNullOrBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        if (isNullOrBlank(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
}
