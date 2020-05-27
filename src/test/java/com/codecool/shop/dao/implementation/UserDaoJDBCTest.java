package com.codecool.shop.dao.implementation;

import com.codecool.shop.connection.DBConnect;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Product;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoJDBCTest {

    public static void executeUpdateFromFile(String filePath) {
        String query = "";
        try {
            query = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


//    @AfterEach
//    public static void tearDown() {
//        try {
////            worldDBCreator.executeUpdate("TRUNCATE TABLE city CASCADE;");
////            worldDBCreator.executeUpdate("TRUNCATE TABLE country CASCADE;");
////            worldDBCreator.executeUpdate("TRUNCATE TABLE countrylanguage CASCADE;");
//
//        } catch (SQLException e) {
//            System.err.println("ERROR: Data cleanup failed.");
//            e.printStackTrace();
//        }
//    }

    public static void executeUpdate(String query) throws SQLException {
        try (Connection connection = DBConnect.getInstance().getConnection()) {

            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();

        } catch (SQLTimeoutException | IOException e) {
            System.err.println("ERROR: SQL Timeout");
        }
    }

    @BeforeEach
    public void setUp() throws IOException {
        DBConnect worldDBCreator = DBConnect.getInstance();
        executeUpdateFromFile("src/main/sql/init_db.sql");
    }

    @Test
    public void findProductTest() throws IOException, SQLException {
        // arrange
        ProductDao productDataStore = ProductDaoJDBC.getInstance();
        ProductCategoryDao productCategoryDao = ProductCategoryJDBC.getInstance();
        SupplierDao supplierDao = SupplierDaoJDBC.getInstance();
        Product product = new Product(3,supplierDao.find(3), productCategoryDao.find(1), "Samsung Galaxy Tab S5e","Weighing in at less than a pound, the incredibly slim Galaxy Tab S5e is perfect for your on-the-go life.", "Samsung Galaxy Tab S5e.jpg", 548.99f, "USD");

        // act
        Product result = productDataStore.find(3);

        // assert

        assertEquals(result.toString(),product.toString());
    }

    @Test
    void getAllProductsTest() {

    }

    @Test
    void getProductByCategoryTest() {

    }

    @Test
    void findProductCategoryTest() {

    }

    @Test
    void getAllProductCategoriesTest() {

    }

    @Test
    void findSupplierTest() {

    }

    @Test
    void getAllSuppliersTest() {

    }

    @Test
    void addCartTest() {

    }

    @Test
    void addOrderTest() {

    }

    @Test
    void getOrderByIdTest(){

    }

    @Test
    void addUserTest(){

    }

    @Test
    void getUserByEmailTest(){

    }

}