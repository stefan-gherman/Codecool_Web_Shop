package com.codecool.shop.dao;

import com.codecool.shop.model.Product;

import java.util.List;
import java.util.Map;

public interface CartDao {

    void add(int id);
    void remove(int id);
    Map<Product, Integer> getCartContents();
    int getCartNumberOfProducts();
    void eraseMe();
    float getTotalSum();


}
