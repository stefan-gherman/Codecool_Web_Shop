package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.implementation.OrderDaoMem;
import com.codecool.shop.model.ListItem;
import com.codecool.shop.model.Order;
import com.codecool.shop.utils.Utils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@WebServlet(urlPatterns = {"/payment-confirmation"})
public class ConfirmationController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        final int ORDERID = 1;
        final int USERID = 1;
        final int CARTID = 1;

        OrderDao orderDao = OrderDaoMem.getInstance();
        Order order = orderDao.getOrderById(ORDERID);

        List<ListItem> temp = new ArrayList<>();
        temp = orderDao.getItemsByOrderId(ORDERID);
        double total = 0;
        String orderCurrency;
        for (ListItem item : temp) {
            total += item.getProductPrice();
        }
        orderCurrency = temp.get(0).getProductCurrency();
        context.setVariable("total", total);
        context.setVariable("order", order);
        context.setVariable("currency", orderCurrency);

        engine.process("payment-confirmation.html", context, resp.getWriter());

    }



//        OrderDao orderDataStore = OrderDaoMem.getInstance();
//        if (Validation.validateCardNumberInput(req.getParameter("card-number"))==false) {
//            orderDataStore.setInvalidCardNumberMessage("A 16 digit card number is required.");
//            resp.sendRedirect("payment-details");
//        } else {
//            orderDataStore.setInvalidCardNumberMessage("");
//        }
//
//
//        orderDataStore.addLogEntry(orderDataStore, "Confirmation");
//        String fullName = orderDataStore.getFullName();
//        int orderId = orderDataStore.getId();
//        String total = orderDataStore.getTotal();

//        // transform Order into JSON and export to file
//        jsonify(orderDataStore);
//
//        // send confirmation email to customer
//        String custEmail = orderDataStore.getEmail();
//        sendEmailConfirmation(custEmail, fullName, orderId, total);
//
//        double totalAmount = Double.parseDouble(orderDataStore.getTotal());
//
//
//        context.setVariable("total", totalAmount);
//
//        context.setVariable("order", orderDataStore);
//
//        engine.process("payment-confirmation.html", context, resp.getWriter());
//        orderDataStore.addLogEntry(orderDataStore, "Confirmation Successful");
//
//        orderDataStore.clear();
//        CartDao cartDataStore = CartDaoMem.getInstance();
//        cartDataStore.eraseMe();
//    }

    private void sendEmailConfirmation(String custEmail, String fullName, int orderId, String total) {
        String to = custEmail;
        String host = "smtp.gmail.com";
        String subject = "EDUCATIONAL PROJECT - shop order confirmation";
        String body =  "EDUCATIONAL PROJECT - content with order details";
        final String user = "ionionescu2020demo@gmail.com";
        final String pass = "verystrongpassword";

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

//        OrderDao orderDataStore = OrderDaoMem.getInstance();
//        Currency orderCurrency = orderDataStore.getItems().get(0).getDefaultCurrency();
//        context.setVariable("order", orderDataStore);
//        context.setVariable("currency", orderCurrency);

        engine.process("payment-confirmation.html", context, resp.getWriter());

    }

}

