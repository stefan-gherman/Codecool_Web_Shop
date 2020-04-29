package com.codecool.shop.dao;

import com.codecool.shop.model.Product;

import java.util.List;

public interface OrderDao {

    List<Product> getItems();
    int getId();
    String getTotal();
    void setFullName(String fullName);
    String getFullName();
    void setEmail(String email);
    void setPhoneNumber(String phoneNumber);
    void setBillingAddress(String billingAddress);
    void setShippingAddress(String shippingAddress);

}
