package com.codecool.shop.dao;

import com.codecool.shop.model.Product;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface CartDao {

    void add(int id) throws SQLException;
    void remove(int id) throws SQLException;
    Map<Product, Integer> getCartContents();
    int getCartNumberOfProducts();
    void eraseMe();
    float getTotalSum();
    void add(int id, int quantity) throws SQLException;




}
