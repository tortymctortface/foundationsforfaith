package com.edee.foundationsforfaith.utils;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

class LocalizationUtils {
    public static ResourceBundle getBundle(Locale locale) {
        return ResourceBundle.getBundle("messages", locale);
    }

    public static String format(String key, Locale locale, Object... params) {
        ResourceBundle bundle = getBundle(locale);
        String pattern = bundle.getString(key);
        return MessageFormat.format(pattern, params);
    }
}