package com.codecool.shop.connection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnectTest extends DBConnect {
    private static String propertiesFilePath = "connection.properties";

    public static String getPropertiesFilePath() {
        return propertiesFilePath;
    }

    public static void setPropertiesFilePath(String propertiesFilePath) {
        DBConnectTest.propertiesFilePath = propertiesFilePath;
    }

    private DBConnectTest() throws IOException {
        super();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertiesFilePath);
        dbProperties.load(inputStream);
    }

    public static DBConnectTest getInstance() throws IOException {
        if (instance == null) instance = new DBConnectTest();
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbProperties.getProperty("url"), dbProperties.getProperty("user"),
                dbProperties.getProperty("pass"));
    }


}
