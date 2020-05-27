package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.implementation.CartDaoJDBC;
import com.codecool.shop.dao.implementation.OrderDaoJDBC;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.ListItem;
import com.codecool.shop.model.User;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = {"/cart"})
public class CartController extends HttpServlet {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CartController.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Cart Controller started");
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        CartDao cartDataStore = CartDaoJDBC.getInstance();
        Map<ListItem, Integer> cartContents;



        // adding a session cart to the session that will be available
        // regardless if the user is logged in or not
        // it has by default user 1 - which will be a default/admin user
        // which is added at the time of creating in the DB
        // the creation in the DB is also important because it gives the cart id
        HttpSession session = req.getSession(false);
        User currentUser = (User) session.getAttribute("user");

        if (session.getAttribute("order")!=null) session.removeAttribute("order");

        Cart sessionCart = (Cart) session.getAttribute("cart");
        if(sessionCart.getCartNumberOfProducts() == 0) {
            cartContents = new HashMap<>();
        } else {
            cartContents = sessionCart.getCartContents();
        }
        String defaultCurrency = "";
        int cartSize = sessionCart.getCartNumberOfProducts();
        float cartTotal = sessionCart.getTotalSum();
        sessionCart.setUserId(currentUser.getId());
        sessionCart = cartDataStore.add(sessionCart);
        session.setAttribute("cart", sessionCart);


        for (Map.Entry<ListItem, Integer> product : cartContents.entrySet()
        ) {
            defaultCurrency = product.getKey().getProductCurrency();
            break;
        }


        context.setVariable("cartSize", cartSize);
        context.setVariable("cartContents", sessionCart.getCartContents());
        context.setVariable("cartTotal", cartTotal);
        context.setVariable("defaultCurrency", defaultCurrency);

        String username = currentUser.getFullName();
        if(username != null) {
            context.setVariable("username", username);
            context.setVariable("userId", currentUser.getId());
        }
        else {
            context.setVariable("username", "null");
            context.setVariable("userId", "null");
        }
        engine.process("product/cart.html", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        CartDao cartDataStore = CartDaoJDBC.getInstance();
        OrderDao orderDao = OrderDaoJDBC.getInstance();
        int quantity;
        int objectId;

        try {
            if (req.getParameter("quantity") != null && req.getParameter("objectId") != null) {
                quantity = Integer.parseInt(req.getParameter("quantity"));
                objectId = Integer.parseInt(req.getParameter("objectId"));

                cartDataStore.addToCart(objectId, quantity);

                //updating the session cart as well
                int productId = objectId;
                ListItem tempItem = orderDao.getListItemByProductId(productId);
                HttpSession session = req.getSession(false);
                Cart tempCart = (Cart) session.getAttribute("cart");
                tempCart.add(objectId, quantity);
                logger.debug("Current cart length is '{}", tempCart.getCartNumberOfProducts());
                session.setAttribute("cart", tempCart);
                //System.out.println("TTTEEEMMMMPPP cart added to session " + ((Cart) session.getAttribute("cart")).getCartContents());
                logger.debug("Cart added to session: {}", ((Cart) session.getAttribute("cart")).getCartContents());

            }


        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

        if(req.getParameter("clearCart")!=null) {
            logger.info("Cart Clear started");
            HttpSession session = req.getSession(false);
            Cart tempCart = (Cart) session.getAttribute("cart");
            User currentUser = (User) session.getAttribute("user");
            if(currentUser.getFullName() != null) {
                try {
                    cartDataStore.delete(currentUser.getId());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            tempCart.eraseMe();
            session.setAttribute("cart", tempCart);
            logger.info("Cart clear stopped");
        }
        if (req.getParameter("saveCart") != null) {
            try {
                logger.info("Save cart started");
                HttpSession session = req.getSession(false);
                if (session != null) {
                    Cart tempCart = (Cart) session.getAttribute("cart");
                    int lastCart = cartDataStore.update(Integer.parseInt(req.getParameter("saveCart")));
                    cartDataStore.saveCartAndListItems(lastCart, tempCart);
                }
            } catch (SQLException throwables) {
                logger.warn("Save cart failed");
                throwables.printStackTrace();
            }
            logger.info("Cart saved successfully");
        }
        doGet(req, resp);
    }

}
