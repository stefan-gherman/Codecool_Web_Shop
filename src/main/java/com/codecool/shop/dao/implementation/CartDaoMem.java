package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.utils.Utils;

import java.sql.SQLException;
import java.util.*;

public class CartDaoMem implements CartDao {

    private HashMap<Product, Integer> cartContents = new LinkedHashMap<>();
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
    public void add(int id) throws SQLException {
        ProductDao productsList = ProductDaoJDBC.getInstance();
        Product product = productsList.find(id);
        if(cartContents.containsKey(product)){
            cartContents.put(product, cartContents.get(product) + 1);
        } else {
            cartContents.put(product, 1);
        }
    }

    @Override
    public void add(int id, int quantity) throws SQLException {
        ProductDao productList = ProductDaoJDBC.getInstance();
        Product product = productList.find(id);

        if(cartContents.containsKey(product)) {
            if (quantity == 0) {

                cartContents.remove(product);
            } else {
                cartContents.put(product, quantity);
            }
        }
    }



    @Override
    public void remove(int id) throws SQLException {
        ProductDao productsList = ProductDaoJDBC.getInstance();
        Product product = productsList.find(id);
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
        System.out.println(Utils.sortMap(cartContents));
        return Utils.sortMap(this.cartContents);
    }

    @Override
    public int getCartNumberOfProducts() {
        int totalProducts = 0;

        for (Map.Entry<Product, Integer> product: cartContents.entrySet()
             ) {
            totalProducts += product.getValue();
        }
        return totalProducts;
    }

    @Override
    public void eraseMe() {
        this.cartContents.clear();
    }

    @Override
    public float getTotalSum() {
        float totalSum = 0;
        for (Map.Entry<Product, Integer> product: cartContents.entrySet()
        ) {
            totalSum += product.getValue() * product.getKey().getDefaultPrice();
        }
        return totalSum;
    }
}
