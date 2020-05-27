package com.codecool.shop.dao.implementation;

import com.codecool.shop.connection.DBConnect;
import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.ListItem;
import com.codecool.shop.model.Product;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class CartDaoJDBC implements CartDao {

    private Cart cart;


    private static CartDaoJDBC instance = null;
    private DBConnect dbConnect = DBConnect.getInstance();
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Cart.class);

    private CartDaoJDBC() throws IOException {
        this.cart = new Cart();
    }


    public static CartDaoJDBC getInstance() throws IOException {
        if (instance == null) {
            instance = new CartDaoJDBC();
        }
        return instance;
    }

    public Cart add(Cart cartToBeAdded) {
       logger.info("Attempting to add new cart.");
        Connection conn = null;
        PreparedStatement pstmt = null;
        Cart cartToBeReturned = cartToBeAdded;
        try {
            conn = dbConnect.getConnection();
            pstmt = conn.prepareStatement("INSERT INTO carts (user_id) VALUES (?) RETURNING id");
            //pstmt.executeUpdate();
            pstmt.setInt(1, cartToBeAdded.getUserId());
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("id"));
                cartToBeReturned.setId(resultSet.getInt("id"));
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

        logger.info("New cart add process complete.");
        return cartToBeReturned;

    }

    @Override
    public void addToCart(int id, int quantity) throws SQLException, IOException {
        this.cart.add(id, quantity);
    }

    @Override
    public void delete(int id) throws SQLException {

        String query = "DELETE FROM carts WHERE user_id = ?";
        logger.info("Delete from database started");
        try (
                Connection connection = dbConnect.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            logger.info("Deleting");
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (Exception e) {
            logger.warn("Delete failed");
            e.printStackTrace();
        }
        logger.info("Delete finished");
    }

    @Override
    public Cart createCartFromQuery(int id) throws SQLException, IOException {
        //Getting a cart from the db from id
        int userCart = 0;
        String query = "SELECT id FROM carts WHERE user_id = ?";
        ProductDao productsList = ProductDaoJDBC.getInstance();

        try (
                Connection connection = dbConnect.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            logger.info("Getting cart from query");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                userCart = resultSet.getInt("id");
                break;
            }
        } catch (Exception e) {
            logger.warn("Connection error");
            e.printStackTrace();
        }
        if (userCart != 0) {
            query = "SELECT product_id FROM cart_items WHERE cart_id = ?";
            try (
                    Connection connection = dbConnect.getConnection();
                    PreparedStatement statement = connection.prepareStatement(query)
            ) {

                statement.setInt(1, userCart);
                ResultSet resultSet = statement.executeQuery();
                Cart currentCart = new Cart();
                logger.info("Getting products");
                while (resultSet.next()) {
                    Product product = productsList.find(resultSet.getInt("product_id"));
                    currentCart.addListItem(new ListItem(resultSet.getInt("product_id"), product.getName(), product.getImage(),
                            product.getDefaultPrice(), product.getDefaultCurrency().toString()));
                }
                statement.close();
                connection.close();
                return currentCart;
            } catch (Exception e) {
                logger.warn("Connection error");
                e.printStackTrace();
            }
        }
        return new Cart();
    }

    @Override
    public int update(int userId) throws SQLException {
        //Delete last cart on user re-save
        this.delete(userId);
        //First statement concerning saving the cart to its specific table
        String query = "INSERT INTO carts (user_id) VALUES (?) RETURNING id ;";
        int lastCart = 0;

        logger.info("Update started");
        try (
                Connection connection = dbConnect.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
        ) {

            logger.debug("Updating user '{}'", userId);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                lastCart = resultSet.getInt("id");
            }
        } catch (Exception e) {
            logger.warn("Connection error");
            e.printStackTrace();
        }

        //Second Statement concerning saving cart and product id in the db
        System.out.println(lastCart);
        return lastCart;
    }

    @Override
    public void saveCartAndListItems(int cartId, Cart cart) {
        //Save each item
        logger.warn("Saving item and list items");
        String query = "INSERT INTO cart_items (cart_id, product_id) VALUES (?,?);";
        try {

            for (Map.Entry<ListItem, Integer> entry : cart.getCartContents().entrySet()
            ) {
                for (int i = 0; i < entry.getValue(); i++) {
                    Connection connection = dbConnect.getConnection();
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setInt(1, cartId);
                    statement.setInt(2, entry.getKey().getProductId());
                    statement.executeUpdate();
                    statement.close();
                    connection.close();
                }
            }
        } catch (Exception e) {
            logger.warn("Connection error");
            e.printStackTrace();
        }
    }


    @Override
    public int getCartNumberOfProducts() {
        return this.cart.getCartNumberOfProducts();
    }


    public Cart getCart() {
        return this.cart;
    }

    public Cart getCartById(int cartId){
        logger.warn("Saving item and list items");
        String query = "SELECT * FROM carts WHERE id = ?";
        Cart tempCart = new Cart();

        try (
                Connection connection = dbConnect.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
        ) {

            logger.debug("Ge user '{}'", cartId);
            statement.setInt(1, cartId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                tempCart.setId(resultSet.getInt("id"));
                tempCart.setUserId(resultSet.getInt("user_id"));
                tempCart.setCartCreatedAtDate(resultSet.getDate("date_created"));
            }
        } catch (Exception e) {
            logger.warn("Connection error");
            e.printStackTrace();
        }
        return tempCart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
