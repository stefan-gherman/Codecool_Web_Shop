package com.codecool.shop.model;

import java.sql.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Cart {
    private int id;
    private int userId;
    private HashMap<Product, Integer> cartContents = new LinkedHashMap<>();
    private Date cartCreatedAtDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public HashMap<Product, Integer> getCartContents() {
        return cartContents;
    }

    public void setCartContents(HashMap<Product, Integer> cartContents) {
        this.cartContents = cartContents;
    }

    public Date getCartCreatedAtDate() {
        return cartCreatedAtDate;
    }

    public void setCartCreatedAtDate(Date cartCreatedAtDate) {
        this.cartCreatedAtDate = cartCreatedAtDate;
    }
}
