package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.implementation.CartDaoMemJDBC;
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
import java.util.Currency;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/checkout"})
public class CheckoutController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        // getting the order DAO
//        OrderDao orderDataStore = OrderDaoMem.getInstance();
        CartDaoMemJDBC cartDataStore = CartDaoMemJDBC.getInstance();

//        orderDataStore.addLogEntry(orderDataStore, "Checkout");

//        orderDataStore.clear();
//        orderDataStore.setItems();
        Map<Product, Integer> tempHashMap = cartDataStore.getCartContents();
//        List<Product> temp = orderDataStore.getItems();
        List<Product> temp = new ArrayList<>();
        for (Product item: tempHashMap.keySet()) {
            temp.add(item);
        }
        System.out.println(temp);

        context.setVariable("items", temp);

        double total=0;
        Currency orderCurrency;
        for (Product item:temp) {
            total += item.getDefaultPrice();
        }
        orderCurrency = temp.get(0).getDefaultCurrency();
//        context.setVariable("order", orderDataStore);
        context.setVariable("total", total);
        context.setVariable("currency", orderCurrency);

        engine.process("checkout.html", context, resp.getWriter());

    }







//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//
//        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
//        WebContext context = new WebContext(req, resp, req.getServletContext());
//
//        // getting the order DAO
//        OrderDao orderDataStore = OrderDaoMem.getInstance();
//        orderDataStore.addLogEntry(orderDataStore, "Checkout");
//
//        orderDataStore.clear();
//        orderDataStore.setItems();
//        List<Product> temp = orderDataStore.getItems();
//        System.out.println(temp);
//
//        context.setVariable("items", temp);
//
//        double total=0;
//        Currency orderCurrency;
//        for (Product item:temp) {
//            total += item.getDefaultPrice();
//        }
//        orderCurrency = temp.get(0).getDefaultCurrency();
//        context.setVariable("order", orderDataStore);
//        context.setVariable("total", total);
//        context.setVariable("currency", orderCurrency);
//
//        engine.process("checkout.html", context, resp.getWriter());
//
//    }

}

