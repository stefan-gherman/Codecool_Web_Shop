package com.codecool.shop.utils;

import com.codecool.shop.model.Product;

import java.util.*;

public class Utils {

    public static Map<Product, Integer> sortMap( Map< Product, Integer> productsInCart) {
        List<Map.Entry<Product, Integer>> productsInCartList = new LinkedList<Map.Entry<Product, Integer>>(productsInCart.entrySet());
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
}
