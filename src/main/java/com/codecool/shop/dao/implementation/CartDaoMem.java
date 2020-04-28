package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.CartDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartDaoMem implements CartDao {

    private HashMap<Product, Integer> cartContents = new HashMap<>();
    private static CartDaoMem instance = null;


    private CartDaoMem() {
    }


    public static CartDaoMem getInstance(){
        if(instance == null) {
            instance = new CartDaoMem();
        }
        return instance;
    }

    @Override
    public void add(Product product) {
        if(cartContents.containsKey(product)){
            cartContents.put(product, cartContents.get(product) + 1);
        } else {
            cartContents.put(product, 1);
        }
    }

    @Override
    public void remove(Product product) {
        if(cartContents.containsKey(product)) {
            if(cartContents.get(product) == 1) {
                cartContents.remove(product);
            } else {
                cartContents.put(product, cartContents.get(product) - 1);
            }
        }
    }

    @Override
    public Map<Product, Integer> getCartContents() {
        return Utils.sortMap(cartContents);
    }

    public int getCartNumberOfProducts() {
        return this.cartContents.size();
    }

    public void eraseMe() {
        this.cartContents = new HashMap<>();
    }
}
