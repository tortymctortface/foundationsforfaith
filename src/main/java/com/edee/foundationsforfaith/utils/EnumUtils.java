package com.edee.foundationsforfaith.utils;

import java.util.Arrays;

public class EnumUtils {

    public static boolean isEnumValue(String value, Class<? extends Enum> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                .anyMatch(constant -> constant.name().equals(value));
    }
}
