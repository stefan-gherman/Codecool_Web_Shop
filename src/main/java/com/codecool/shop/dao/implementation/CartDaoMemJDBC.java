package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.Product;
import com.codecool.shop.utils.Utils;

import java.sql.Date;
import java.text.DateFormat;
import java.util.*;

public class CartDaoMemJDBC implements CartDao {

    private Cart cart;



    private static CartDaoMemJDBC instance = null;


    private CartDaoMemJDBC() {
        this.cart = new Cart();
        java.util.Date utilDate = new java.util.Date();
        this.cart.setCartCreatedAtDate(new Date(utilDate.getTime()));
    }


    public static CartDaoMemJDBC getInstance(){
        if(instance == null) {
            instance = new CartDaoMemJDBC();
        }
        return instance;
    }

    @Override
    public void add(int id) {
        ProductDao productsList = ProductDaoMem.getInstance();
        Product product = productsList.find(id);
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

        if(cart.getCartContents().containsKey(product)) {
            if (quantity == 0) {

                cart.getCartContents().remove(product);
            } else {
                cart.getCartContents().put(product, quantity);
            }
        }
    }



    @Override
    public void remove(int id) {
        ProductDao productsList = ProductDaoMem.getInstance();
        Product product = productsList.find(id);
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
