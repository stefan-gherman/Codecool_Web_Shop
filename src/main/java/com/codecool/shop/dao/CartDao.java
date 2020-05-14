package com.codecool.shop.dao;

import com.codecool.shop.model.Cart;
import com.codecool.shop.model.ListItem;
import com.codecool.shop.model.Product;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface CartDao {

    void add(int id) throws SQLException, IOException;

    Cart addCartToDB(Cart cartToBeAdded);

    //    void remove(int id);
    Map<ListItem, Integer> getCartContents();

    //    void update(int id);
//    void getAll();
    int getCartNumberOfProducts();

    //    void eraseMe();
    float getTotalSum();

    Cart getCart();

    int saveInDB(int userId) throws SQLException;

    void saveCartAndListItems(int cartId, Cart cart);

    void add(int id, int quantity) throws SQLException, IOException;

    void deleteUserCart(int id) throws SQLException;

    Cart createCartFromQuery(int id) throws SQLException, IOException;

}
