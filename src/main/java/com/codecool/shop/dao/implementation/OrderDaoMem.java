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
        String fullName = order.getOwnerName();
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
    public void update(Order order) {
        System.out.println("Attempting to update order.");
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = dbConnect.getConnection();
            pstmt = conn.prepareStatement("UPDATE orders " +
                    "SET date_created = ?, " +
                        "cart_id = ?," +
                        "user_id = ?," +
                        "owner_name = ?," +
                        "owner_phone = ?," +
                        "owner_email = ?," +
                        "billing_address = ?," +
                        "shipping_address = ?" +
                    "WHERE id = ?;");
            pstmt.setDate(1, order.getDateCreated());
            pstmt.setInt(2, order.getCartId());
            pstmt.setInt(3, order.getUserId());
            pstmt.setString(4, order.getOwnerName());
            pstmt.setString(5, order.getPhoneNumber());
            pstmt.setString(6, order.getEmail());
            pstmt.setString(7, order.getBillingAddress());
            pstmt.setString(8, order.getShippingAddress());
            pstmt.setInt(9, order.getId());
            pstmt.executeUpdate();
        }
        catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
            System.out.println("Order update process complete.");

    }

    @Override
    public Order getOrderById(int id) {
        System.out.println("Attempting to get order by ID.");
        Connection conn = null;
        PreparedStatement pstmt = null;
        Order temp = new Order();
        try {
            conn = dbConnect.getConnection();
            pstmt = conn.prepareStatement("" +
                    "SELECT * FROM orders WHERE id=?");
            pstmt.setInt(1, id);

            ResultSet resultSet = pstmt.executeQuery();
            while(resultSet.next()) {
                temp.setId(resultSet.getInt("id"));
                temp.setDateCreated(resultSet.getDate("date_created"));
                temp.setCartId(resultSet.getInt("cart_id"));
                temp.setUserId(resultSet.getInt("user_id"));
                temp.setOwnerName(resultSet.getString("owner_name"));
                temp.setPhoneNumber(resultSet.getString("owner_phone"));
                temp.setEmail(resultSet.getString("owner_email"));
                temp.setBillingAddress(resultSet.getString("billing_address"));
                temp.setShippingAddress(resultSet.getString("shipping_address"));
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
        System.out.println("Order by ID retrieval complete.");
        return temp;
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

    @Override
    public List<Order> getOrderHistoryByUserId(int userId) {
        System.out.println("Attempting to get order history.");
        Connection conn = null;
        PreparedStatement pstmt = null;
        List<Order> temp = new ArrayList<>();
        List<ListItem> tempItems = new ArrayList<>();
        try {
            conn = dbConnect.getConnection();
            pstmt = conn.prepareStatement("" +
                    "SELECT * FROM orders WHERE id = ?;");
            pstmt.setInt(1, userId);

            Order tempOrder = new Order();
            ResultSet resultSet = pstmt.executeQuery();
            while(resultSet.next()) {
                tempOrder.setId(resultSet.getInt("id"));

                tempItems = getItems(resultSet.getInt("id"));
                tempOrder.setItems(tempItems); //TODO make getItemsbyoriderid

                tempOrder.setDateCreated(resultSet.getDate("date_created"));
                tempOrder.setCartId(resultSet.getInt("cart_id"));
                tempOrder.setUserId(resultSet.getInt("user_id"));
                tempOrder.setOwnerName(resultSet.getString("owner_name"));
                tempOrder.setPhoneNumber(resultSet.getString("owner_phone"));
                tempOrder.setEmail(resultSet.getString("owner_email"));
                tempOrder.setBillingAddress(resultSet.getString("billing_address"));
                tempOrder.setShippingAddress(resultSet.getString("shipping_address"));
                temp.add(tempOrder);
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
        System.out.println("History retrieval complete.");

        return temp;
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE, USER, PASS);
    }

}
