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
import java.util.List;

public class SupplierDaoJDBC implements SupplierDao {
    DBConnect dbConnect = DBConnect.getInstance();
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    List<Supplier> supplierList = new ArrayList<>();
    private static SupplierDaoJDBC instance;
    private static Logger logger = LoggerFactory.getLogger(SupplierDaoJDBC.class);

    private SupplierDaoJDBC() throws IOException { }

    public static SupplierDaoJDBC getInstance() throws IOException {
        if (instance == null) instance = new SupplierDaoJDBC();
        return instance;
    }

    @Override
    public void add(Supplier supplier) {
        try {
            connection = dbConnect.getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO suppliers (name, description)" +
                    "VALUES (?, ?)");
            preparedStatement.setString(1, supplier.getName());
            preparedStatement.setString(2, supplier.getDescription());
            int insertRow = preparedStatement.executeUpdate();
        } catch (Exception ex) {
            logger.error(ex.getMessage() + " when adding supplier.");
        } finally {
            try { resultSet.close(); } catch (Exception e) { /* ignored */ }
            try { preparedStatement.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    @Override
    public Supplier find(int id) {
        try {
            connection = dbConnect.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM suppliers WHERE id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                return new Supplier(id, name, description);
            }

        } catch(Exception ex){
            logger.error(ex.getMessage() + " when searching for category.");
        } finally {
            try { resultSet.close(); } catch (Exception e) { /* ignored */ }
            try { preparedStatement.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
        }
        return null;

    }

    @Override
    public void remove(int id) {
        try {
            connection = dbConnect.getConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM suppliers WHERE id = ?");
            preparedStatement.setInt(1, id);
            int deleteRow = preparedStatement.executeUpdate();
        } catch (Exception ex) {
            logger.error(ex.getMessage() + " when removing supplier from database.");
        } finally {
            try { resultSet.close(); } catch (Exception e) { /* ignored */ }
            try { preparedStatement.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    @Override
    public List<Supplier> getAll() {
        try {
            supplierList.clear();
            connection = dbConnect.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM suppliers");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                supplierList.add(new Supplier(id, name, description));
            }
            return supplierList;
        } catch (Exception ex) {
            logger.error(ex.getMessage() + " when getting all suppliers from database.");
        } finally {
            try { resultSet.close(); } catch (Exception e) { /* ignored */ }
            try { preparedStatement.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
        }
        return null;
    }
}
