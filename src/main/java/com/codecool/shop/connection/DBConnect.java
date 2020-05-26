package com.codecool.shop.connection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnect {
    protected final Properties dbProperties = new Properties();
    protected static DBConnect instance;
    protected static String propertiesFilePath = "connection.properties";

    public static String getPropertiesFilePath() {
        return propertiesFilePath;
    }

    public static void setPropertiesFilePath(String propertiesFilePath) {
        DBConnect.propertiesFilePath = propertiesFilePath;
    }

    protected DBConnect() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertiesFilePath);
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
