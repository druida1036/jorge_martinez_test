package com.cache;

import java.util.Optional;

public class NumberComparator {


    public static final String GREATER = "greater";
    public static final String MESSAGE = "[%s] is %s than [%s]";
    public static final String LESS = "less";
    public static final String EQUAL = "equal";

    public String compare(String s1, String s2) {
        double d1 = toDouble(s1);
        double d2 = toDouble(s2);
        if (d1 == d2) {
            return String.format(MESSAGE, s1, EQUAL, s2);
        }
        if (d1 > d2) {
            return String.format(MESSAGE, s1, GREATER, s2);

        }

        return String.format(MESSAGE, s2, LESS, s1);

    }

    private static double toDouble(String s1) {
        return Optional.ofNullable(s1).map(Double::parseDouble).orElseThrow(() -> {
            throw new IllegalArgumentException();
        });
    }
}
