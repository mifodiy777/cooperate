package com.cooperate.comparator;

import com.cooperate.entity.Garag;

import java.util.Comparator;
import java.util.regex.Pattern;

public class GaragNumberComparator implements Comparator<Garag> {
    
     private static final Pattern PATTERN = Pattern.compile("(\\D*)(\\d*)");

    @Override
    public int compare(Garag g1, Garag g2) {

        boolean isText1;
        boolean isText2;
        Integer series1 = 0;
        Integer series2 = 0;
        if (g1.getSeries().isEmpty()) {
            return -1;
        }
        if (g2.getSeries().isEmpty()) {
            return 1;
        }
        if (g1.getSeries().isEmpty() && g2.getSeries().isEmpty()) {
            return 0;
        }
        if (g1.getSeries().matches("\\d+")) {
            series1 = Integer.parseInt(g1.getSeries());
            isText1 = false;
        } else {
            isText1 = true;
        }
        if (g2.getSeries().matches("\\d+")) {
            series2 = Integer.parseInt(g2.getSeries());
            isText2 = false;
        } else {
            isText2 = true;
        }
        if (isText1 && isText2) {
            return g1.getSeries().compareToIgnoreCase(g2.getSeries());
        }
        if (isText1 && !isText2) {
            return 1;
        }
        if (!isText1 && isText2) {
            return -1;
        }

        if (series1 < series2) {
            return -1;
        } else if (series1 > series2) {
            return 1;
        } else if (series1.equals(series2)) {

            boolean isTextNumber1;
            boolean isTextNumber2;
            Integer number1 = 0;
            Integer number2 = 0;

            if (g1.getNumber().isEmpty()) {
                return -1;
            }
            if (g2.getNumber().isEmpty()) {
                return 1;
            }
            if (g1.getNumber().isEmpty() && g2.getNumber().isEmpty()) {
                return 0;
            }
            if (g1.getNumber().matches("\\d+")) {
                number1 = Integer.parseInt(g1.getNumber());
                isTextNumber1 = false;
            } else {
                isTextNumber1 = true;
            }
            if (g2.getNumber().matches("\\d+")) {
                number2 = Integer.parseInt(g2.getNumber());
                isTextNumber2 = false;
            } else {
                isTextNumber2 = true;
            }

            if (!isTextNumber1 && !isTextNumber2) {
                if (number1 < number2) {
                    return -1;
                } else if (number1 > number2) {
                    return 1;
                } else {
                    return 0;
                }
            }
            if (isTextNumber1 && isTextNumber2) {
                return g1.getNumber().compareToIgnoreCase(g2.getNumber());
            }
            if (isTextNumber1 && !isTextNumber2) {
                int size = g1.getNumber().length();
                Integer numer = Integer.parseInt(g1.getNumber().substring(0, size - 1));
                return numer - number2;
            }
            if (!isTextNumber1 && isTextNumber2) {
                int size = g2.getNumber().length();
                Integer numer = Integer.parseInt(g2.getNumber().substring(0, size - 1));
                return number1 - numer;
            }
        }
        return 0;
    }
}
