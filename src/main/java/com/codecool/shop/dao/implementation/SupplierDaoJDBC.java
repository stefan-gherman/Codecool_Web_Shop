package com.codecool.shop.dao.implementation;

import com.codecool.shop.connection.DBConnect;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierDaoJDBC implements SupplierDao {
    DBConnect dbConnect = DBConnect.getInstance();
    PreparedStatement preparedStatement = null;
    List<Supplier> supplierList = new ArrayList<>();

    private static SupplierDaoJDBC instance;

    private SupplierDaoJDBC() {

    }

    public static SupplierDaoJDBC getInstance() {
        if (instance == null) {
            instance = new SupplierDaoJDBC();
        }
        return instance;
    }

    @Override
    public void add(Supplier supplier) throws SQLException {
        try {
            preparedStatement = dbConnect.getConnection().prepareStatement("INSERT INTO suppliers (name, description)" +
                    "VALUES (?, ?)");
            preparedStatement.setString(1, supplier.getName());
            preparedStatement.setString(2, supplier.getDescription());
            int insertRow = preparedStatement.executeUpdate();
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage() + " when adding supplier.");
        } finally {
            if (dbConnect.getConnection() != null) {
                dbConnect.getConnection().close();
            }
        }
    }

    @Override
    public Supplier find(int id) throws SQLException {
        try {
            preparedStatement = dbConnect.getConnection().prepareStatement("SELECT * FROM suppliers WHERE id = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                return new Supplier(id, name, description);
            }

        } catch(Exception ex){
            System.out.println("Error: " + ex.getMessage() + " when searching for category.");
        }  finally {
            if(dbConnect.getConnection() != null){
                dbConnect.getConnection().close();
            }
        }
        return null;

    }

    @Override
    public void remove(int id) throws SQLException {
        try {
            preparedStatement = dbConnect.getConnection().prepareStatement("DELETE FROM suppliers WHERE id = ?");
            preparedStatement.setInt(1, id);
            int deleteRow = preparedStatement.executeUpdate();
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage() + " when removing supplier from database.");
        } finally {
            if (dbConnect.getConnection() != null) {
                dbConnect.getConnection().close();
            }
        }
    }

    @Override
    public List<Supplier> getAll() throws SQLException {
        try {
            supplierList.clear();
            preparedStatement = dbConnect.getConnection().prepareStatement("SELECT * FROM suppliers");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                supplierList.add(new Supplier(id, name, description));
            }
            return supplierList;
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage() + " when getting all suppliers from database.");
        } finally {
            if (dbConnect.getConnection() != null) {
                dbConnect.getConnection().close();
            }
        }
        return null;
    }
}
