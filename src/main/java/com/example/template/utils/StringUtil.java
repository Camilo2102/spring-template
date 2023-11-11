package com.example.template.utils;

public class StringUtil {
    public static String getFromTemplate(String template, String... args) {
        return String.format(template, args);
    }

    public static String firstUpperLetter(String value){
        return value.substring(0, 1).toUpperCase() + value.substring(1);
    }
}
