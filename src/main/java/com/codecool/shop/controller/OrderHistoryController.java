package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.implementation.OrderDaoJDBC;
import com.codecool.shop.model.Cart;
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

@WebServlet(urlPatterns = {"/history"})
public class OrderHistoryController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        System.out.println("********************** Order history button pressed.");
        // temporary constants until shopping cart id is passed to /checkout
//        final int CARTID = 1;
//        final int USERID = 1;
//        final String PLACEHOLDER = "PLACEHOLDER";

        HttpSession session = req.getSession();
        Cart tempCart = (Cart) session.getAttribute("cart");
        User tempUser = (User) session.getAttribute("user");


        // getting list of orders from DB based on user ID
        OrderDao orderDao = OrderDaoJDBC.getInstance();
        List<Order> orderHistory = new ArrayList<>();
        orderHistory = orderDao.getOrderHistoryByUserId(tempUser.getId());
        context.setVariable("orderHistory", orderHistory);



        // getting info from DB to create order as Checkout button was pressed
//        List<ListItem> temp = new ArrayList<>();
//        temp = orderDao.getItems(CARTID);
//        context.setVariable("items", temp);
//        double total=0;
//        String orderCurrency;
//        for (ListItem item:temp) {
//            total += item.getProductPrice();
//        }
//        orderCurrency = temp.get(0).getProductCurrency();
//
//        // create new order object in memory and also in the DB with the limited data
//        Order tempOrder = new Order();
//        tempOrder.setCartId(CARTID);
//        tempOrder.setUserId(USERID);
//        tempOrder.setItems(temp);
//        int orderIdFromDb = orderDao.add(tempOrder);
//        // TODO add items to order_items
//
//
//
//        context.setVariable("total", total);
//        context.setVariable("currency", orderCurrency);
        engine.process("history.html", context, resp.getWriter());
    }

}

