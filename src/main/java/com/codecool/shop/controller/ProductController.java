package com.codecool.shop.controller;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.config.TemplateEngineUtil;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/"})
public class ProductController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductDao productDataStore = ProductDaoMem.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        SupplierDao supplierDao = SupplierDaoMem.getInstance();

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());


        try {
            int categoryId = getIdFromCategory(req, productCategoryDataStore);
            int supplierId = getIdFromSupplier(req, supplierDao);
//            productCategoryDataStore.find(categoryId);

            displayProducts(context, engine, resp, "category", productCategoryDataStore, productDataStore,
                    categoryId, supplierId, supplierDao);

        } catch (Exception notFound) {
            engine.process("product/notFound.html", context, resp.getWriter());

        }



    }





    /**
     * Display products from category
     *
     * @param context                  context
     * @param engine                   engine
     * @param resp                     resp
     * @param name                     category or supplier
     * @param productCategoryDataStore productCategoryDataStore
     * @param productDataStore         productDataStore
     * @param categoryId                       category id
     * @param supplierId                       supplier id
     * @throws IOException exception
     */
    private void displayProducts(WebContext context, TemplateEngine engine, HttpServletResponse resp,
                                 String name, ProductCategoryDao productCategoryDataStore,
                                 ProductDao productDataStore, int categoryId, int supplierId, SupplierDao supplierDao)
            throws IOException {

        context.setVariable(name, productCategoryDataStore.find(categoryId));
        context.setVariable("supplier", supplierDao.find(supplierId));
        context.setVariable("products", productDataStore.getBy(categoryId, supplierId));
        engine.process("product/index.html", context, resp.getWriter());
    }






    /**
     * Get id from category
     *
     * @param req                      req from HttpServletRequest
     * @param productCategoryDataStore productCategory Interface using DAO pattern
     * @return id from product category
     */
    private int getIdFromCategory(HttpServletRequest req, ProductCategoryDao productCategoryDataStore) {
        int defaultCategory = 1;
        String[] categoryArray;

        String currentProductCategory = req.getParameter("productCategory");
        if (currentProductCategory == null ) {
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

    private int getIdFromSupplier(HttpServletRequest req, SupplierDao supplierDao){

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



}




