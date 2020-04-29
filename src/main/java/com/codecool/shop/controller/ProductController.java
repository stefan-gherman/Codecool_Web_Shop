package com.codecool.shop.controller;

import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.implementation.CartDaoMem;
import com.codecool.shop.dao.implementation.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.model.ProductCategory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@WebServlet(urlPatterns = {"/"})
public class ProductController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductDao productDataStore = ProductDaoMem.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        CartDao cartDataStore = CartDaoMem.getInstance();
        int cartSize = cartDataStore.getCartNumberOfProducts();

        System.out.println(req.getParameterMap());
        if(req.getParameter("addToCart")!=null) {
            try{
                int prodIdParses = Integer.parseInt(req.getParameter("addToCart"));
                System.out.println(prodIdParses);
                cartDataStore.add(prodIdParses);
                System.out.println(cartDataStore.getCartContents());
                cartSize = cartDataStore.getCartNumberOfProducts();
                System.out.println(cartSize);
            } catch (Exception e) {
                System.out.println(e.getStackTrace());
            }
        }

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());


        try {

            int id = getIdFromCategory(req, productCategoryDataStore);
            displayProducts(context, engine, resp, "category", productCategoryDataStore, productDataStore,
                    id, cartSize);
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
     * @param name                     category or supplier
     * @param productCategoryDataStore productCategoryDataStore
     * @param productDataStore         productDataStore
     * @param id                       category id
     * @throws IOException exception
     */
    private void displayProducts(WebContext context, TemplateEngine engine, HttpServletResponse resp,
                                 String name, ProductCategoryDao productCategoryDataStore,
                                 ProductDao productDataStore, int id, int cartSize) throws IOException {

        context.setVariable(name, productCategoryDataStore.find(id));
        context.setVariable("products", productDataStore.getBy(productCategoryDataStore.find(id)));
        context.setVariable("cartSize", cartSize);
        engine.process("product/index.html", context, resp.getWriter());
    }

    /**
     * Get id from category or supplier
     *
     * @param req                      req from HttpServletRequest
     * @param productCategoryDataStore productCategory Interface using DAO pattern
     * @return id from product category
     */
    private int getIdFromCategory(HttpServletRequest req, ProductCategoryDao productCategoryDataStore) {
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
}



