package com.codecool.shop.dao.implementation;

import com.codecool.shop.connection.DBConnect;
import com.codecool.shop.dao.*;
import com.codecool.shop.model.*;
import com.codecool.shop.model.Order;
import org.junit.jupiter.api.*;
import org.postgresql.util.PSQLException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.util.*;

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
        executeUpdateFromFile("src/test/sql/init_dbtest.sql");
    }

    @Test
    public void findProductTest() throws IOException, SQLException {
        // arrange
        ProductDao productDataStore = ProductDaoJDBC.getInstance();
        ProductCategoryDao productCategoryDao = ProductCategoryJDBC.getInstance();
        SupplierDao supplierDao = SupplierDaoJDBC.getInstance();
        Product product = new Product(3, supplierDao.find(3), productCategoryDao.find(1), "Samsung Galaxy Tab S5e", "Weighing in at less than a pound, the incredibly slim Galaxy Tab S5e is perfect for your on-the-go life.", "Samsung Galaxy Tab S5e.jpg", 548.99f, "USD");

        // act
        Product result = productDataStore.find(3);

        // assert
        assertEquals(result.toString(), product.toString());
    }

    @Test
    void getAllProductsTest() throws IOException, SQLException {
        // arrange
        ProductDao productDataStore = ProductDaoJDBC.getInstance();
        ProductCategoryDao productCategoryDao = ProductCategoryJDBC.getInstance();
        SupplierDao supplierDao = SupplierDaoJDBC.getInstance();
        Product product1 = new Product(1, supplierDao.find(1), productCategoryDao.find(1), "Amazon Fire", "Fantastic price. Large content ecosystem. Good parental controls. Helpful technical support.", "product_2.jpg", 49.9f, "USD");
        ;
        Product product2 = new Product(2, supplierDao.find(1), productCategoryDao.find(2), "Blink XT2 Smart Security Camera", "Camera with cloud storage included, 2-way audio, 2-year battery life – 3 camera kit.", "Blink XT2 Smart Security Camera.jpg", 249.99f, "USD");
        Product product3 = new Product(3, supplierDao.find(3), productCategoryDao.find(1), "Samsung Galaxy Tab S5e", "Weighing in at less than a pound, the incredibly slim Galaxy Tab S5e is perfect for your on-the-go life.", "Samsung Galaxy Tab S5e.jpg", 548.99f, "USD");
        List<Product> productsAsList = new ArrayList<>(Arrays.asList(product1, product2, product3));
        productsAsList.sort(Comparator.comparingInt(BaseModel::getId));


        // act
        List<Product> resultProducts = productDataStore.getAll();
        resultProducts.sort(Comparator.comparingInt(BaseModel::getId));

        // assert
        assertEquals(resultProducts.toString(), productsAsList.toString());
        resultProducts.clear();
    }

    @Test
    void getProductByCategoryTest() throws IOException, SQLException {
        // arrange
        ProductDao productDataStore = ProductDaoJDBC.getInstance();
        ProductCategoryDao productCategoryDao = ProductCategoryJDBC.getInstance();
        SupplierDao supplierDao = SupplierDaoJDBC.getInstance();
        ProductCategory category = new ProductCategory(1, "Tablets", "Hardware", "A tablet computer, commonly shortened to tablet, is a thin, flat mobile computer with a touchscreen display.");
        Product product1 = new Product(1, supplierDao.find(1), productCategoryDao.find(1), "Amazon Fire", "Fantastic price. Large content ecosystem. Good parental controls. Helpful technical support.", "product_2.jpg", 49.9f, "USD");
        ;
        Product product2 = new Product(3, supplierDao.find(3), productCategoryDao.find(1), "Samsung Galaxy Tab S5e", "Weighing in at less than a pound, the incredibly slim Galaxy Tab S5e is perfect for your on-the-go life.", "Samsung Galaxy Tab S5e.jpg", 548.99f, "USD");
        List<Product> productsAsList = new ArrayList<>(Arrays.asList(product1, product2));

        // act
        List<Product> resultProducts = productDataStore.getBy(category);

        // assert
        assertEquals(resultProducts.toString(), productsAsList.toString());
        resultProducts.clear();


    }

    @Test
    void findProductCategoryTest() throws IOException, SQLException {
        // arrange
        ProductCategoryDao productCategoryDao = ProductCategoryJDBC.getInstance();
        ProductCategory category = new ProductCategory(1, "Tablets", "Hardware", "A tablet computer, commonly shortened to tablet, is a thin, flat mobile computer with a touchscreen display.");

        // act
        ProductCategory result = productCategoryDao.find(1);

        // assert
        assertEquals(result.toString(), category.toString());
    }

    @Test
    void getAllProductCategoriesTest() throws IOException, SQLException {
        // arrange
        ProductCategoryDao productCategoryDao = ProductCategoryJDBC.getInstance();
        ProductCategory category1 = new ProductCategory(1, "Tablets", "Hardware", "A tablet computer, commonly shortened to tablet, is a thin, flat mobile computer with a touchscreen display.");
        ProductCategory category2 = new ProductCategory(2, "Cameras", "Smart Home", "Protect your home with our cameras");
        ProductCategory category3 = new ProductCategory(3, "Living Room Furniture", "Furniture",
                "Make entertainment the focus of your living room with one of our living room pieces.");
        ProductCategory category4 = new ProductCategory(4, "Monitors", "Computers", "Buy the best monitor for your needs.");
        List<ProductCategory> categories = new ArrayList<>(Arrays.asList(category1, category2, category3, category4));
        categories.sort(Comparator.comparingInt(BaseModel::getId));

        // act
        List<ProductCategory> resultCategories = productCategoryDao.getAll();
        resultCategories.sort(Comparator.comparingInt(BaseModel::getId));


        // assert
        assertEquals(categories.toString(), resultCategories.toString());
    }

    @Test
    void findSupplierTest() throws IOException, SQLException {
        // arrange
        SupplierDao supplierDao = SupplierDaoJDBC.getInstance();
        Supplier supplier = new Supplier(3, "Walmart", "Various products.");

        // act
        Supplier result = supplierDao.find(3);

        // assert
        assertEquals(result.toString(), supplier.toString());
    }

    @Test
    void getAllSuppliersTest() throws IOException, SQLException {
        // arrange
        SupplierDao supplierDao = SupplierDaoJDBC.getInstance();
        Supplier supplier1 = new Supplier(1, "Amazon", "Digital content and services.");
        Supplier supplier2 = new Supplier(2, "Lenovo", "Computers and electronics.");
        Supplier supplier3 = new Supplier(3, "Walmart", "Various products.");
        List<Supplier> supplier = new ArrayList<>(Arrays.asList(supplier1, supplier2, supplier3));
        supplier.sort(Comparator.comparingInt(BaseModel::getId));

        // act
        List<Supplier> resultSuppliers = supplierDao.getAll();
        supplier.sort(Comparator.comparingInt(BaseModel::getId));

        // assert
        assertEquals(resultSuppliers.toString(), supplier.toString());

    }

    @Test
    void addCartTest() throws IOException, SQLException {
        // arrange
        CartDao cartDataStore = CartDaoJDBC.getInstance();
        Cart cart = new Cart();
        cart.setId(1);
        cart.setUserId(1);

        // act
        cartDataStore.add(cart);
        Cart resultCart = cartDataStore.getCartById(cart.getId());

        // assert
        assertEquals(cart.getId(), resultCart.getId());
        assertEquals(cart.getUserId(), resultCart.getUserId());

    }

    @Test
    void addOrderTest() throws IOException {
        // arrange
        CartDao cartDataStore = CartDaoJDBC.getInstance();
        Cart cart = new Cart();
        cart.setId(1);
        cart.setUserId(1);
        cartDataStore.add(cart);

        OrderDao orderDataStore = OrderDaoJDBC.getInstance();
        Order order = new Order();
        order.setId(1);
        order.setCartId(cart.getId());
        order.setBillingAddress("billing");
        order.setShippingAddress("billing2");
        order.setPhoneNumber("2131231");
        order.setEmail("email@gmail.com");

        // user id is 1 from int_dbtest.sql initialization
        order.setUserId(1);
        orderDataStore.add(order);

        // act
        Order orderResult = orderDataStore.getOrderById(order.getId());

        // assert
        assertEquals(order.getId(), orderResult.getId());
        assertEquals(order.getCartId(), orderResult.getCartId());
        assertEquals(order.getEmail(), orderResult.getEmail());
    }

    @Test
    void getOrderByIdTest() throws IOException {
        // arrange
        CartDao cartDataStore = CartDaoJDBC.getInstance();
        Cart cart = new Cart();
        cart.setId(1);
        cart.setUserId(1);
        cartDataStore.add(cart);

        OrderDao orderDataStore = OrderDaoJDBC.getInstance();
        Order order = new Order();
        order.setId(1);
        order.setCartId(cart.getId());
        order.setBillingAddress("billing");
        order.setShippingAddress("billing2");
        order.setPhoneNumber("2131231");
        order.setEmail("email@gmail.com");
        // user id is 1 from int_dbtest.sql initialization
        order.setUserId(1);
        orderDataStore.add(order);

        // act
        Order orderResult = orderDataStore.getOrderById(order.getId());

        // assert
        assertEquals(order.getId(), orderResult.getId());
    }

    @Test
    void addUserTest() throws IOException {
        // arrange
        UserDao userDao = UserDaoJDBC.getInstance();
        User user = new User();
        user.setFullName("John Doe");
        user.setEmail("myemail@gmail.com");
        userDao.add(user);

        // act
        User userResult = userDao.getUserByEmail("myemail@gmail.com");

        // assert
        assertEquals(user.getEmail(),userResult.getEmail());
        assertEquals(user.getFullName(),userResult.getFullName());
    }

    @Test
    void getUserByEmailTest() throws IOException {
        // arrange
        UserDao userDao = UserDaoJDBC.getInstance();
        String expectedEmail = "jakesmith@gmail.com";

        // act
        User userResult = userDao.getUserByEmail("jakesmith@gmail.com");

        // assert
        assertEquals(expectedEmail,userResult.getEmail());
    }

}