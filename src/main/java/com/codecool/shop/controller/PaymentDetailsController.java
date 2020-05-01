package com.codecool.shop.controller;

import com.codecool.shop.config.Logger;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.implementation.OrderDaoMem;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Currency;

@WebServlet(urlPatterns = {"/payment-details"})
public class PaymentDetailsController extends HttpServlet implements Logger {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException  {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        OrderDao orderDataStore = OrderDaoMem.getInstance();
        Currency orderCurrency = orderDataStore.getItems().get(0).getDefaultCurrency();

        double total = Double.parseDouble(orderDataStore.getTotal());

        context.setVariable("total", total);
        context.setVariable("order", orderDataStore);
        context.setVariable("currency", orderCurrency);

        if (req.getParameter("payment-method").equals("card")) {
            engine.process("payment-details-card.html", context, resp.getWriter());
        }
        else if (req.getParameter("payment-method").equals("paypal")) {
            engine.process("payment-details-paypal.html", context, resp.getWriter());
        }
        else {
            engine.process("product/notFound.html", context, resp.getWriter());
        }
        adminLog(req, orderDataStore, "payment-details");

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        OrderDao orderDataStore = OrderDaoMem.getInstance();
        engine.process("paymentUnavailable.html", context, resp.getWriter());


    }


}

