package com.codecool.shop.model;

import java.sql.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cart {
    private int id;
    private int userId;
    private Map<ListItem, Integer> cartContents = new HashMap<>();
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

    public Map<ListItem, Integer> getCartContents() {
        return cartContents;
    }

    public void setCartContents(Map<ListItem, Integer> cartContents) {
        this.cartContents = cartContents;
    }

    public Date getCartCreatedAtDate() {
        return cartCreatedAtDate;
    }

    public void setCartCreatedAtDate(Date cartCreatedAtDate) {
        this.cartCreatedAtDate = cartCreatedAtDate;
    }
}
