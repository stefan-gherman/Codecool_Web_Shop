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
    private boolean methodCard = false;
    private String cardHolder;
    private String cardNumber;
    private String cardExpiration;
    private String cardSecurityCode;
    private boolean methodPayPal = false;
    private String payPalUsername;
    private String payPalPassword;

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

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getBillingAddress() {
        return this.billingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.billingAddress = shippingAddress;
    }

    public String getShippingAddress() {
        return this.shippingAddress;
    }

    public void clear() {
        fullName = "";
        email = "";
        phoneNumber = "";
        billingAddress = "";
        shippingAddress = "";
        methodCard = false;
        cardHolder = "";
        cardNumber = "";
        cardExpiration = "";
        cardSecurityCode = "";
        methodPayPal = false;
        payPalUsername = "";
        payPalPassword = "";
    }


}
