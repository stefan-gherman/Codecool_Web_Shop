package com.codecool.shop.connection;

//import com.codecool.shop.dao.implementation.OrderDaoMem;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnect {

    private String DATABASE = getConnectionProperties("url");
    private String USER = getConnectionProperties("user");
    private String PASS = getConnectionProperties("pass");
    private static DBConnect instance;

    private DBConnect() throws IOException {

    }

    public static DBConnect getInstance() throws IOException {
        if (instance == null) {
            instance = new DBConnect();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE, USER, PASS);
    }

    private String getConnectionProperties(String property) throws IOException {
        String path = "src/main/java/com/codecool/shop/connection/";
        String connectionName = path + "connection.properties";
        Properties dbProperties = new Properties();

        dbProperties.load(new FileInputStream(connectionName));

        String url = dbProperties.getProperty("url");
        String user = dbProperties.getProperty("user");
        String pass = dbProperties.getProperty("pass");

        if (property.equalsIgnoreCase("url")) {
            return url;
        } else if (property.equalsIgnoreCase("user")) {
            return user;
        } else if (property.equalsIgnoreCase("pass")) {
            return pass;
        }
        return null;
    }

}
