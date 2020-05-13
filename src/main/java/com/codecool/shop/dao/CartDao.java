package com.codecool.shop.dao;

import com.codecool.shop.model.Product;

import java.util.Map;

public interface CartDao {

    void add(int id);
    void remove(int id);
    Map<Product, Integer> getCartContents();
    void update(int id);
    void getAll();
    int getCartNumberOfProducts();
    void eraseMe();
    float getTotalSum();
    void add(int id, int quantity);
    int insertInDB(Integer userId);



}
