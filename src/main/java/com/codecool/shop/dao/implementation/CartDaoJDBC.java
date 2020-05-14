package com.codecool.shop.dao.implementation;

import com.codecool.shop.connection.DBConnect;
import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.ListItem;
import com.codecool.shop.model.Product;
import com.codecool.shop.utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class CartDaoJDBC implements CartDao {

    private Cart cart;


    private static CartDaoJDBC instance = null;
    private DBConnect dbConnect = DBConnect.getInstance();

    private CartDaoJDBC() {
        this.cart = new Cart();
    }


    public static CartDaoJDBC getInstance() {
        if (instance == null) {
            instance = new CartDaoJDBC();
        }
        return instance;
    }

    public Cart addCartToDB(Cart cartToBeAdded) {
        System.out.println("Attempting to add new cart.");
        Connection conn = null;
        PreparedStatement pstmt = null;
        Cart cartToBeReturned = cartToBeAdded;
        try {
            conn = dbConnect.getConnection();
            pstmt = conn.prepareStatement("INSERT INTO carts (user_id) VALUES (1) RETURNING id");
            //pstmt.executeUpdate();
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

        System.out.println("New cart add process complete.");
        return cartToBeReturned;

    }

    @Override
    public void add(int id) throws SQLException {
        ProductDao productsList = ProductDaoJDBC.getInstance();
        Product product = productsList.find(id);
        Map<ListItem, Integer> tempMap = cart.getCartContents();
        int count = 0;
        //
        System.out.println("Product to be added Id:" + product.getId());
        System.out.println("Adding");
        if (cart.getCartContents().size() == 0) {
            cart.getCartContents().put(new ListItem(id, product.getName(), product.getImage(),
                    product.getDefaultPrice(), product.getDefaultCurrency().toString()), 1);
        } else {
            for (Map.Entry<ListItem, Integer> entry : cart.getCartContents().entrySet()
            ) {

                if (entry.getKey().getProductId() == product.getId()) {
                    System.out.println("Adding existing");
                    tempMap.put(entry.getKey(), entry.getValue() + 1);
                    break;
                } else {
                    count++;
                }

            }
        }
        if (count == cart.getCartContents().size()) {
            System.out.println("Adding non existing");
            tempMap.put(new ListItem(id, product.getName(), product.getImage(),
                    product.getDefaultPrice(), product.getDefaultCurrency().toString()), 1);
        }
        cart.setCartContents(tempMap);

    }

    @Override
    public void add(int id, int quantity) throws SQLException {
        ProductDao productsList = ProductDaoJDBC.getInstance();
        Product product = productsList.find(id);
        //
        for (Map.Entry<ListItem, Integer> entry : cart.getCartContents().entrySet()
        ) {
            if (entry.getKey().getProductId() == product.getId()) {
                cart.getCartContents().put(entry.getKey(), entry.getValue() + quantity);
            }
        }
    }

    @Override
    public void deleteUserCart(int id) throws SQLException {
        String query = "DELETE FROM carts WHERE user_id = ?";
        try {
            Connection connection = dbConnect.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,id);
            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int saveInDB(int userId) throws SQLException {
        //Delete last cart on user re-save
        this.deleteUserCart(userId);
        //First statement concerning saving the cart to its specific table
        String query = "INSERT INTO carts (user_id) VALUES (?) RETURNING id ;";
        int lastCart = 0;


        try {
            Connection connection = dbConnect.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                lastCart = resultSet.getInt("id");
            }
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Second Statement concerning saving cart and product id in the db
        System.out.println(lastCart);
        return lastCart;
    }

    @Override
    public void saveCartAndListItems(int cartId, Cart cart) {
        //Save each item
        System.out.println("It gets here.");
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
            e.printStackTrace();
        }
    }

    public boolean checkIfAlreadySaved(Integer userId) {
        return true;
    }

//    @Override
//    public void remove(int id) {
//        ProductDao productsList = ProductDaoMem.getInstance();
//        Product product = productsList.find(id);
//        //
//        if(cart.getCartContents().containsKey(product)) {
//            if(cart.getCartContents().get(product) == 1) {
//                cart.getCartContents().remove(product);
//            } else {
//                cart.getCartContents().put(product, cart.getCartContents().get(product) - 1);
//            }
//        }
//    }

    @Override
    public Map<ListItem, Integer> getCartContents() {
        System.out.println(Utils.sortMap(cart.getCartContents()));
        return Utils.sortMap(this.cart.getCartContents());
    }

//    @Override
//    public void update(int id) {
//
//    }
//
//    @Override
//    public void getAll() {
//
//    }

    @Override
    public int getCartNumberOfProducts() {
        int numberOfProudcts = 0;

        for (Map.Entry<ListItem, Integer> entry : cart.getCartContents().entrySet()
        ) {
            numberOfProudcts += entry.getValue();
        }
        return numberOfProudcts;
    }

//    @Override
//    public void eraseMe() {
//        this.cart = null;
//    }

    @Override
    public float getTotalSum() {
        float totalSum = 0;
        for (Map.Entry<ListItem, Integer> product : cart.getCartContents().entrySet()
        ) {
            totalSum += product.getValue() * product.getKey().getProductPrice();
        }
        return totalSum;
    }

    public Cart getCart() {
        return this.cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
