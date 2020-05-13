package com.codecool.shop.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {
    private final String DATABASE = "jdbc:postgresql://localhost:5432/codecoolshop";
    private final String USER = "postgres";
    private final String PASS = "postgres";
    private static DBConnect instance;

    private DBConnect() {

    }

    public static DBConnect getInstance() {
        if (instance == null) {
            instance = new DBConnect();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE, USER, PASS);
    }
}
