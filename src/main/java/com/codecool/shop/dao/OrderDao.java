package com.codecool.shop.dao;

import com.codecool.shop.model.ListItem;
import com.codecool.shop.model.Order;

import java.util.List;

public interface OrderDao {

    public void add(String fullName, int cartId, String phoneNumber, String email, String billingAddress, String shippingAddress);
    public void update();
    public Order get(int id);
    public List<ListItem> getItems(int userId);

}
