package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.implementation.OrderDaoMem;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {"/payment-details"})
public class PaymentDetailsController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

//        final int ORDERID = 1;
//        final int USERID= 1;
//        final int CARTID = 1;

        HttpSession session = req.getSession();
        Cart tempCart = (Cart) session.getAttribute("cart");
        User tempUser = (User) session.getAttribute("user");
        Order tempOrder = (Order) session.getAttribute("order");


        OrderDao orderDao = OrderDaoMem.getInstance();
        Order order = orderDao.getOrderById(tempOrder.getId());

        List<ListItem> temp = new ArrayList<>();
        temp = orderDao.getItemsByOrderId(tempOrder.getId());
        System.out.println("Items length after retrieval via ORDER ID" + temp.size());
        double total = 0;
        String orderCurrency;
        for (ListItem item:temp) {
            total += item.getProductPrice();
        }
        if (temp.size()!=0) {
            orderCurrency = temp.get(0).getProductCurrency();
        }
        else {
            orderCurrency = "";
        }
        context.setVariable("total", total);
        context.setVariable("order", order);
        context.setVariable("currency", orderCurrency);

        if (req.getParameter("payment-method").equals("card")) {
//            order.setPaymentMethodCard(true);
            engine.process("payment-details-card.html", context, resp.getWriter());
//            orderDataStore.addLogEntry(orderDataStore, "Card Details");
        }
        else if (req.getParameter("payment-method").equals("paypal")) {
//            order.setPaymentMethodPayPal(true);
            engine.process("payment-details-paypal.html", context, resp.getWriter());
//            orderDataStore.addLogEntry(orderDataStore, "PayPal Details");
        }
        else {
            engine.process("product/notFound.html", context, resp.getWriter());
        }

    }
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
//    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

//        final int ORDERID = 1;
//        final int USERID= 1;
//        final int CARTID = 1;

        HttpSession session = req.getSession();
        Cart tempCart = (Cart) session.getAttribute("cart");
        User tempUser = (User) session.getAttribute("user");
        Order tempOrder = (Order) session.getAttribute("order");


        OrderDao orderDao = OrderDaoMem.getInstance();
        Order order = orderDao.getOrderById(tempOrder.getId());

        List<ListItem> temp = new ArrayList<>();
        temp = orderDao.getItemsByOrderId(tempOrder.getId());
        double total = 0;
        String orderCurrency;
        for (ListItem item:temp) {
            total += item.getProductPrice();
        }

        if (temp.size()!=0) {
            orderCurrency = temp.get(0).getProductCurrency();
        }
        else {
            orderCurrency = "";
        }

        context.setVariable("total", total);
        context.setVariable("order", order);
        context.setVariable("currency", orderCurrency);

        if (req.getParameter("payment-method").equals("card")) {
            order.setPaymentMethodCard(true);
            engine.process("payment-details-card.html", context, resp.getWriter());
//            orderDataStore.addLogEntry(orderDataStore, "Card Details");
        }
        else if (req.getParameter("payment-method").equals("paypal")) {
            order.setPaymentMethodPayPal(true);
            engine.process("payment-details-paypal.html", context, resp.getWriter());
//            orderDataStore.addLogEntry(orderDataStore, "PayPal Details");
        }
        else {
            engine.process("product/notFound.html", context, resp.getWriter());
        }



    }
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
//
//    }

}

