package com.codecool.shop.model;

import java.util.List;

public class Order {

    private List<Product> items;
    private int id;
    private static int numberOfOrders=0;
    private double total;

    {
        numberOfOrders +=1;
    }

    public Order(List<Product> items) {
        this.id = numberOfOrders;
        this.items = items;
        this.total = getTotal();
    }

    private double getTotal() {
        for (Product item : this.items) {
            total += item.getDefaultPrice();
        }
        return total;
    }

}
