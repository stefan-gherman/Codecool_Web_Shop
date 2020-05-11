package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/payment-details"})
public class PaymentDetailsController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException  {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

//        OrderDao orderDataStore = OrderDaoMem.getInstance();
//        Currency orderCurrency = orderDataStore.getItems().get(0).getDefaultCurrency();
//        double total = Double.parseDouble(orderDataStore.getTotal());
//        context.setVariable("total", total);
//        context.setVariable("order", orderDataStore);
//        context.setVariable("currency", orderCurrency);
//
//        if (req.getParameter("payment-method").equals("card")) {
//            orderDataStore.setPaymentMethodCard(true);
//            engine.process("payment-details-card.html", context, resp.getWriter());
//            orderDataStore.addLogEntry(orderDataStore, "Card Details");
//        }
//        else if (req.getParameter("payment-method").equals("paypal")) {
//            orderDataStore.setPaymentMethodPayPal(true);
//            engine.process("payment-details-paypal.html", context, resp.getWriter());
//            orderDataStore.addLogEntry(orderDataStore, "PayPal Details");
//        }
//        else {
//            engine.process("product/notFound.html", context, resp.getWriter());
//        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

//        OrderDao orderDataStore = OrderDaoMem.getInstance();
//
//        if (orderDataStore.getPaymentMethodCard()==true) {
//            Currency orderCurrency = orderDataStore.getItems().get(0).getDefaultCurrency();
//            double total = Double.parseDouble(orderDataStore.getTotal());
//            context.setVariable("total", total);
//            context.setVariable("order", orderDataStore);
//            context.setVariable("currency", orderCurrency);
//            engine.process("payment-details-card.html", context, resp.getWriter());
//            orderDataStore.addLogEntry(orderDataStore, "Card Details");
//        }
//        else if (orderDataStore.getPaymentMethodPayPal()==true) {
//            Currency orderCurrency = orderDataStore.getItems().get(0).getDefaultCurrency();
//            double total = Double.parseDouble(orderDataStore.getTotal());
//            context.setVariable("total", total);
//            context.setVariable("order", orderDataStore);
//            context.setVariable("currency", orderCurrency);
//            engine.process("payment-details-paypal.html", context, resp.getWriter());
//            orderDataStore.addLogEntry(orderDataStore, "PayPal Details");
//        }
//        else {
//            engine.process("paymentUnavailable.html", context, resp.getWriter());
//        }

    }

}

