package com.codecool.shop.connection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnect {
    private final Properties dbProperties = new Properties();
    private static DBConnect instance;

    private DBConnect() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("connection.properties");
        dbProperties.load(inputStream);
    }

    public static DBConnect getInstance() throws IOException {
        if (instance == null) instance = new DBConnect();
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbProperties.getProperty("url"), dbProperties.getProperty("user"),
                dbProperties.getProperty("pass"));
    }


}
