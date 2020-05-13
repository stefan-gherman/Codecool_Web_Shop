package com.codecool.shop.dao.implementation;

import com.codecool.shop.connection.DBConnect;
import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.Product;
import com.codecool.shop.utils.Utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class CartDaoJDBC implements CartDao {

    private Cart cart;



    private static CartDaoJDBC instance = null;
    private static DBConnect connection = DBConnect.getInstance();


    private CartDaoJDBC() {
        this.cart = new Cart();
    }


    public static CartDaoJDBC getInstance(){
        if(instance == null) {
            instance = new CartDaoJDBC();
        }
        return instance;
    }

    @Override
    public void add(int id) {
        ProductDao productsList = ProductDaoMem.getInstance();
        Product product = productsList.find(id);
        //
        if(cart.getCartContents().containsKey(product)){
            cart.getCartContents().put(product, cart.getCartContents().get(product) + 1);
        } else {
            cart.getCartContents().put(product, 1);
        }
    }

    @Override
    public void add(int id, int quantity) {
        ProductDao productList = ProductDaoMem.getInstance();
        Product product = productList.find(id);
        //
        if(cart.getCartContents().containsKey(product)) {
            if (quantity == 0) {

                cart.getCartContents().remove(product);
            } else {
                cart.getCartContents().put(product, quantity);
            }
        }
    }

    @Override
    public int insertInDB(Integer userId) {

        String query = "INSERT INTO carts (user_id) VALUES (?) RETURNING id ;";
        int lastCart = 0;
        if (userId == null) {
            lastCart = -1;
        }

        try {
            PreparedStatement statement = connection.getConnection().prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                lastCart = resultSet.getInt("id");
            }
            statement.close();
            connection.getConnection().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lastCart;
    }


    @Override
    public void remove(int id) {
        ProductDao productsList = ProductDaoMem.getInstance();
        Product product = productsList.find(id);
        //
        if(cart.getCartContents().containsKey(product)) {
            if(cart.getCartContents().get(product) == 1) {
                cart.getCartContents().remove(product);
            } else {
                cart.getCartContents().put(product, cart.getCartContents().get(product) - 1);
            }
        }
    }

    @Override
    public Map<Product, Integer> getCartContents() {
        System.out.println(Utils.sortMap(cart.getCartContents()));
        return Utils.sortMap(this.cart.getCartContents());
    }

    @Override
    public void update(int id) {

    }

    @Override
    public void getAll() {

    }

    @Override
    public int getCartNumberOfProducts() {
        int totalProducts = 0;

        for (Map.Entry<Product, Integer> product: cart.getCartContents().entrySet()
             ) {
            totalProducts += product.getValue();
        }
        return totalProducts;
    }

    @Override
    public void eraseMe() {
        this.cart = null;
    }

    @Override
    public float getTotalSum() {
        float totalSum = 0;
        for (Map.Entry<Product, Integer> product: cart.getCartContents().entrySet()
        ) {
            totalSum += product.getValue() * product.getKey().getDefaultPrice();
        }
        return totalSum;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
