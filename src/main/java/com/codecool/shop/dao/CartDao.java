package com.codecool.shop.dao;

import com.codecool.shop.model.Cart;
import com.codecool.shop.model.ListItem;
import com.codecool.shop.model.Product;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface CartDao {

    Cart add(Cart cartToBeAdded);

    int getCartNumberOfProducts();

    Cart getCart();

    int update(int userId) throws SQLException;

    void saveCartAndListItems(int cartId, Cart cart);

    void addToCart(int id, int quantity) throws SQLException, IOException;

    void delete(int id) throws SQLException;

    Cart createCartFromQuery(int id) throws SQLException, IOException;

    Cart getCartById(int cartId);

}
