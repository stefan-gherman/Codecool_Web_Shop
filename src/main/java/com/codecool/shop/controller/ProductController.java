package com.codecool.shop.controller;


import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.*;
import com.codecool.shop.dao.implementation.*;
import com.codecool.shop.model.*;
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
import java.util.List;

//import com.codecool.shop.dao.implementation.ProductCategoryDaoMem;
//import com.codecool.shop.dao.implementation.ProductDaoMem;
//import com.codecool.shop.dao.implementation.SupplierDaoMem;


@WebServlet(urlPatterns = {"/"})
public class ProductController extends HttpServlet {
    ProductCategoryDao productCategoryDataStore = null;
    ProductDao productDataStore = null;
    SupplierDao supplierDao = null;
    private final int MAX_SESSION_LIFE_IN_SECONDS = 3600;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        int cartSize = 0;
        if (session == null) {
            System.out.println("Session null");
            session = req.getSession();
            session.setMaxInactiveInterval(MAX_SESSION_LIFE_IN_SECONDS);
            if (session.getAttribute("user") == null) session.setAttribute("user", new User());
            if (session.getAttribute("cart") == null) session.setAttribute("cart", new Cart());
        }
        if (session.getAttribute("user") == null) session.setAttribute("user", new User());
        if (session.getAttribute("cart") == null) session.setAttribute("cart", new Cart());

        try {
            productDataStore = ProductDaoJDBC.getInstance();
            productCategoryDataStore = ProductCategoryJDBC.getInstance();
            supplierDao = SupplierDaoJDBC.getInstance();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            System.out.println(supplierDao.find(2));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        CartDao cartDataStore = CartDaoJDBC.getInstance();
        User currentUser = (User) session.getAttribute("user");
        Cart currentCart = (Cart) session.getAttribute("cart");

        if (currentUser.getFullName() != null) {
            try {
                if (cartDataStore.createCartFromQuery(currentUser.getId()).getCartContents().size() == 0) {
                    currentCart = (Cart) session.getAttribute("cart");
                } else {
                    session.setAttribute("cart", cartDataStore.createCartFromQuery(currentUser.getId()));
                    currentCart = (Cart) session.getAttribute("cart");
                }
//                if(currentCart.getCartNumberOfProducts()!= 0 && currentCart.getCartNumberOfProducts() !=
//                        cartDataStore.createCartFromQuery(currentUser.getId()).getCartNumberOfProducts()){
//                    session.setAttribute("cart", currentCart);
//                } else {
//                    session.setAttribute("cart", cartDataStore.createCartFromQuery(
//                            currentUser.getId()));
//                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        Cart tempCart = (Cart) session.getAttribute("cart");
        if (tempCart == null) {
            cartSize = 0;
        } else {
            cartSize = tempCart.getCartNumberOfProducts();
        }


        //System.out.println(req.getParameter("addToCart"));
        if (req.getParameter("addToCart") != null) {
            try {
                cartSize = addToCartReturningID(req);
            } catch (Exception e) {
                System.out.println(e.getStackTrace());
            }
        }

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());


        try {
            System.out.println("ID SESSION" + session.getId());
            int categoryId = getIdFromCategory(req, productCategoryDataStore);
            int supplierId = getIdFromSupplier(req, supplierDao);
            currentUser = (User) session.getAttribute("user");
            String username = currentUser.getFullName();
            if (username != null) {
                context.setVariable("username", username);
            } else {
                context.setVariable("username", "null");
            }

            displayProducts(context, engine, resp, productCategoryDataStore, productDataStore,
                    categoryId, supplierId, cartSize, supplierDao, session, currentUser);
        } catch (Exception notFound) {
            engine.process("product/notFound.html", context, resp.getWriter());

        }

    }


    /**
     * Display products from category or supplier
     *
     * @param context                  context
     * @param engine                   engine
     * @param resp                     resp
     * @param productCategoryDataStore productCategoryDataStore
     * @param productDataStore         productDataStore
     * @param categoryId               category id
     * @param supplierId               supplier id
     * @throws IOException exception
     */
    private void displayProducts(WebContext context, TemplateEngine engine, HttpServletResponse resp,
                                 ProductCategoryDao productCategoryDataStore,
                                 ProductDao productDataStore, int categoryId, int supplierId,
                                 int cartSize, SupplierDao supplierDao, HttpSession session, User currentUser)
            throws IOException, SQLException {

        context.setVariable("category", productCategoryDataStore.find(categoryId));
        //Suggestion
        if (supplierDao.find(supplierId) == null) {
            //System.out.println("Supplier null");
            context.setVariable("supplier", "all");
        } else {
            //System.out.println("Supplier not null");
            context.setVariable("supplier", supplierDao.find(supplierId));
        }
        context.setVariable("products", productDataStore.getBy(categoryId, supplierId));
        context.setVariable("cartSize", cartSize);


        context.setVariable("user", currentUser);
        System.out.println("tet sess " + session.getId());
        context.setVariable("session", session);
        engine.process("product/index.html", context, resp.getWriter());
    }


    /**
     * Get id from category
     *
     * @param req                      req from HttpServletRequest
     * @param productCategoryDataStore productCategory Interface using DAO pattern
     * @return category id
     */
    private int getIdFromCategory(HttpServletRequest req, ProductCategoryDao productCategoryDataStore) throws SQLException {
        int defaultCategory = 1;
        String[] categoryArray;

        String currentProductCategory = req.getParameter("productCategory");
        if (currentProductCategory == null) {
            return defaultCategory;
        } else {
            categoryArray = currentProductCategory.split("-");
        }
        List<ProductCategory> productCategoryList = productCategoryDataStore.getAll();
        for (ProductCategory productCategory : productCategoryList) {
            for (String word : categoryArray) {
                if (productCategory.getName().toLowerCase().contains(word)) {
                    return productCategory.getId();
                }
            }
        }
        return 0;

    }

    /**
     * Get id from supplier
     *
     * @param req         req from HttpServletRequest
     * @param supplierDao supplier interface using DAO pattern
     * @return supplier id
     */
    private int getIdFromSupplier(HttpServletRequest req, SupplierDao supplierDao) throws SQLException {

        if (req.getParameter("supplier") == null) {
            return 0;
        }

        String currentSupplier = req.getParameter("supplier");
        String[] supplierArray = currentSupplier.split("/s++");

        List<Supplier> supplierList = supplierDao.getAll();
        for (Supplier supplier : supplierList) {
            for (String word : supplierArray) {
                if (supplier.getName().toLowerCase().contains(word)) {
                    return supplier.getId();
                }
            }
        }

        return 0;


    }

    private int addToCartReturningID(HttpServletRequest req) throws IOException, SQLException {
        int prodIdParses = Integer.parseInt(req.getParameter("addToCart"));
//                System.out.println(prodIdParses);
//                cartDataStore.add(prodIdParses);
//                System.out.println("Current in cart" +cartDataStore.getCartContents());
//                cartSize = cartDataStore.getCartNumberOfProducts();
//                System.out.println("Current size " +cartSize);

        //updating the session cart as well
        OrderDao orderDao = OrderDaoJDBC.getInstance();
        int productId = prodIdParses;
        ListItem tempItem = orderDao.getListItemByProductId(productId);
        HttpSession session = req.getSession(false);
//        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAA - Got session.");
        Cart tempCart = (Cart) session.getAttribute("cart");
//        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAA - Got session cart " + tempCart.getId());
        tempCart.add(productId);
//        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAA - Got session cart contents: " + tempCart.getCartContents());
////                tempCartContents.put(tempItem, 1);
//        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAA - put: " + tempItem.getProductName() + " in contents");
////                tempCart.setCartContents(tempCartContents);
//        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAA - set contents of temp cart again to: " + tempCart.getCartContents());
//        System.out.println("TTTEEEMMMMPPP cart contents len: " + tempCart.getCartNumberOfProducts());
        session.removeAttribute("cart");
        session.setAttribute("cart", tempCart);
        tempCart = (Cart) session.getAttribute("cart");
        int cartSize = tempCart.getCartNumberOfProducts();
//                Cart tempCart2 = (Cart) session.getAttribute("cart");
//                System.out.println("RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRemove and re-create cart: " + tempCart2.getCartContents());
        return cartSize;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}




