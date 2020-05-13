package com.codecool.shop.model;

public class ListItem {
    int productId;
    String productName;
    String productImage;
    Float productPrice;
    String productCurrency;

    public ListItem(int productId, String productName, String productImage, float productPrice, String productCurrency) {
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
        this.productPrice = productPrice;
        this.productCurrency = productCurrency;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public Float getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Float productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductCurrency() {
        return productCurrency;
    }

    public void setProductCurrency(String productCurrency) {
        this.productCurrency = productCurrency;
    }

    @Override
    public String toString() {
        return "ListItem{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
//                ", productImage='" + productImage + '\'' +
//                ", productPrice=" + productPrice +
//                ", productCurrency='" + productCurrency + '\'' +
                '}';
    }
}
