package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.UserDao;
import com.codecool.shop.dao.implementation.UserDaoJDBC;
import com.codecool.shop.model.User;
import com.codecool.shop.utils.Utils;
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
import java.io.IOException;
import java.util.Properties;

@WebServlet(urlPatterns = {"/register"})
public class RegisterController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        engine.process("register.html", context, resp.getWriter());

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        UserDao userDao = UserDaoJDBC.getInstance();

        String fullName = req.getParameter("full-name");
        String email = req.getParameter("input-email");
        String password = req.getParameter("password");
        String phoneNumber = req.getParameter("input-phone");
        String billingAddress = req.getParameter("billing-address");
        String shippingAddress = req.getParameter("shipping-address");

        User user = new User(fullName, email, Utils.hasher(password), phoneNumber, billingAddress, shippingAddress);
        int dbUserId = userDao.add(user);
        user.setId(dbUserId);
        HttpSession session = req.getSession(false);
        session.setAttribute("user", user);
        System.out.println("User id from db:" + dbUserId);
        sendEmailRegistrationConfirmation(user);
        resp.sendRedirect("/");

    }

    private void sendEmailRegistrationConfirmation(User shopUser) {
        String to = shopUser.getEmail();
        String host = "smtp.gmail.com";
        String subject = "EDUCATIONAL PROJECT - User registration successful";
        String body =  "EDUCATIONAL PROJECT - User registration successful";
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
            message.setSubject("EDUCATIONAL PROJECT - WEB SHOP USER REGISTRATION CONFIRMATION");
            message.setText("**** EDUCATIONAL PROJECT**** NOT AN ACTUAL SHOP\n" +
                    "Hello " + shopUser.getFullName() +",\n" +
                    "\n" +
                    "Thanks for creating an account with on our site.\n" +
                    "You can use your email and password to sign in." +
                    "We hope you'll find everything you need here and more!\n" +
                    "If you have any questions, you can always reach us at support@webshop.com\n" +
                    "\n" +
                    "Thanks again for the signup and have a wonderful day! \n" +
                    "The CodeCool Web Shop Team"

            );

            //send the message
            Transport.send(message);

            System.out.println("message sent successfully...");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
