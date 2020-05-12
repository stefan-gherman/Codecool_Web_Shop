package com.codecool.shop.utils;

import com.codecool.shop.model.Product;

import java.util.*;
import org.mindrot.jbcrypt.BCrypt;

public class Utils {

    public static Map<Product, Integer> sortMap( Map< Product, Integer> productsInCart) {
        List<Map.Entry<Product, Integer>> productsInCartList = new LinkedList<>(productsInCart.entrySet());
        productsInCartList.sort((prod1, prod2) -> {
            if ((prod1.getKey().getDefaultPrice() * prod1.getValue()) < (prod2.getKey().getDefaultPrice() * prod2.getValue())) {

                return 1;
            } else if ((prod1.getKey().getDefaultPrice() * prod1.getValue()) == (prod2.getKey().getDefaultPrice() * prod2.getValue())) {

                return 0;
            } else if ((prod1.getKey().getDefaultPrice() * prod1.getValue()) > (prod2.getKey().getDefaultPrice() * prod2.getValue())) {

                return -1;
            }
            return 1;
        });
        Map<Product, Integer> sortedCart = new LinkedHashMap<>();

        for(Map.Entry<Product, Integer> product : productsInCartList) {
            sortedCart.put(product.getKey(), product.getValue());
        }
        return sortedCart;
    }

    public static String getPathForLogs() {
        // comment out the paths from other computers and add yours

        // dan
        //return "/home/dan/Downloads/";

        //stefan
//        return "/mnt/7d45c543-fc06-4310-b70a-2a9aa2e43a54/Projects/codecool/java/Codecool_Web_Shop/src/main/webapp/static/logs/";

        //general
        return "src/main/webapp/static/logs/";
    }

    public static String hasher(String password) {
        int toughness = 12;
        String salt = BCrypt.gensalt(toughness);
        String hashed_password = BCrypt.hashpw(password, salt);
        return(hashed_password);
    }

    public static boolean checkPassword(String password_plaintext, String stored_hash) {
        boolean password_verified = false;

        if(null == stored_hash || !stored_hash.startsWith("$2a$"))
            throw new java.lang.IllegalArgumentException("Invalid hash provided for comparison");

        password_verified = BCrypt.checkpw(password_plaintext, stored_hash);

        return(password_verified);
    }
}
