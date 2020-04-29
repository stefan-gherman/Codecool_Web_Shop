package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.model.Product;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;


public class OrderDaoMem implements OrderDao {

    private int id;
    private int orderCounter = 0;
    private List<Product> items = setItems();
    private static OrderDaoMem instance = null;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String billingAddress;
    private String shippingAddress;

    {
        orderCounter += 1;
    }

    private OrderDaoMem() {
        id = orderCounter;
    }

    public static OrderDaoMem getInstance(){
        if(instance == null) {
            instance = new OrderDaoMem();
        }
        return instance;
    }

    private List<Product> setItems() {
        List<Product> temp = new ArrayList<>();
        CartDao cartDataStore = CartDaoMem.getInstance();
        cartDataStore.getCartContents().forEach((key, value) -> {
                    for(int i=0; i<value; i++) {
                        temp.add(key);
                    }
                }
                );
        return temp;
    }

    public List<Product> getItems() {
        return items;
    }

    public int getId() {
        return id;
    }

    public String getTotal() {
        double temp = 0;
        for (Product item : this.items) {
            temp += item.getDefaultPrice();
        }
        NumberFormat formatter = new DecimalFormat("#.00");
        String total = formatter.format(temp);
        return total;
    }

}
