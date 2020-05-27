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
import com.codecool.shop.utils.Utils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

@WebServlet(urlPatterns = {"/payment-confirmation"})
public class ConfirmationController extends HttpServlet {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ConfirmationController.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        logger.info("Reached confirmation page - doPost.");

        HttpSession session = req.getSession(false);
        Cart tempCart = (Cart) session.getAttribute("cart");
        User tempUser = (User) session.getAttribute("user");
        Order tempOrder = (Order) session.getAttribute("order");

        double total = 0;
        String orderCurrency = "";
        for (ListItem item:tempOrder.getItems()) {
            total += item.getProductPrice();
        }
        if (tempOrder.getItems().size()!=0) orderCurrency = tempOrder.getItems().get(0).getProductCurrency();

        OrderDao orderDao = OrderDaoJDBC.getInstance();
        tempOrder.setStatus("Shipped");
        tempOrder.setTotal(total);
        orderDao.update(tempOrder);

        context.setVariable("total", total);
        context.setVariable("order", tempOrder);
        context.setVariable("currency", orderCurrency);
        User currentUser = (User) session.getAttribute("user");
        String username = currentUser.getFullName();
        if (username != null) {
            context.setVariable("username", username);
        } else {
            context.setVariable("username", "null");
        }

        int cartSize = 0;
        tempCart = (Cart) session.getAttribute("cart");
        if (tempCart == null) {
            cartSize = 0;
        } else {
            cartSize = tempCart.getCartNumberOfProducts();
            context.setVariable("cartSize", cartSize);
        }
        logger.info(String.format("Order %d payment confirmed.", tempOrder.getId()));
        engine.process("payment-confirmation.html", context, resp.getWriter());

        // clearing the cart and the order from the session as the purchase is completed and backup isn't needed
        session.removeAttribute("cart");
        session.removeAttribute("order");

        // send confirmation email to customer
        sendEmailConfirmation(tempOrder.getEmail(), tempOrder.getOwnerName(), tempOrder.getId(), String.valueOf(total));

    }


    private void sendEmailConfirmation(String custEmail, String fullName, int orderId, String total) {
        String to = custEmail;
        String host = "smtp.gmail.com";
        String subject = "EDUCATIONAL PROJECT - shop order confirmation";
        String body =  "EDUCATIONAL PROJECT - content with order details";
        final String user = "ionionescu2020demo@gmail.com";
        final String pass = "notverystrongpassword";

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                        return new javax.mail.PasswordAuthentication(user,pass);
                    }
                });

        //Compose the message
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
            message.setSubject("EDUCATIONAL PROJECT - WEB SHOP ORDER CONFIRMATION");
            message.setText("**** EDUCATIONAL PROJECT**** NOT AN ACTUAL ORDER\n" +
                    "Hello " + fullName +",\n" +
                    "\n" +
                    "Thanks for purchasing from our shop.\n" +
                    "Your order, ID: " + orderId + ", totalling " + total + " USD, was processed successfully" +
                    " and we'll be shipping it shortly.\n" +
                    "If you have any questions, you can always reach us at orders@webshop.com\n" +
                    "\n" +
                    "Thanks again for the business and have a wonderful day! \n" +
                    "The Web Shop Team"

            );

            //send the message
            Transport.send(message);

            System.out.println("message sent successfully...");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void jsonify(OrderDao orderDataStore) throws IOException {

        // get the path from Utils for the log file to be saved in (different paths based on computer)
        String path = Utils.getPathForLogs();

        FileWriter file = new FileWriter(path + "log.txt", true);
        JSONObject obj = new JSONObject();
//        obj.put("ID", orderDataStore.getId());
        obj.put("ID", "545454");
        JSONArray items = new JSONArray();
//        for (Product item : orderDataStore.getItems()){
//            items.add(item.getName());
//        }
//        obj.put("Items", items);

        try {
            file.write(obj.toJSONString());
            file.write("\n");
            System.out.println("Successfully copied JSON Object to file.");
            System.out.println("\nJSON Object: " + obj);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            file.flush();
            file.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        logger.info("Reached confirmation page - doGet.");

//        OrderDao orderDataStore = OrderDaoMem.getInstance();
//        Currency orderCurrency = orderDataStore.getItems().get(0).getDefaultCurrency();
//        context.setVariable("order", orderDataStore);
//        context.setVariable("currency", orderCurrency);

        engine.process("payment-confirmation.html", context, resp.getWriter());

    }

}

