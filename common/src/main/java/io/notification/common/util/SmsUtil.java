package io.notification.common.util;

import java.util.regex.Pattern;

public class SmsUtil {
    private static final String E164_REGEX = "^\\+[1-9]\\d{1,14}$";
    private static final Pattern PHONE_PATTERN = Pattern.compile(E164_REGEX);

    private static boolean isValidE164(String phone) {
        if (phone == null) {
            return false;
        }
        String cleanPhone = phone.replaceAll("[\\s\\-\\(\\)]", "");

        return PHONE_PATTERN.matcher(cleanPhone).matches();
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank() || phoneNumber.isEmpty())
            return false;
        return isValidE164(phoneNumber);
    }
}