package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.Product;

import java.sql.*;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;


public class OrderDaoMem implements OrderDao {

    private static OrderDaoMem instance = null;
    static final String DATABASE = "jdbc:postgresql://localhost:5432/codecoolshop";
    static final String USER = "postgres";
    static final String PASS = "postgres";


    private OrderDaoMem() {

    }

    public static OrderDaoMem getInstance() {
        if (instance == null) {
            instance = new OrderDaoMem();
        }
        return instance;
    }

    @Override
    public void add(String fullName, int cartId, String phoneNumber, String email, String billingAddress, String shippingAddress) {
        System.out.println("Attempting to add new order." + fullName + phoneNumber + email);
        Connection conn = null;
        PreparedStatement pstmt = null;
        //TODO INSERT INTO orders RETURNING ID -- param object, id null > create obj in DB, get serial, return DB ID > in memory update object ID
        // take object as parameter - update the order field in the DB
        // future modifications only in method body, not signature
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement("INSERT INTO orders (owner_name, cart_id, owner_phone, owner_email, billing_address, shipping_address) VALUES (?, ?, ?, ?, ?, ?)");
            pstmt.setString(1, fullName);
            pstmt.setInt(2, cartId);
            pstmt.setString(3, phoneNumber);
            pstmt.setString(4, email);
            pstmt.setString(5, billingAddress);
            pstmt.setString(6, shippingAddress);
            pstmt.executeUpdate();
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            try{
                if(pstmt!=null)
                    pstmt.close();
            }catch(SQLException se2){
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }

        System.out.println("New order add process complete.");
    }

    @Override
    public void update() {

    }

    @Override
    public Order get(int id) {
        return null;
    }

    @Override
    public List<Product> getItems() {
        return null;
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE, USER, PASS);
    }

}
