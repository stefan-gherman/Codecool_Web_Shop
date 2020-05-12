package com.codecool.shop.model;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.utils.Utils;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Order {

    private int id;
    private int cartId;
    private int userId;
    private java.sql.Date dateCreated;
    private List<ListItem> items;
    private String ownerName;
    private String email;
    private String phoneNumber;
    private String billingAddress;
    private String shippingAddress;
    private boolean paymentMethodCard = false;
    private String cardHolder;
    private String cardNumber;
    private String cardExpiration;
    private String cardSecurityCode;
    private boolean paymentMethodPayPal = false;
    private String payPalUsername;
    private String payPalPassword;
    private String logFileName;
    private String invalidFullNameEntryMessage = "";
    private String invalidCardNumberMessage = "";

    public Order() {

    }

    public void setItems(List<ListItem> items) {
        this.items = items;
    }

    public List<ListItem> getItems() {
        return items;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public java.sql.Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(java.sql.Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public boolean isPaymentMethodCard() {
        return paymentMethodCard;
    }

    public boolean isPaymentMethodPayPal() {
        return paymentMethodPayPal;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTotal() {
        double temp = 0;
        for (ListItem item : this.items) {
            temp += item.getProductPrice();
        }
        NumberFormat formatter = new DecimalFormat("#.00");
        String total = formatter.format(temp);
        return total;
    }

    public void clear() {
        ownerName = "";
        email = "";
        phoneNumber = "";
        billingAddress = "";
        shippingAddress = "";
        paymentMethodCard = false;
        cardHolder = "";
        cardNumber = "";
        cardExpiration = "";
        cardSecurityCode = "";
        paymentMethodPayPal = false;
        payPalUsername = "";
        payPalPassword = "";
        items.clear();
        logFileName = "";
        invalidFullNameEntryMessage = "";
        invalidCardNumberMessage = "";
    }

    public void addLogEntry(OrderDao orderDataStore, String step) throws IOException {
//        String orderID = String.valueOf(orderDataStore.getId());
        String orderID = "5488402";
        String pattern = "MM_dd_yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        Date today = Calendar.getInstance().getTime();
        String todayAsString = df.format(today);

        String logFileName = "log_order_" + orderID + "_" + todayAsString;
        this.logFileName = logFileName;

        // get the path from Utils for the log file to be saved in (different paths based on computer)
        String path = Utils.getPathForLogs();

        FileWriter file = new FileWriter(path + logFileName + ".txt", true);

        String stepData = "log order ID: " + orderID + ". Order entered "+ step +" page.\n";

        try {
            file.write(stepData);
            System.out.println("Log entry complete - " + step);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            file.flush();
            file.close();
        }

    }

    public void setInvalidFullNameEntryMessage(String content) {
        invalidFullNameEntryMessage = content;
    }

    public String getInvalidFullNameEntryMessage() {
        return invalidFullNameEntryMessage;
    }

    public void setInvalidCardNumberMessage(String content) {
        invalidCardNumberMessage = content;
    }

    public String getInvalidCardNumberMessage() {
        return invalidCardNumberMessage;
    }

    public void setPaymentMethodCard(boolean status) {
        paymentMethodCard = status;
    }

    public boolean getPaymentMethodCard() {
        return paymentMethodCard;
    }

    public boolean getPaymentMethodPayPal() {
        return paymentMethodPayPal;
    }

    public void setPaymentMethodPayPal(boolean status) {
        paymentMethodPayPal = status;
    }

    public void setOwnerName(String fullName) {
        this.ownerName = fullName;
    }

    public String getOwnerName() {
        return ownerName;
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
        this.shippingAddress = shippingAddress;
    }

    public String getShippingAddress() {
        return this.shippingAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
