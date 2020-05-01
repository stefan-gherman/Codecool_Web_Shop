package com.codecool.shop.dao;

import com.codecool.shop.model.Product;

import java.io.IOException;
import java.util.List;

public interface OrderDao {

    List<Product> getItems();
    int getId();
    String getTotal();
    void setFullName(String fullName);
    String getFullName();
    void setEmail(String email);
    String getEmail();
    void setPhoneNumber(String phoneNumber);
    void setBillingAddress(String billingAddress);
    String getBillingAddress();
    void setShippingAddress(String shippingAddress);
    String getShippingAddress();
    void clear();
    List<Product> setItems();
    void addLogEntry(OrderDao orderDataStore, String checkout) throws IOException;
}
