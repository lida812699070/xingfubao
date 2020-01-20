package com.careagle.sdk.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class PriceChangeUtils {

    //保留两位小数
    public static String getNumKb(double s) {
        NumberFormat formatter = new DecimalFormat("#,##0.00");
        formatter.setRoundingMode(RoundingMode.FLOOR);
        return formatter.format(s);
    }

    public static String getNumKbOne(double s) {
        NumberFormat formatter = new DecimalFormat("#,##0.#");
        formatter.setRoundingMode(RoundingMode.FLOOR);
        return formatter.format(s);
    }

    public static String getOneNumKb(double s) {
        NumberFormat formatter = new DecimalFormat("#,##0.0#");
        formatter.setRoundingMode(RoundingMode.FLOOR);
        return formatter.format(s);
    }

    //最少保留一位
    public static String getOneNumKb(float s) {
        NumberFormat formatter = new DecimalFormat("#,##0.0#");
        formatter.setRoundingMode(RoundingMode.FLOOR);
        return formatter.format(s);
    }

    //有就保留  没有就不保留
    public static String getDoubleKb(double s) {
        NumberFormat formatter = new DecimalFormat("###0.##");
        formatter.setRoundingMode(RoundingMode.FLOOR);
        return formatter.format(s);
    }

    //有就保留  没有就不保留
    public static String getNumKbs(double s) {
        NumberFormat formatter = new DecimalFormat("#,##0.##");
        formatter.setRoundingMode(RoundingMode.FLOOR);
        return formatter.format(s);
    }

    //有就保留  没有就不保留
    public static String getNumInteger(double s) {
        NumberFormat formatter = new DecimalFormat("#,##0");
        formatter.setRoundingMode(RoundingMode.FLOOR);
        return formatter.format(s);
    }

}
