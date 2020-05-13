package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.UserDao;
import com.codecool.shop.dao.implementation.UserDaoJDBC;
import com.codecool.shop.model.User;
import com.codecool.shop.utils.Utils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"/login"})
public class LoginController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        engine.process("login.html", context, resp.getWriter());

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        UserDao userDao = UserDaoJDBC.getInstance();

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        System.out.println(email);
        String hashedPasswordFromDB = userDao.getUserPasswordByEmail(email);
        System.out.println(password + " " + hashedPasswordFromDB);
        if(Utils.checkPassword(password, hashedPasswordFromDB)) {
            User userFromDB = userDao.getUserByEmail(email);
            HttpSession session = req.getSession();
            session.setAttribute("user", userFromDB);
            resp.sendRedirect("/");
        } else {
            resp.sendRedirect("/login");
        }


    }
}
