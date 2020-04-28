package com.codecool.shop.model;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class Order {

    private List<Product> items;
    private int id;
    private static int numberOfOrders=0;
    private String total;

    {
        numberOfOrders +=1;
    }

    public Order(List<Product> items) {
        this.id = numberOfOrders;
        this.items = items;
        this.total = getTotal();
    }

    public String getTotal() {
        double temp = 0;
        for (Product item : this.items) {
            temp += item.getDefaultPrice();
        }
        NumberFormat formatter = new DecimalFormat("#.00");
        total = formatter.format(temp);
        return total;
    }

    public int getId() {
        return id;
    }

    public List<Product> getItems() {
        return items;
    }

}
