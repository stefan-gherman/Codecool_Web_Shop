package com.codecool.shop.controller;

import com.codecool.shop.config.Logger;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.implementation.OrderDaoMem;
import com.codecool.shop.model.ListItem;
import com.codecool.shop.model.Order;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {"/payment-method-select"})
public class PaymentMethodSelectController extends HttpServlet implements Logger {

        @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException  {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        // temporary constant until session is implemented
        final int USERID = 1;
        final int ORDERID = 1;
        final int CARTID = 1;

        OrderDao orderDao = OrderDaoMem.getInstance();

        Order order = orderDao.getOrderById(ORDERID);

        String ownerName = req.getParameter("full-name");
        String email = req.getParameter("input-email");
        String phoneNumber = req.getParameter("input-phone");
        String billingAddress = req.getParameter("billing-address");
        String shippingAddress = req.getParameter("shipping-address");

        order.setUserId(USERID);
        order.setOwnerName(ownerName);
        order.setEmail(email);
        order.setPhoneNumber(phoneNumber);
        order.setBillingAddress(billingAddress);
        order.setShippingAddress(shippingAddress);

        orderDao.update(order);

        List<ListItem> temp = new ArrayList<>();
        temp=orderDao.getItems(CARTID);
        context.setVariable("items", temp);
        double total = 0;
        String orderCurrency;
        for (ListItem item:temp) {
            total += item.getProductPrice();
        }
        orderCurrency = temp.get(0).getProductCurrency();

        // filling order information from checkout form
//        if (Validation.validateNameInput(req.getParameter("full-name"))==false) {
//            orderDataStore.setInvalidFullNameEntryMessage("A 2 to 50 character full name is required.");
//            resp.sendRedirect("checkout");
//        } else {
//            orderDataStore.setInvalidFullNameEntryMessage("");
//        }

        context.setVariable("total", total);
        context.setVariable("order", order);
        context.setVariable("currency", orderCurrency);
        engine.process("payment-method-select.html", context, resp.getWriter());
//        adminLog(req, orderDataStore, "payment-method");
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        engine.process("paymentUnavailable.html", context, resp.getWriter());

    }


}

