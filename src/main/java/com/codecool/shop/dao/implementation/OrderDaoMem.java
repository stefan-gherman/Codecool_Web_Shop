package com.codecool.shop.dao.implementation;

import com.codecool.shop.connection.DBConnect;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.model.ListItem;
import com.codecool.shop.model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class OrderDaoMem implements OrderDao {

    DBConnect dbConnect = DBConnect.getInstance();
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
    public int add(Order order) {
        String fullName = order.getFullName();
        int cartId = order.getCartId();
        String phoneNumber = order.getPhoneNumber();
        String email = order.getEmail();
        String billingAddress = order.getBillingAddress();
        String shippingAddress = order.getShippingAddress();


        System.out.println("Attempting to add new order." + fullName + phoneNumber + email);
        Connection conn = null;
        PreparedStatement pstmt = null;
        int orderIdFromDb = 0;
        //TODO INSERT INTO orders RETURNING ID -- param object, id null > create obj in DB, get serial, return DB ID > in memory update object ID
        // take object as parameter - update the order field in the DB
        // future modifications only in method body, not signature
        try {
            conn = dbConnect.getConnection();
            pstmt = conn.prepareStatement("INSERT INTO orders (owner_name, cart_id, owner_phone, owner_email, billing_address, shipping_address) " +
                    "VALUES (?, ?, ?, ?, ?, ?) RETURNING id");
            pstmt.setString(1, fullName);
            pstmt.setInt(2, cartId);
            pstmt.setString(3, phoneNumber);
            pstmt.setString(4, email);
            pstmt.setString(5, billingAddress);
            pstmt.setString(6, shippingAddress);
            //pstmt.executeUpdate();
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                orderIdFromDb = resultSet.getInt("id");
            }

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
        return orderIdFromDb;
    }

    @Override
    public void update() {

    }

    @Override
    public Order get(int id) {
        return null;
    }

    @Override
    public List<ListItem> getItems(int cartId) {
        System.out.println("Attempting to get items.");
        Connection conn = null;
        PreparedStatement pstmt = null;
        List<ListItem> returnedItems = new ArrayList<>();
        try {
            conn = dbConnect.getConnection();
            pstmt = conn.prepareStatement("" +
                    "SELECT products.id, products.name, " +
                    "products.image, products.price, products.currency " +
                    "FROM cart_items " +
                    "JOIN products ON cart_items.product_id = products.id " +
                    "WHERE cart_items.cart_id = ?;");
            pstmt.setInt(1, cartId);

            ResultSet resultSet = pstmt.executeQuery();
            while(resultSet.next()) {
                returnedItems.add(new ListItem(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("image"),
                        resultSet.getFloat("price"),
                        resultSet.getString("currency")));
            }
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
        return returnedItems;
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE, USER, PASS);
    }

}
