package com.bereznev;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

public class InputValueFormatter {
    private InputValueFormatter() {
    }

    public static String formatInputValue(String str) {
        char capitalFirstLetter = Character.toUpperCase(str.charAt(0));
        str = str.toLowerCase();
        return str.replace(str.charAt(0), capitalFirstLetter);
    }
}
