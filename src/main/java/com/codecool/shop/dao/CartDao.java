package com.codecool.shop.dao;

import com.codecool.shop.model.ListItem;
import com.codecool.shop.model.Product;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface CartDao {

    void add(int id) throws SQLException, IOException;
//    void remove(int id);
    Map<ListItem, Integer> getCartContents();
//    void update(int id);
//    void getAll();
    int getCartNumberOfProducts();
//    void eraseMe();
    float getTotalSum();
    int saveInDB(Integer userId) throws SQLException;
    void add(int id, int quantity) throws SQLException, IOException;




}
