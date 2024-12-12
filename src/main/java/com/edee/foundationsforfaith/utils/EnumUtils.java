package com.edee.foundationsforfaith.utils;

import com.edee.foundationsforfaith.enums.ProjectType;

import java.util.Arrays;

public class EnumUtils {

    public static ProjectType getProjectType(String projectType){
        return (projectType == null) || !(EnumUtils.isEnumValue(projectType, ProjectType .class))
                ? ProjectType.UNKNOWN
                : Enum.valueOf(ProjectType.class, projectType);
    }

    public static boolean isEnumValue(String value, Class<? extends Enum> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                .anyMatch(constant -> constant.name().equals(value));
    }
}