package iti.mad.gusto.presentation.common.util;
import java.util.regex.Pattern;


public class ValidationUtil {

    private static final String EMAIL_PATTERN =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]{2,}$";

    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*.]).{6,}$";

    private static final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
    private static final Pattern passwordPattern = Pattern.compile(PASSWORD_PATTERN);

    public static boolean isValidEmail(String email) {
        return isNotNullOrEmpty(email) && emailPattern.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return isNotNullOrEmpty(password) && password.length() >= 6 && passwordPattern.matcher(password).matches();
    }

    public static boolean isNotNullOrEmpty(String text) {
        return text != null && !text.trim().isEmpty();
    }
}