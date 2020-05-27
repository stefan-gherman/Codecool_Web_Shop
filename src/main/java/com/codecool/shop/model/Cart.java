package com.codecool.shop.model;

import com.codecool.shop.controller.CartController;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.implementation.ProductDaoJDBC;
import com.codecool.shop.utils.Utils;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cart {
    private int id;
    private int userId;
    private Map<ListItem, Integer> cartContents = new HashMap<>();
    private Date cartCreatedAtDate;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Cart.class);

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Map<ListItem, Integer> getCartContents() {
        return Utils.sortMap(this.cartContents);
    }


    public void setCartContents(Map<ListItem, Integer> cartContents) {
        this.cartContents = cartContents;
    }

    public Date getCartCreatedAtDate() {
        return cartCreatedAtDate;
    }

    public void setCartCreatedAtDate(Date cartCreatedAtDate) {
        this.cartCreatedAtDate = cartCreatedAtDate;
    }


    public int getCartNumberOfProducts() {
        int numberOfProudcts = 0;

        for (Map.Entry<ListItem, Integer> entry : cartContents.entrySet()
        ) {
            numberOfProudcts += entry.getValue();
        }
        return numberOfProudcts;
    }

    public float getTotalSum() {
        float totalSum = 0;
        for (Map.Entry<ListItem, Integer> product : cartContents.entrySet()
        ) {
            totalSum += product.getValue() * product.getKey().getProductPrice();
        }
        return totalSum;
    }

    public void addListItem(ListItem item) {
        Map<ListItem, Integer> tempMap = cartContents;
        int count = 0;
        if (cartContents.size() == 0) {
            cartContents.put(item, 1);
        } else {
            for (Map.Entry<ListItem, Integer> entry : this.cartContents.entrySet()
            ) {
                if (entry.getKey().getProductId() == item.getProductId()) {
                    tempMap.put(entry.getKey(), entry.getValue() + 1);
                    break;
                } else {
                    count++;
                }
            }
        }
        if( count == this.cartContents.size()) {
            tempMap.put(item, 1);
        }
        this.setCartContents(tempMap);
    }

    public void add(int id) throws SQLException, IOException {
        ProductDao productsList = ProductDaoJDBC.getInstance();
        Product product = productsList.find(id);
        Map<ListItem, Integer> tempMap = cartContents;
        int count = 0;
        logger.info("Started add to cart with just id");
        logger.debug("Product to be added Id:'{}'", product.getId());
        logger.info("Adding");
        if (cartContents.size() == 0) {
            cartContents.put(new ListItem(id, product.getName(), product.getImage(),
                    product.getDefaultPrice(), product.getDefaultCurrency().toString()), 1);
        } else {
            for (Map.Entry<ListItem, Integer> entry : cartContents.entrySet()
            ) {

                if (entry.getKey().getProductId() == product.getId()) {
                    logger.warn("Adding existing");
                    tempMap.put(entry.getKey(), entry.getValue() + 1);
                    break;
                } else {
                    count++;
                }

            }
        }
        if (count == this.cartContents.size()) {
            logger.warn("Adding non existing");
            tempMap.put(new ListItem(id, product.getName(), product.getImage(),
                    product.getDefaultPrice(), product.getDefaultCurrency().toString()), 1);
        }
        this.setCartContents(tempMap);
    }

    public void add(int id, int quantity) throws SQLException, IOException {
        ProductDao productsList = ProductDaoJDBC.getInstance();
        Product product = productsList.find(id);
        Map<ListItem, Integer> tempMap = cartContents;
        int count = 0;
        logger.info("Started add to cart with id and quantity");
        logger.debug("Product to be added Id:'{}'", product.getId());
        logger.info("Adding");
        if (cartContents.size() == 0) {
            cartContents.put(new ListItem(id, product.getName(), product.getImage(),
                    product.getDefaultPrice(), product.getDefaultCurrency().toString()), 1);
        } else {
            for (Map.Entry<ListItem, Integer> entry : cartContents.entrySet()
            ) {

                if (entry.getKey().getProductId() == product.getId()) {
                    logger.warn("Adding existing");
                    if (quantity == 0) {

                        if (tempMap.containsKey(entry.getKey())) {
                            System.out.println(entry.getKey().getProductName());
                            tempMap.remove(entry.getKey());
                        } else {
                            logger.debug("Does not contain");
                        }

                    } else {
                        tempMap.put(entry.getKey(), quantity);

                    }
                    break;
                } else {
                    count++;
                }

            }
        }
        if (count == this.cartContents.size() && quantity != 0) {
            logger.warn("Adding non existing");
            tempMap.put(new ListItem(id, product.getName(), product.getImage(),
                    product.getDefaultPrice(), product.getDefaultCurrency().toString()), 1);
        }
        this.setCartContents(tempMap);
    }

    public void eraseMe() {
        this.cartContents.clear();
        logger.warn("Cart Cleared");
    }
}
