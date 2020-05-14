package com.codecool.shop.controller;

import com.codecool.shop.config.Logger;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.implementation.OrderDaoJDBC;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.ListItem;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.User;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"/payment-method-select"})
public class PaymentMethodSelectController extends HttpServlet implements Logger {

        @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException  {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        HttpSession session = req.getSession();
        Cart tempCart = (Cart) session.getAttribute("cart");
        User tempUser = (User) session.getAttribute("user");
        Order tempOrder = (Order) session.getAttribute("order");

        OrderDao orderDao = OrderDaoJDBC.getInstance();
        Order order = orderDao.getOrderById(tempOrder.getId());

        // get info from previous checkout form
        String ownerName = req.getParameter("full-name");
        String email = req.getParameter("input-email");
        String phoneNumber = req.getParameter("input-phone");
        String billingAddress = req.getParameter("billing-address");
        String shippingAddress = req.getParameter("shipping-address");

        // attach form info to the order
        order.setUserId(tempUser.getId());
        order.setOwnerName(ownerName);
        order.setEmail(email);
        order.setPhoneNumber(phoneNumber);
        order.setBillingAddress(billingAddress);
        order.setShippingAddress(shippingAddress);

        // update the new order details in the DB and in the session
        orderDao.update(order);
        session.setAttribute("order", order);

        // setting up info for display on html - currency as string for demo
        double total = 0;
        String orderCurrency = "";
        for (ListItem item:tempOrder.getItems()) {
            total += item.getProductPrice();
        }
        if (tempOrder.getItems().size()!=0) orderCurrency = tempOrder.getItems().get(0).getProductCurrency();

        context.setVariable("total", total);
        context.setVariable("order", tempOrder);
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

