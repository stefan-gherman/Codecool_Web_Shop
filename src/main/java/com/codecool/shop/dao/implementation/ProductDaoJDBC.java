package com.codecool.shop.dao.implementation;

import com.codecool.shop.connection.DBConnect;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.Result;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class ProductDaoJDBC implements ProductDao {
    private static Logger logger = LoggerFactory.getLogger(ProductDaoJDBC.class);
    private DBConnect dbConnect = DBConnect.getInstance();
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    private SupplierDao supplierDao = SupplierDaoJDBC.getInstance();
    private ProductCategoryDao productCategoryDao = ProductCategoryJDBC.getInstance();
    private static ProductDaoJDBC instance;
    private List<Product> productList = new ArrayList<>();

    private ProductDaoJDBC() throws SQLException, IOException {}

    public static ProductDaoJDBC getInstance() throws SQLException, IOException {
        if (instance == null) instance = new ProductDaoJDBC();
        return instance;
    }


    @Override
    public void add(Product product){
        try {
            preparedStatement = dbConnect.getConnection().prepareStatement("INSERT INTO products (supplier_id, " +
                    "category_id, name, description, image, price, currency) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?)");
            preparedStatement.setInt(1,product.getSupplier().getId());
            preparedStatement.setInt(2,product.getProductCategory().getId());
            preparedStatement.setString(3,product.getName());
            preparedStatement.setString(4, product.getDescription());
            preparedStatement.setString(5, product.getImage());
            preparedStatement.setFloat(6, product.getDefaultPrice());
            preparedStatement.setString(7, (product.getDefaultCurrency().toString()));
            int rows = preparedStatement.executeUpdate();
        } catch(Exception ex){
            System.out.println("Error: " + ex.getMessage() + " when adding product to the database.");
        } finally {
            try { resultSet.close(); } catch (Exception e) { /* ignored */ }
            try { preparedStatement.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    @Override
    public Product find(int id) {
        try {
            connection = dbConnect.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM products WHERE id= ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int supplierId = resultSet.getInt("supplier_id");
                int categoryId = resultSet.getInt("category_id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                String image = resultSet.getString("image");
                float price = resultSet.getFloat("price");
                String currency = resultSet.getString("currency");
                return new Product(id, supplierDao.find(supplierId),
                        productCategoryDao.find(categoryId), name, description, image, price, currency);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage() + " when finding product.");
        } finally {
            try { resultSet.close(); } catch (Exception e) { /* ignored */ }
            try { preparedStatement.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
        }
        return null;
    }

    @Override
    public void remove(int id){
        try {
            preparedStatement = dbConnect.getConnection().prepareStatement("DELETE FROM products " +
                    "WHERE id = ?");
            preparedStatement.setInt(1, id);
            int deleteRow = preparedStatement.executeUpdate();
        } catch(Exception ex){
            logger.error(ex.getMessage() + " when removing product from database.");
        } finally {
            try { resultSet.close(); } catch (Exception e) { /* ignored */ }
            try { preparedStatement.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    @Override
    public List<Product> getAll(){
        try {
            connection = dbConnect.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM products");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int supplierId = resultSet.getInt("supplier_id");
                int categoryId = resultSet.getInt("category_id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                String image = resultSet.getString("image");
                float price = resultSet.getFloat("price");
                String currency = resultSet.getString("currency");
                productList.add(new Product(id, supplierDao.find(supplierId),
                        productCategoryDao.find(categoryId), name, description, image, price, currency));
            }
        } catch(Exception ex){
            logger.error(ex.getMessage() + " when getting all products from database.");
        } finally {
            try { resultSet.close(); } catch (Exception e) { /* ignored */ }
            try { preparedStatement.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
        }
        return productList;
    }

    @Override
    public List<Product> getBy(Supplier supplier){
        try {
            productList.clear();
            connection = dbConnect.getConnection();
            productList.clear();
            preparedStatement = connection.prepareStatement("SELECT * FROM products " +
                    "WHERE supplier_id = ?");
            preparedStatement.setInt(1, supplier.getId());
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                int categoryId = resultSet.getInt("category_id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                String image = resultSet.getString("image");
                float price = resultSet.getFloat("price");
                String currency = resultSet.getString("currency");

                productList.add(new Product(id, supplierDao.find(supplier.getId()), productCategoryDao.find(categoryId), name, description,
                        image, price, currency));
            }
        } catch(Exception ex){
            logger.error(ex.getMessage() + " when getting products by supplier.");
        } finally {
            try { resultSet.close(); } catch (Exception e) { /* ignored */ }
            try { preparedStatement.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
        }
        return productList;
    }

    @Override
    public List<Product> getBy(ProductCategory productCategory) throws SQLException {
        try {
            productList.clear();
            connection = dbConnect.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM products " +
                    "WHERE category_id = ?");
            preparedStatement.setInt(1, productCategory.getId());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int supplierId = resultSet.getInt("supplier_id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                String image = resultSet.getString("image");
                float price = resultSet.getFloat("price");
                String currency = resultSet.getString("currency");

                productList.add(new Product(id, supplierDao.find(supplierId), productCategory, name, description,
                        image, price, currency));
            }
        } catch(Exception ex){
            logger.error(ex.getMessage() + " when getting products by category.");
        } finally {
            try { resultSet.close(); } catch (Exception e) { /* ignored */ }
            try { preparedStatement.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
        }
        return productList;

    }

    @Override
    public List<Product> getBy(int categoryId, int supplierId){
        try {
            productList.clear();
            connection = dbConnect.getConnection();
            if(supplierId != 0){
                preparedStatement = connection.prepareStatement("SELECT * FROM products " +
                        "WHERE category_id = ? AND supplier_id = ?");
                preparedStatement.setInt(1, categoryId);
                preparedStatement.setInt(2, supplierId);
            } else {
                getBy(productCategoryDao.find(categoryId));
            }
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                String image = resultSet.getString("image");
                float price = resultSet.getFloat("price");
                String currency = resultSet.getString("currency");
                productList.add(new Product(id, supplierDao.find(supplierId), productCategoryDao.find(categoryId), name, description,
                        image, price, currency));
            }
        } catch(Exception ex){
            logger.error(ex.getMessage() + " when getting products by category id and supplier id.");
        } finally {
            try { resultSet.close(); } catch (Exception e) { /* ignored */ }
            try { preparedStatement.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
        }
        return productList;
    }



}
