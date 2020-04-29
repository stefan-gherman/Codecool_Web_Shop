package com.codecool.shop.config;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.dao.implementation.SupplierDaoMem;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.swing.text.html.ListView;

@WebListener
public class Initializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ProductDao productDataStore = ProductDaoMem.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        SupplierDao supplierDataStore = SupplierDaoMem.getInstance();

        //setting up a new supplier
        Supplier amazon = new Supplier("Amazon", "Digital content and services");
        supplierDataStore.add(amazon);

        Supplier lenovo = new Supplier("Lenovo", "Computers");
        supplierDataStore.add(lenovo);

        Supplier hp = new Supplier("HP", "Monitors");
        supplierDataStore.add(lenovo);

        // tablets category
        ProductCategory tablets = new ProductCategory("Tablets", "Hardware",
                "A tablet computer, commonly shortened to tablet, is a thin, flat mobile computer with a touchscreen display.");
        productCategoryDataStore.add(tablets);

        // products from tablets category
        productDataStore.add(new Product("Amazon Fire","product_1.jpg", 49.9f, "USD",
                "Fantastic price. Large content ecosystem. Good parental controls. Helpful technical support.",
                tablets, amazon));
        productDataStore.add(new Product("Lenovo IdeaPad Miix 700","product_2.jpg", 479, "USD",
                "Keyboard cover is included. Fanless Core m5 processor. Full-size USB ports. Adjustable kickstand.", tablets, lenovo));
        productDataStore.add(new Product("Amazon Fire HD 8","product_3.jpg", 89, "USD",
                "Amazon's latest Fire HD 8 tablet is a great value for media consumption.", tablets, amazon));
        productDataStore.add(new Product("Samsung Galaxy Tab S5e", "Samsung Galaxy Tab S5e.jpg", 549.99f, "USD",
                "Weighing in at less than a pound, the incredibly slim Galaxy Tab S5e is perfect for your on-the-go life.", tablets, amazon));
        productDataStore.add(new Product("VANKYO MatrixPad S8 Tablet 8 inch", "VANKYO MatrixPad S8 Tablet 8 inch.jog", 85.99f, "USD",
                "Featuring a vibrant 8-inch display, the tablet brings your digital media to life in rich colors and vivid details. " +
                        "The VANKYO MatrixPad S8 lets you browse, read books, watch your favorite shows and movies, play light games on a tablet that’s light and comfortable in your hands. ",
                tablets, amazon));
        productDataStore.add(new Product("Fire 7 Tablet", "Fire 7 Tablet.jpf", 49.99f, "USD",
                "With award-winning Amazon FreeTime, parents can create child profiles to limit screen time, set educational goals, and manage content with easy-to-use parental controls. ",
                tablets, amazon));

        // cameras category
        ProductCategory cameras = new ProductCategory("Cameras",
                "Smart Home", "Protect your home with our cameras");
        productCategoryDataStore.add(cameras);


        // cameras products
        productDataStore.add(new Product("Ring Video Doorbell with HD Video", "Ring Video Doorbell with HD Video.jpg", 99.99f,
                "USD", "Stay connected to home with motion-activated alerts, HD video and two-way talk from Ring Video Doorbell.",
                cameras, amazon));
        productDataStore.add(new Product("Blink XT2 Smart Security Camera", "Blink XT2 Smart Security Camera.jpg",
                249.99f, "USD", " Camera with cloud storage included, 2-way audio, 2-year battery life – 3 camera kit.",
                cameras, amazon));
        productDataStore.add(new Product("Arlo - Wireless Home Security Camera System", "Arlo - Wireless Home Security Camera System.jpg",
                120, "USD", "The Arlo camera is a 100 Percent Wire-Free, completely wireless, HD smart home security camera – so you can get exactly the shot you need – inside or out. ",
                cameras, amazon));
        productDataStore.add(new Product("Zmodo Wireless Security Camera System", "Zmodo Wireless Security Camera System.jpg", 75f,
                "USD", "Get a crystal clear full-HD picture of your home, from anywhere at any time, and see up to 65ft away in the dark." +
                " Smarter and adjustable night vision allows you to see color image even in a dim environment by lowering the IR sensitivity ",
                cameras, amazon));
        productDataStore.add(new Product("Panasonic HomeHawk Wireless", "Panasonic HomeHawk Wireless.jpg", 249.99f, "USD",
                "HomeHawk wire-free outdoor cameras include built-in rechargeable batteries to make it easier than ever to place and install your cameras around the home's exterior, exactly where you want them.",
                cameras, amazon));

        // living room category
        ProductCategory livingRoomFurniture = new ProductCategory("Living Room Furniture", "Furniture",
                "Make entertainment the focus of your living room with one of our living room pieces.");
        productCategoryDataStore.add(livingRoomFurniture);

        // living room furniture products
        productDataStore.add(new Product("Canal Heights Side Table","livingRoomFurniture.jpg", 99.99f, "USD",
                "Perfect for accentuating any living area or home environment", livingRoomFurniture, amazon));
        productDataStore.add(new Product("Zigzag Coffee table", "Zigzag Coffee table.jpg", 139.99f, "USD",
                "With the wood and dark panel finish, this is a classy addition to the Zigzag range with the added benefit of a shelf underneath for storage."
        , livingRoomFurniture, amazon));
        productDataStore.add(new Product("Jonas & James Ralla TV Unit", "Jonas & James Ralla TV Unit.jpg", 219.99f, "USD",
                "he combination of Artisan Oak and dark accents will feel right at home in practically any interior, and Ralla' " +
                        "high-quality fixings such as soft close hinges and drawers will the furniture a joy to use every day.",
                livingRoomFurniture, amazon));
        productDataStore.add(new Product("Kinsale Two Door Two Drawer Sideboard", "Kinsale Two Door Two Drawer Sideboard.jpg", 249.99f, "USD",
                "A sturdy solid oak construction and natural wax finish makes our Kinsale Two Door Two Drawer Sideboard a desirably rustic and traditional storage solution",livingRoomFurniture, amazon));
        productDataStore.add(new Product("Zigzag Wide One Door Bookcase", "Zigzag Wide One Door Bookcase.jpg", 289.99f, "USD",
                "A bookcase with a single cupboard door in the signature Zigzag design, as well as open shelving to display your most read, or maybe some of the more high-brow titles to impress guests!", livingRoomFurniture, amazon));
        productDataStore.add(new Product("Kinsale Large TV Unit", "Kinsale Large TV Unit.jpg", 189.99f, "USD",
                "Our luxury Kinsale Large TV Unit is fashioned from beautiful and sturdy solid oak wood with a natural wax finish.", livingRoomFurniture, amazon));
        productDataStore.add(new Product("Camford Coffee Table", "Camford Coffee Table.jpg", 99, "USD",
                "A great way to tie your living room together, this is an excellent location to keep magazines, decorations or place a drink while you watch TV", livingRoomFurniture, amazon));
        productDataStore.add(new Product("Puro Coffee Table - Charcoal", "Puro Coffee Table - Charcoal.jpg", 129.99f, "USD",
                "Made with style and practicality in mind, this Puro Coffee Table is beautifully crafted with a high gloss lacquer finish.", livingRoomFurniture, amazon));
        productDataStore.add(new Product("Sevenoaks Coffee Table", "Sevenoaks Coffee Table.jpg", 129.99f, "USD",
                "Simple and elegant collection with laminated board (resistant to damage and scratching, moisture and high temperatures) and modern handle-less system.",
                livingRoomFurniture, amazon));
        productDataStore.add(new Product("Curve Bookcase - Oak", "Curve Bookcase - Oak.jpg", 359.99f, "USD",
                "Crafted from solid oak and oak veneers, the Curve combines clean lines with a gentle curved edging, adding a modern, retro feel to any dining area.", livingRoomFurniture, amazon));


        // monitors category
        ProductCategory monitors = new ProductCategory("Monitors", "Computers", "Buy the best monitor for your needs.");
        productCategoryDataStore.add(monitors);

        // products in monitors category
        productDataStore.add(new Product("HP VH240a 23.8-inch Full HD 1080p", "HP VH240a 23.8-inch Full HD 1080p.jpg", 109.99f, "USD",
                "Get the best productivity from home or at the office with the virtually borderless HP VH240a 23.8-Inch display featuring an ergonomic stand, built-in speakers and an ultra-slim design at a competitively low price point",
                monitors, amazon));
    }

}
