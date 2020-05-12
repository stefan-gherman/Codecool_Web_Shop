package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.UserDao;
import com.codecool.shop.dao.implementation.UserDaoJDBC;
import com.codecool.shop.model.User;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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


        User user = new User(fullName, email, password, phoneNumber, billingAddress, shippingAddress);
        int dbUserId = userDao.add(user);
        System.out.println("User id from db:" + dbUserId);
        resp.sendRedirect("/");

    }
}
