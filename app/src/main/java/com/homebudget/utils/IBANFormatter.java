package com.homebudget.utils;

public class IBANFormatter {

    public static String textFormatter(String text) {
        char[] textInChars = text.replace(" ", "").toCharArray();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (; i < 2; i++) {
            if (i == textInChars.length) {
                break;
            }
            sb.append(textInChars[i]);
        }
        while (i < textInChars.length) {
            sb.append(" ");
            for (int j = 0; j < 4; j++) {
                if (i == textInChars.length) {
                    break;
                }
                sb.append(textInChars[i]);
                i++;
            }
        }
        return sb.toString();
    }
}
