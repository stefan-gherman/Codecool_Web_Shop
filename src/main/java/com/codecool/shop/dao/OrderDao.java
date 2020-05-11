package com.codecool.shop.dao;

import com.codecool.shop.model.Order;
import com.codecool.shop.model.Product;

import java.util.List;

public interface OrderDao {

    public void add(String fullName, String phoneNumber, String email, String billingAddress, String shippingAddress);
    public void update();
    public Order get(int id);
    public List<Product> getItems();

}
