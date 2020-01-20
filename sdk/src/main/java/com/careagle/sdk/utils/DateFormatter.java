package com.careagle.sdk.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Calendar;

public enum DateFormatter {
    NORMAL("yyyy-MM-dd HH:mm"),
    DD("yyyy-MM-dd"),
    MM("yyyy-MM"),
    SS("yyyy-MM-dd HH:mm:ss"),
    NOYY("MM-dd HH:mm"),
    TIME("HH:mm:ss");
    private String value;

    DateFormatter(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 != null && cal2 != null) {
            return cal1.get(0) == cal2.get(0) && cal1.get(1) == cal2.get(1) && cal1.get(6) == cal2.get(6);
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    public static boolean isSameDay(@Nullable java.util.Date date2, @NotNull java.util.Date date1) {
        if (date1 != null && date2 != null) {
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date2);
            return isSameDay(cal1, cal2);
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }
}