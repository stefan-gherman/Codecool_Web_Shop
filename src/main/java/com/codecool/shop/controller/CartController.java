package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.implementation.CartDaoMem;
import com.codecool.shop.model.Product;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/cart"})
public class CartController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        CartDao cartDataStore = CartDaoMem.getInstance();
        int cartSize = cartDataStore.getCartNumberOfProducts();
        float cartTotal = cartDataStore.getTotalSum();
        Map<Product, Integer> cartContents = cartDataStore.getCartContents();
        String defaultCurrency="";
        int valueChanged;
        int objectId;



        for (Map.Entry<Product, Integer> product: cartContents.entrySet()
             ) {
            defaultCurrency = product.getKey().getDefaultCurrency().toString();
            break;
        }

        try{
            if( req.getParameter("valueChanged")!= null && req.getParameter("objectId")!=null) {
                valueChanged = Integer.parseInt(req.getParameter("valueChanged"));
                objectId = Integer.parseInt(req.getParameter("objectId"));

                cartDataStore.add(objectId, valueChanged);

                cartSize = cartDataStore.getCartNumberOfProducts();
                cartTotal = cartDataStore.getTotalSum();
                cartContents = cartDataStore.getCartContents();
            }


        } catch ( Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

        System.out.println(cartContents);
        System.out.println(req.getParameter("valueChanged"));
        System.out.println(req.getParameter("objectId"));
        context.setVariable("cartSize",cartSize);
        context.setVariable("cartContents", cartContents);
        context.setVariable("cartTotal", cartTotal);
        context.setVariable("defaultCurrency", defaultCurrency);
        engine.process("product/cart.html", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
