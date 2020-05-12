package com.codecool.shop.controller;

import com.codecool.shop.config.Logger;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.implementation.CartDaoMem;
import com.codecool.shop.dao.implementation.OrderDaoMem;
import com.codecool.shop.model.Order;
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

@WebServlet(urlPatterns = {"/payment-method-select"})
public class PaymentMethodSelectController extends HttpServlet implements Logger {

        @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException  {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        CartDao cartDataStore = CartDaoMem.getInstance();

        Map<Product, Integer> tempHashMap = cartDataStore.getCartContents();
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

        // filling order information from checkout form
//        if (Validation.validateNameInput(req.getParameter("full-name"))==false) {
//            orderDataStore.setInvalidFullNameEntryMessage("A 2 to 50 character full name is required.");
//            resp.sendRedirect("checkout");
//        } else {
//            orderDataStore.setInvalidFullNameEntryMessage("");
//        }
        String fullName = req.getParameter("full-name");
        String email = req.getParameter("input-email");
        String phoneNumber = req.getParameter("input-phone");
        String billingAddress = req.getParameter("billing-address");
        String shippingAddress = req.getParameter("shipping-address");

        OrderDao orderDataStore = OrderDaoMem.getInstance();
        int cartId = 24;
        orderDataStore.add(fullName, cartId, phoneNumber, email,  billingAddress, shippingAddress);


        Order order = new Order();
        context.setVariable("total", total);
//        context.setVariable("order", orderDataStore);
        context.setVariable("order", order);
        context.setVariable("currency", orderCurrency);
        engine.process("payment-method-select.html", context, resp.getWriter());
//        adminLog(req, orderDataStore, "payment-method");
    }





//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException  {
//        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
//        WebContext context = new WebContext(req, resp, req.getServletContext());
//
//        OrderDao orderDataStore = OrderDaoMem.getInstance();
//        Currency orderCurrency = orderDataStore.getItems().get(0).getDefaultCurrency();
//        orderDataStore.addLogEntry(orderDataStore, "Payment Method Select");
//
//        // filling order information from checkout form
//        if (Validation.validateNameInput(req.getParameter("full-name"))==false) {
//            orderDataStore.setInvalidFullNameEntryMessage("A 2 to 50 character full name is required.");
//            resp.sendRedirect("checkout");
//        } else {
//            orderDataStore.setInvalidFullNameEntryMessage("");
//        }
//        orderDataStore.setFullName(req.getParameter("full-name"));
//        orderDataStore.setEmail(req.getParameter("input-email"));
//        orderDataStore.setPhoneNumber(req.getParameter("input-phone"));
//        orderDataStore.setBillingAddress(req.getParameter("billing-address"));
//        orderDataStore.setShippingAddress(req.getParameter("shipping-address"));
//
//        double total = Double.parseDouble(orderDataStore.getTotal());
//
//        context.setVariable("total", total);
//        context.setVariable("order", orderDataStore);
//        context.setVariable("currency", orderCurrency);
//        engine.process("payment-method-select.html", context, resp.getWriter());
//        adminLog(req, orderDataStore, "payment-method");
//    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
//        OrderDao orderDataStore = OrderDaoMem.getInstance();

        engine.process("paymentUnavailable.html", context, resp.getWriter());

    }


}

