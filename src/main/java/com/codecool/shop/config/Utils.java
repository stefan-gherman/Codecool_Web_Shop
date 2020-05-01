package com.codecool.shop.config;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    /**
     * Validates a name at input based on given conditions.
     * @param name
     * @return true or false
     */
    public static boolean validateNameInput(String name) {
        // the name has to be between 2 and 50 chars
        if (name.length() < 2 || name.length() > 50) return false;
        return true;
    }

    public String sanitize(String text) {
        text.replace("'", "\'");
        text.replace('"', '\"');
        text.replace("<", "&lt;");
        text.replace(">", "&gt;");
        return text;
    }

    /**
     * Validation for the card number at input.
     * @param cardNumber
     * @return true or false
     */
    public static boolean validateCardNumberInput(String cardNumber) {
        // the number has to be 16 digits long
        String temp = cardNumber.replace(" ", "");
        String temp2 = temp.replace("-", "");
        String temp3 = temp2.replace(".", "");
        String regex = "^\\d{16}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(temp3);
        if (matcher.matches()) return true;
        return false;
    }

}
