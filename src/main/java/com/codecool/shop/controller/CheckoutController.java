package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.implementation.OrderDaoMem;
import com.codecool.shop.model.Product;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Currency;
import java.util.List;

@WebServlet(urlPatterns = {"/checkout"})
public class CheckoutController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        // getting the order DAO
        OrderDao orderDataStore = OrderDaoMem.getInstance();
        orderDataStore.addLogEntry(orderDataStore, "Checkout");

        orderDataStore.clear();
        orderDataStore.setItems();
        List<Product> temp = orderDataStore.getItems();
        System.out.println(temp);

        context.setVariable("items", temp);

        double total=0;
        Currency orderCurrency;
        for (Product item:temp) {
            total += item.getDefaultPrice();
        }
        orderCurrency = temp.get(0).getDefaultCurrency();
        context.setVariable("total", total);
        context.setVariable("currency", orderCurrency);

        engine.process("checkout.html", context, resp.getWriter());

    }

}

