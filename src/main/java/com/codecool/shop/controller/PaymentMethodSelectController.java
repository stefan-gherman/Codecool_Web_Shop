package com.codecool.shop.controller;

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

@WebServlet(urlPatterns = {"/payment-method-select"})
public class PaymentMethodSelectController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException  {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        OrderDao orderDataStore = OrderDaoMem.getInstance();
        Currency orderCurrency = orderDataStore.getItems().get(0).getDefaultCurrency();

        // filling order information from checkout form
        orderDataStore.setFullName(req.getParameter("full-name"));
        orderDataStore.setEmail(req.getParameter("input-email"));
        orderDataStore.setPhoneNumber(req.getParameter("input-phone"));
        orderDataStore.setBillingAddress(req.getParameter("billing-address"));
        orderDataStore.setShippingAddress(req.getParameter("shipping-address"));

        double total = Double.parseDouble(orderDataStore.getTotal());

        context.setVariable("total", total);
        context.setVariable("order", orderDataStore);
        context.setVariable("currency", orderCurrency);

        engine.process("payment-method-select.html", context, resp.getWriter());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        engine.process("paymentUnavailable.html", context, resp.getWriter());

    }

}

