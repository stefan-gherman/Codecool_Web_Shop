package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.implementation.CartDaoJDBC;
import com.codecool.shop.dao.implementation.OrderDaoJDBC;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.ListItem;
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
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

@WebServlet(urlPatterns = {"/cart"})
public class CartController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        CartDao cartDataStore = CartDaoJDBC.getInstance();

        // adding a session cart to the session that will be available
        // regardless if the user is logged in or not
        // it has by default user 1 - which will be a default/admin user
        // which is added at the time of creating in the DB
        // the creation in the DB is also important because it gives the cart id
        HttpSession session = req.getSession(false);
        session.removeAttribute("order");

        Cart sessionCart = (Cart) session.getAttribute("cart");
        Map<ListItem, Integer> cartContents = sessionCart.getCartContents();
        String defaultCurrency = "";
        int cartSize = sessionCart.getCartNumberOfProducts();
        float cartTotal = sessionCart.getTotalSum();
        sessionCart = cartDataStore.addCartToDB(sessionCart);
        session.setAttribute("cart", sessionCart);


        for (Map.Entry<ListItem, Integer> product : sessionCart.getCartContents().entrySet()
        ) {
            defaultCurrency = product.getKey().getProductCurrency();
            break;
        }


        context.setVariable("cartSize", sessionCart.getCartNumberOfProducts());
        context.setVariable("cartContents", sessionCart.getCartContents());
        context.setVariable("cartTotal", sessionCart.getTotalSum());
        context.setVariable("defaultCurrency", defaultCurrency);
        User currentUser = (User) session.getAttribute("user");
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

                cartDataStore.add(objectId, quantity);

                //updating the session cart as well
                int productId = objectId;
                ListItem tempItem = orderDao.getListItemByProductId(productId);
                HttpSession session = req.getSession(false);
                Cart tempCart = (Cart) session.getAttribute("cart");
                tempCart.add(objectId, quantity);
                System.out.println("TTTEEEMMMMPPP cart contents len: " + tempCart.getCartNumberOfProducts());
                session.setAttribute("cart", tempCart);
                System.out.println("TTTEEEMMMMPPP cart added to session " + (Cart) ((Cart) session.getAttribute("cart")).getCartContents());

            }


        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

        if(req.getParameter("clearCart")!=null) {
            HttpSession session = req.getSession(false);
            Cart tempCart = (Cart) session.getAttribute("cart");
            User currentUser = (User) session.getAttribute("user");
            if(currentUser.getFullName() != null) {
                try {
                    cartDataStore.deleteUserCart(currentUser.getId());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            tempCart.eraseMe();
            session.setAttribute("cart", tempCart);
        }
        if (req.getParameter("saveCart") != null) {
            try {

                HttpSession session = req.getSession(false);
                if (session != null) {
                    Cart tempCart = (Cart) session.getAttribute("cart");
                    int lastCart = cartDataStore.saveInDB(Integer.parseInt(req.getParameter("saveCart")));
                    cartDataStore.saveCartAndListItems(lastCart, tempCart);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        doGet(req, resp);
    }

}
