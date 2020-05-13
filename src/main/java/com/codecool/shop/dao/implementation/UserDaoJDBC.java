package com.codecool.shop.dao.implementation;

import com.codecool.shop.connection.DBConnect;
import com.codecool.shop.dao.UserDao;
import com.codecool.shop.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoJDBC implements UserDao {

    DBConnect dbConnect = DBConnect.getInstance();

    private static UserDaoJDBC instance;

    private UserDaoJDBC() {

    }

    public static UserDaoJDBC getInstance() {
        if (instance == null) {
            instance = new UserDaoJDBC();
        }
        return instance;
    }

    @Override
    public int add(User user) {
        System.out.println("Attempting to add new user.");
        Connection conn = null;
        PreparedStatement pstmt = null;
        int returnedId=0;
        //TODO INSERT INTO orders RETURNING ID -- param object, id null > create obj in DB, get serial, return DB ID > in memory update object ID
        // take object as parameter - update the order field in the DB
        // future modifications only in method body, not signature
        try {
            conn = dbConnect.getConnection();
            pstmt = conn.prepareStatement("INSERT INTO users (name, email, password,phone_number, " +
                     "billing_address, " +
                    "shipping_address) VALUES (?, ?, ?, ?, ?, ?) RETURNING id");
            pstmt.setString(1, user.getFullName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getPhoneNumber());
            pstmt.setString(5, user.getBillingAddress());
            pstmt.setString(6, user.getShippingAddress());
            //pstmt.executeUpdate();
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("id"));
                System.out.println("String repr:" + resultSet.getString("id"));
                returnedId = resultSet.getInt("id");
            }

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

        System.out.println("New order add process complete.");
        return returnedId;
    }

    @Override
    public User getUserByEmail(String email) {
        System.out.println("Attempting to get user by Email.");
        Connection conn = null;
        PreparedStatement pstmt = null;
        User userFromDB = new User();
        try {
            conn = dbConnect.getConnection();
            pstmt = conn.prepareStatement("" +
                    "SELECT * FROM users WHERE email=?");
            pstmt.setString(1, email);

            ResultSet resultSet = pstmt.executeQuery();
            while(resultSet.next()) {
               userFromDB.setId(resultSet.getInt("id"));
               userFromDB.setFullName(resultSet.getString("name"));
               userFromDB.setEmail(resultSet.getString("email"));
               userFromDB.setPassword(resultSet.getString("password"));
               userFromDB.setPhoneNumber(resultSet.getString("phone_number"));
               userFromDB.setBillingAddress(resultSet.getString("billing_address"));
               userFromDB.setShippingAddress(resultSet.getString("shipping_address"));
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
        System.out.println("User by email retrieval complete.");
        return userFromDB;
    }

    @Override
    public String getUserPasswordByEmail(String email) {
        System.out.println("Attempting to get user hashed password by Email.");
        Connection conn = null;
        PreparedStatement pstmt = null;
        String hashedPassword = "";
        try {
            conn = dbConnect.getConnection();
            pstmt = conn.prepareStatement("" +
                    "SELECT password FROM users WHERE email=?");
            pstmt.setString(1, email);

            ResultSet resultSet = pstmt.executeQuery();
            while(resultSet.next()) {
               hashedPassword = resultSet.getString("password");
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
        System.out.println("User hashed password by email retrieval complete.");
        return hashedPassword;
    }
}
