package com.codecool.shop.dao.implementation;

import com.codecool.shop.connection.DBConnect;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import jdk.jfr.Category;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductCategoryJDBC implements ProductCategoryDao {
    DBConnect dbConnect = DBConnect.getInstance();
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    List<ProductCategory> productCategoryList = new ArrayList<>();


    private static ProductCategoryJDBC instance;

    private ProductCategoryJDBC() throws SQLException {

    }

    public static ProductCategoryJDBC getInstance() throws SQLException {
        if (instance == null) {
            instance = new ProductCategoryJDBC();
        }
        return instance;
    }


    @Override
    public void add(ProductCategory category) throws SQLException {
        try {
            connection = dbConnect.getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO categories (name, " +
                    "department, description) VALUES (?, ?, ?)");
            preparedStatement.setString(1, category.getName());
            preparedStatement.setString(2, category.getDepartment());
            preparedStatement.setString(3, category.getDescription());
            int insertRow = preparedStatement.executeUpdate();
        }catch(Exception ex){
            System.out.println("Error: " + ex.getMessage() + " when adding category to database.");
        } finally {
            try { resultSet.close(); } catch (Exception e) { /* ignored */ }
            try { preparedStatement.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    @Override
    public ProductCategory find(int id) throws SQLException {
        try {
            connection = dbConnect.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM categories WHERE id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                String name = resultSet.getString("name");
                String department = resultSet.getString("department");
                String description = resultSet.getString("description");
                return new ProductCategory(id, name, department, description);
            }

        } catch(Exception ex){
            System.out.println("Error: " + ex.getMessage() + " when searching for category.");
        }  finally {
            try { resultSet.close(); } catch (Exception e) { /* ignored */ }
            try { preparedStatement.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
        }
        return null;
    }

    @Override
    public void remove(int id) throws SQLException {
        try {
            connection = dbConnect.getConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM categories WHERE id = ?");
            preparedStatement.setInt(1, id);
            int deleteRow = preparedStatement.executeUpdate();
        } catch(Exception ex){
            System.out.println("Error: " + ex.getMessage() + " when deleting category from database.");
        }  finally {
            try { resultSet.close(); } catch (Exception e) { /* ignored */ }
            try { preparedStatement.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    @Override
    public List<ProductCategory> getAll() throws SQLException {
        try {
            connection = dbConnect.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM categories");
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String department = resultSet.getString("department");
                String description = resultSet.getString("description");
                productCategoryList.add(new ProductCategory(id, name, department, description));
            }
            return productCategoryList;
        }catch(Exception ex){
            System.out.println("Error: " + ex.getMessage() + " when getting all categories from database.");
        } finally {
            try { resultSet.close(); } catch (Exception e) { /* ignored */ }
            try { preparedStatement.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
        }
        return null;
    }
}
