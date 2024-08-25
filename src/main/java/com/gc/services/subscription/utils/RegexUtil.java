package com.gc.services.subscription.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegexUtil {

    private RegexUtil() {}

    public static boolean validatePhoneNumber(final String phoneNumber) {
        String regex = "^\\+[1-9]\\d{0,2}([ -]?\\(?\\d{1,4}\\)?[ -]?)\\d{1,9}([ -]?\\d{1,9})?$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);

        return matcher.matches();
    }
}
