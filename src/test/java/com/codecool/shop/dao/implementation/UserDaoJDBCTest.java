package com.codecool.shop.dao.implementation;

import com.codecool.shop.connection.DBConnect;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoJDBCTest {
//    static DBConnect dbConnect;
    @BeforeAll
    public static void setUp() throws IOException {
        DBConnect.setPropertiesFilePath("connect_test.properties");
        DBConnect dbConnect = DBConnect.getInstance();


    }

    @Test
    void add() {
    }

    @Test
    void getUserByEmail() {
    }

    @Test
    void getUserPasswordByEmail() {
    }
}