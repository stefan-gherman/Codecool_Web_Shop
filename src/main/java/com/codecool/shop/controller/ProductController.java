package com.codecool.shop.controller;


import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.CartDaoJDBC;
import com.codecool.shop.dao.implementation.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.dao.implementation.*;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.model.Product;
import com.codecool.shop.dao.implementation.SupplierDaoMem;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;


@WebServlet(urlPatterns = {"/"})
public class ProductController extends HttpServlet {
    ProductCategoryDao productCategoryDataStore = null;
    ProductDao productDataStore = null;
    SupplierDao supplierDao = null;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            productDataStore = ProductDaoJDBC.getInstance();
            productCategoryDataStore = ProductCategoryJDBC.getInstance();
            supplierDao = SupplierDaoJDBC.getInstance();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            System.out.println(supplierDao.find(2));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        CartDao cartDataStore = CartDaoJDBC.getInstance();
        int cartSize = cartDataStore.getCartNumberOfProducts();


        //System.out.println(req.getParameter("addToCart"));
        if(req.getParameter("addToCart")!=null) {
            try{
                int prodIdParses = Integer.parseInt(req.getParameter("addToCart"));
                System.out.println(prodIdParses);
                cartDataStore.add(prodIdParses);
                System.out.println("Current in cart" +cartDataStore.getCartContents());
                cartSize = cartDataStore.getCartNumberOfProducts();
                System.out.println("Current size " +cartSize);
            } catch (Exception e) {
                System.out.println(e.getStackTrace());
            }
        }

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        try {
            int categoryId = getIdFromCategory(req, productCategoryDataStore);
            int supplierId = getIdFromSupplier(req, supplierDao);
            displayProducts(context, engine, resp, productCategoryDataStore, productDataStore,
                    categoryId, supplierId, cartSize, supplierDao );
        } catch (Exception notFound) {
            engine.process("product/notFound.html", context, resp.getWriter());

        }

    }


    /**
     * Display products from category or supplier
     *
     * @param context                  context
     * @param engine                   engine
     * @param resp                     resp
     * @param productCategoryDataStore productCategoryDataStore
     * @param productDataStore         productDataStore
     * @param categoryId                       category id
     * @param supplierId                       supplier id
     * @throws IOException exception
     */
    private void displayProducts(WebContext context, TemplateEngine engine, HttpServletResponse resp,
                                 ProductCategoryDao productCategoryDataStore,
                                 ProductDao productDataStore, int categoryId, int supplierId,
                                 int cartSize, SupplierDao supplierDao)
            throws IOException, SQLException {

        context.setVariable("category", productCategoryDataStore.find(categoryId));
        //Suggestion
        if(supplierDao.find(supplierId) == null) {
            //System.out.println("Supplier null");
            context.setVariable("supplier", "all" );
        }
        else {
            //System.out.println("Supplier not null");
            context.setVariable("supplier", supplierDao.find(supplierId));
        }
        context.setVariable("products", productDataStore.getBy(categoryId, supplierId));
        context.setVariable("cartSize", cartSize);
        engine.process("product/index.html", context, resp.getWriter());
    }






    /**
     * Get id from category
     *
     * @param req                      req from HttpServletRequest
     * @param productCategoryDataStore productCategory Interface using DAO pattern
     * @return category id
     */
    private int getIdFromCategory(HttpServletRequest req, ProductCategoryDao productCategoryDataStore) throws SQLException {
        int defaultCategory = 1;
        String[] categoryArray;

        String currentProductCategory = req.getParameter("productCategory");
        if (currentProductCategory == null) {
            return defaultCategory;
        } else {
            categoryArray = currentProductCategory.split("-");
        }
        List<ProductCategory> productCategoryList = productCategoryDataStore.getAll();
        for (ProductCategory productCategory : productCategoryList) {
            for (String word : categoryArray) {
                if (productCategory.getName().toLowerCase().contains(word)) {
                    return productCategory.getId();
                }
            }
        }
        return 0;

    }

    /**
     * Get id from supplier
     * @param req req from HttpServletRequest
     * @param supplierDao supplier interface using DAO pattern
     * @return supplier id
     */
    private int getIdFromSupplier(HttpServletRequest req, SupplierDao supplierDao) throws SQLException {

        if(req.getParameter("supplier") == null){
            return 0;
        }

        String currentSupplier = req.getParameter("supplier");
        String[] supplierArray = currentSupplier.split("/s++");

        List<Supplier> supplierList = supplierDao.getAll();
        for (Supplier supplier : supplierList) {
            for (String word : supplierArray) {
                if (supplier.getName().toLowerCase().contains(word)) {
                    return supplier.getId();
                }
            }
        }

        return 0;



    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}




