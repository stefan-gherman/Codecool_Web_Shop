package com.codecool.shop.dao.implementation;

import com.codecool.shop.connection.DBConnect;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.model.ListItem;
import com.codecool.shop.model.Order;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class OrderDaoJDBC implements OrderDao {

    DBConnect dbConnect = DBConnect.getInstance();
    private static OrderDaoJDBC instance = null;
    static final String DATABASE = "jdbc:postgresql://localhost:5432/codecoolshop";
    static final String USER = "postgres";
    static final String PASS = "postgres";


    private OrderDaoJDBC() throws IOException {

    }

    public static OrderDaoJDBC getInstance() throws IOException {
        if (instance == null) {
            instance = new OrderDaoJDBC();
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


//        System.out.println("Attempting to add new order." + order.getItems().size() + fullName + phoneNumber + email);
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
    public void addToOrderItems(Order order) {
        System.out.println("Attempting to add new order batch to order_items. Order ID: " + order.getId());
        Connection conn = null;
        PreparedStatement pstmt = null;
        System.out.println("Items list length: " + order.getItems().size());
        for (ListItem item : order.getItems()) {
            try {
                System.out.println("Trying to add to order_items: order " + order.getId() + " product " + item.getProductId());
                conn = dbConnect.getConnection();
                pstmt = conn.prepareStatement("INSERT INTO order_items (order_id, product_id) VALUES (?, ?)");
                pstmt.setInt(1, order.getId());
                pstmt.setInt(2, item.getProductId());
                pstmt.executeUpdate();
                System.out.println("Added to order_items: order " + order.getId() + " product " + item.getProductId());
            } catch (SQLException se) {
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

            System.out.println("order_items add process complete.");

        }
    }

    @Override
    public void update(Order order) {
        System.out.println("Attempting to update order.");
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            System.out.println("ENTERED UPDATE <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< try");
            conn = dbConnect.getConnection();
            pstmt = conn.prepareStatement("UPDATE orders " +
                    "SET cart_id = ?," +
                        "user_id = ?," +
                        "status = ?," +
                        "owner_name = ?," +
                        "owner_phone = ?," +
                        "owner_email = ?," +
                        "billing_address = ?," +
                        "shipping_address = ?," +
                        "total = ?" +
                    "WHERE id = ?;");
            pstmt.setInt(1, order.getCartId());
            System.out.println("1 " + order.getCartId());
            pstmt.setInt(2, order.getUserId());
            System.out.println("2 " + order.getUserId());
            pstmt.setString(3, order.getStatus());
            System.out.println("3 " + order.getStatus());
            pstmt.setString(4, order.getOwnerName());
            System.out.println("4 " + order.getOwnerName());
            pstmt.setString(5, order.getPhoneNumber());
            System.out.println("5 " + order.getPhoneNumber());
            pstmt.setString(6, order.getEmail());
            System.out.println("6 " + order.getEmail());
            pstmt.setString(7, order.getBillingAddress());
            System.out.println("7 " + order.getBillingAddress());
            pstmt.setString(8, order.getShippingAddress());
            System.out.println("8 " + order.getShippingAddress());
            pstmt.setFloat(9, order.getTotal());
            System.out.println("9 " + order.getTotal());
            pstmt.setInt(10, order.getId());
            System.out.println("10 " + order.getId());
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
                temp.setStatus(resultSet.getString("status"));
                temp.setOwnerName(resultSet.getString("owner_name"));
                temp.setPhoneNumber(resultSet.getString("owner_phone"));
                temp.setEmail(resultSet.getString("owner_email"));
                temp.setBillingAddress(resultSet.getString("billing_address"));
                temp.setShippingAddress(resultSet.getString("shipping_address"));
                temp.setTotal(resultSet.getFloat("total"));
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
    public List<ListItem> getItemsByCartId(int cartId) {
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
    public List<ListItem> getItemsByOrderId(int orderId) {
        System.out.println("Attempting to get items by order ID.");
        Connection conn = null;
        PreparedStatement pstmt = null;
        List<ListItem> returnedItems = new ArrayList<>();
        try {
            conn = dbConnect.getConnection();
            pstmt = conn.prepareStatement("" +
                    "SELECT products.id, products.name, " +
                    "products.image, products.price, products.currency " +
                    "FROM order_items " +
                    "JOIN products ON order_items.product_id = products.id " +
                    "WHERE order_items.order_id = ?;");
            pstmt.setInt(1, orderId);

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
        System.out.println("Get items by order ID complete.");
        return returnedItems;
    }


    @Override
    public List<Order> getOrderHistoryByUserId(int userId) {
        System.out.println("Attempting to get order history for user " + userId + ".");
        Connection conn = null;
        PreparedStatement pstmt = null;
        List<Integer> ids = new ArrayList<>();
        List<Order> temp = new ArrayList<>();
        List<ListItem> tempItems = new ArrayList<>();

        // get list from DB of all the ids of the orders of the user
        try {
            conn = dbConnect.getConnection();
            System.out.println("HISTORY _ got connection");
            pstmt = conn.prepareStatement("" +
                    "SELECT id FROM orders WHERE user_id = ?;");
            pstmt.setInt(1, userId);
            ResultSet resultSet = pstmt.executeQuery();
            System.out.println("HISTORY _ query executed");
            while(resultSet.next()) {
                System.out.println("HISTORY _ entered result set");
                ids.add(resultSet.getInt("id"));
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
        System.out.println("HISTORY ORDERS IDS: " + ids.toString());

        // loop through all the ids above and add each order to the list
        for (Integer historyId : ids) {
            try {
                conn = dbConnect.getConnection();
                System.out.println("HISTORY _ got connection");
                pstmt = conn.prepareStatement("" +
                        "SELECT * FROM orders WHERE id = ?;");
                pstmt.setInt(1, historyId);
                Order tempOrder = new Order();
                ResultSet resultSet = pstmt.executeQuery();
                System.out.println("HISTORY _ query executed");
                while(resultSet.next()) {
                    System.out.println("HISTORY _ entered result set");
                    tempOrder.setId(resultSet.getInt("id"));

                    tempItems = getItemsByOrderId(resultSet.getInt("id"));
                    tempOrder.setItems(tempItems);

                    tempOrder.setDateCreated(resultSet.getDate("date_created"));
                    tempOrder.setCartId(resultSet.getInt("cart_id"));
                    tempOrder.setUserId(resultSet.getInt("user_id"));
                    tempOrder.setStatus(resultSet.getString("status"));
                    tempOrder.setOwnerName(resultSet.getString("owner_name"));
                    tempOrder.setPhoneNumber(resultSet.getString("owner_phone"));
                    tempOrder.setEmail(resultSet.getString("owner_email"));
                    tempOrder.setBillingAddress(resultSet.getString("billing_address"));
                    tempOrder.setShippingAddress(resultSet.getString("shipping_address"));
                    tempOrder.setTotal(resultSet.getFloat("total"));
                    temp.add(tempOrder);
                    System.out.println("History order " + tempOrder.getId() + " added.");
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
        }
        System.out.println("History retrieval complete.");

        return temp;
    }

    @Override
    public ListItem getListItemByProductId(int productId) {

        System.out.println("Attempting to get ListItem by Product ID: " + productId);
        Connection conn = null;
        PreparedStatement pstmt = null;
        ListItem tempItem = new ListItem();
        try {
            conn = dbConnect.getConnection();
            pstmt = conn.prepareStatement("" +
                    "SELECT * FROM products WHERE id = ?;");
            pstmt.setInt(1, productId);
            ResultSet resultSet = pstmt.executeQuery();
            while(resultSet.next()) {
                tempItem.setProductId(resultSet.getInt("id"));
                tempItem.setProductName(resultSet.getString("name"));
                tempItem.setProductImage(resultSet.getString("image"));
                tempItem.setProductPrice(resultSet.getFloat("price"));
                tempItem.setProductCurrency(resultSet.getString("currency"));
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
        System.out.println("ListItem retrieval complete.");
        return tempItem;
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE, USER, PASS);
    }

}
