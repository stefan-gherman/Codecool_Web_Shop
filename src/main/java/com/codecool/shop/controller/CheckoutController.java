package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.implementation.CartDaoJDBC;
import com.codecool.shop.dao.implementation.OrderDaoJDBC;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.ListItem;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/checkout"})
public class CheckoutController extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(CheckoutController.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        logger.info("Checkout button pressed. Checkout page reached.");
        OrderDao orderDao = OrderDaoJDBC.getInstance();
        HttpSession session = req.getSession(false);
        Cart tempCart = (Cart) session.getAttribute("cart");
        User tempUser = (User) session.getAttribute("user");

        // create new order object in memory and also in the DB with the limited data
        Order tempOrder = new Order();
        tempOrder.setCartId(tempCart.getId());
        tempOrder.setUserId(tempUser.getId());
        logger.info("Temporary order object created and populated with user ID and cart ID from the session.");

        // getting items from cart and putting them into the order
        Map<ListItem, Integer> tempMap = tempCart.getCartContents();
        List<ListItem> temp = new ArrayList<>();
        for (Map.Entry<ListItem, Integer> entry : tempMap.entrySet()) {
            for (int i = 1; i <= entry.getValue(); i++) {
                temp.add(entry.getKey());
            }
        }
        tempOrder.setItems(temp);
        logger.info("Items added to order.");


        // setting up info for the Checkout page, the total is needed at the update as well
        double total = 0;
        String orderCurrency = "";
        for (ListItem item : tempOrder.getItems()) {
            total += item.getProductPrice();
        }
        if (tempOrder.getItems().size() != 0) orderCurrency = tempOrder.getItems().get(0).getProductCurrency();

        // creating the new order in the DB and in the session
        int orderIdFromDb = orderDao.add(tempOrder);
        tempOrder.setId(orderIdFromDb);
        tempOrder.setCartId(tempCart.getId());
        tempOrder.setUserId(tempUser.getId());
        tempOrder.setStatus("Checked out");
        tempOrder.setOwnerName(tempUser.getFullName());
        tempOrder.setPhoneNumber(tempUser.getPhoneNumber());
        tempOrder.setEmail(tempUser.getEmail());
        tempOrder.setBillingAddress(tempUser.getBillingAddress());
        tempOrder.setShippingAddress(tempUser.getShippingAddress());
        tempOrder.setTotal(total);
        session.setAttribute("order", tempOrder);
        orderDao.update(tempOrder);
        logger.info("Temporary order saved in the session and the DB.");

        // adding the items in the order to the DB
        orderDao.addToOrderItems(tempOrder);


        int cartSize = 0;
        tempCart = (Cart) session.getAttribute("cart");
        if (tempCart == null) {
            cartSize = 0;
        } else {
            cartSize = tempCart.getCartNumberOfProducts();
            context.setVariable("cartSize", cartSize);
        }
        context.setVariable("user", tempUser);
        context.setVariable("items", tempOrder.getItems());
        context.setVariable("total", total);
        context.setVariable("currency", orderCurrency);
        User currentUser = (User) session.getAttribute("user");
        String username = currentUser.getFullName();
        if (username != null) {
            context.setVariable("username", username);
        } else {
            context.setVariable("username", "null");
        }
        logger.info("Generating checkout.html page.");
        engine.process("checkout.html", context, resp.getWriter());

    }

}

