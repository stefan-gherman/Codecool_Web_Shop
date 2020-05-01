package com.codecool.shop.config;

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

}
