package com.codecool.shop.controller;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/checkout"})
public class CheckoutController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        System.out.println("********************** Checkout button pressed.");
        // temporary constants until shopping cart id is passed to /checkout
//        final int CARTID = 1;
//        final int USERID = 1;
        OrderDao orderDao = OrderDaoJDBC.getInstance();
        HttpSession session = req.getSession(false);
        System.out.println("*************************** Got session");
        Cart tempCart = (Cart) session.getAttribute("cart");
        User tempUserOrig = (User) session.getAttribute("user");
        System.out.println("**************************** user from session ID: " + tempUserOrig.getId());
        System.out.println("**************************** Got cart from session: " + tempCart);
        System.out.println("**************************** Got cart contents from session: " + tempCart.getCartContents());
        User tempUser = (User) session.getAttribute("user");


        // create new order object in memory and also in the DB with the limited data
        Order tempOrder = new Order();

        tempOrder.setCartId(tempCart.getId());
        tempOrder.setUserId(tempUser.getId());

        // getting items from cart and putting them into the order
        Map<ListItem, Integer> tempMap = tempCart.getCartContents();
        List<ListItem> temp = new ArrayList<>();
        for (Map.Entry<ListItem, Integer> entry : tempMap.entrySet()) {
            System.out.println("WWWOOOWWWOOOWWWOOOWWWOOO" + entry.getKey() + " = " + entry.getValue());
            for (int i=1; i<=entry.getValue(); i++) {
                temp.add(entry.getKey());
            }
        }
        System.out.println("From Checkout Controller - length of items list: " + temp.size());
        tempOrder.setItems(temp);

        // creating the new order in the DB and in the session
        int orderIdFromDb = orderDao.add(tempOrder);
        tempOrder.setId(orderIdFromDb);
        session.setAttribute("order", tempOrder);

        // adding the items in the order to the DB
        orderDao.addToOrderItems(tempOrder);


        // setting up info for the Checkout page
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

        for (ListItem item :
                tempOrder.getItems()) {
            System.out.println(item.getProductName() + " " + item.getProductPrice());

        }

        context.setVariable("items", tempOrder.getItems());
        context.setVariable("total", total);
        context.setVariable("currency", orderCurrency);
        engine.process("checkout.html", context, resp.getWriter());
    }

}

