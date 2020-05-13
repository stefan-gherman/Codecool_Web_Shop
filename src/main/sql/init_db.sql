DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS carts CASCADE;
DROP TABLE IF EXISTS products CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS suppliers CASCADE;
DROP TABLE IF EXISTS cart_items CASCADE;
DROP TABLE IF EXISTS order_items CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users
(
    id               SERIAL PRIMARY KEY NOT NULL,
    name             VARCHAR(50),
    email            VARCHAR(70),
    password         VARCHAR(255),
    phone_number     VARCHAR(48),
    billing_address  VARCHAR(100),
    shipping_address VARCHAR(100)

);

CREATE TABLE carts
(
    id           SERIAL PRIMARY KEY NOT NULL,
    user_id      INTEGER REFERENCES users (id) ON DELETE CASCADE,
    date_created TIMESTAMP          NOT NULL DEFAULT NOW()
);

CREATE TABLE orders
(
    id               SERIAL PRIMARY KEY NOT NULL,
    date_created     TIMESTAMP          NOT NULL DEFAULT NOW(),
    cart_id          INTEGER REFERENCES carts (id) ON DELETE CASCADE,
    user_id          INTEGER REFERENCES users (id) ON DELETE CASCADE,
    owner_name       VARCHAR(50),
    owner_phone      VARCHAR(20),
    owner_email      VARCHAR(50),
    billing_address  VARCHAR(100),
    shipping_address VARCHAR(100)
);

CREATE TABLE categories
(
    id          SERIAL PRIMARY KEY NOT NULL,
    name        VARCHAR(255),
    department  VARCHAR(255),
    description TEXT
);

CREATE TABLE suppliers
(
    id          SERIAL PRIMARY KEY NOT NULL,
    name        VARCHAR(255),
    description TEXT
);

CREATE TABLE products
(
    id          SERIAL PRIMARY KEY NOT NULL,
    supplier_id INTEGER REFERENCES suppliers (id) ON DELETE CASCADE,
    category_id INTEGER REFERENCES categories (id) ON DELETE CASCADE,
    name        VARCHAR(255),
    description TEXT,
    image       VARCHAR(255),
    price       FLOAT,
    currency    VARCHAR(10)
);

CREATE TABLE cart_items
(
    cart_id    INTEGER REFERENCES carts (id) ON DELETE CASCADE,
    product_id INTEGER REFERENCES products (id) ON DELETE CASCADE
);

CREATE TABLE order_items
(
    order_id   INTEGER REFERENCES orders (id) ON DELETE CASCADE,
    product_id INTEGER REFERENCES products (id) ON DELETE CASCADE
);

-- separate table many to many item_list : product_id, order_id


INSERT INTO suppliers (name, description)
VALUES ('Amazon', 'Digital content and services.');
INSERT INTO suppliers (name, description)
VALUES ('Lenovo', 'Computers and electronics.');
INSERT INTO suppliers (name, description)
VALUES ('Walmart', 'Various products.');

INSERT INTO categories (name, department, description)
VALUES ('Tablets', 'Hardware',
        'A tablet computer, commonly shortened to tablet, is a thin, flat mobile computer with a touchscreen display.');
INSERT INTO categories (name, department, description)
VALUES ('Cameras', 'Smart Home', 'Protect your home with our cameras');
INSERT INTO categories (name, department, description)
VALUES ('Living Room Furniture', 'Furniture',
        'Make entertainment the focus of your living room with one of our living room pieces.');
INSERT INTO categories (name, department, description)
VALUES ('Monitors', 'Computers', 'Buy the best monitor for your needs.');
INSERT INTO products (supplier_id, category_id, name, description, image, price, currency)
VALUES (1, 1, 'Amazon Fire','Fantastic price. Large content ecosystem. Good parental controls. Helpful technical support.', 'product_2.jpg', 49.9, 'USD');

INSERT INTO products (supplier_id, category_id, name, description, image, price, currency)
VALUES (1, 1, 'Amazon Fire HD 8','Latest Fire HD 8 tablet is a great value for media consumption.', 'product_3.jpg', 89, 'USD');

INSERT INTO products (supplier_id, category_id, name, description, image, price, currency)
VALUES (3, 1, 'Samsung Galaxy Tab S5e','Weighing in at less than a pound, the incredibly slim Galaxy Tab S5e is perfect for your on-the-go life.', 'Samsung Galaxy Tab S5e.jpg', 548.99, 'USD');

INSERT INTO products (supplier_id, category_id, name, description, image, price, currency)
VALUES (3, 1,'VANKYO MatrixPad S8 Tablet 8 inch','The VANKYO MatrixPad S8 lets you browse, read books, watch your favorite shows and movies, play light games on a tablet that’s light and comfortable in your hands.', 'VANKYO MatrixPad S8 Tablet 8 inch.jpg', 85.99, 'USD');

INSERT INTO products (supplier_id, category_id, name, description, image, price, currency)
VALUES (1, 1,'Fire 7 Tablet','With award-winning Amazon FreeTime, parents can create child profiles to limit screen time, set educational goals, and manage content with easy-to-use parental controls.', 'Fire 7 Tablet.jpg', 49.99, 'USD');

INSERT INTO products (supplier_id, category_id, name, description, image, price, currency)
VALUES (1, 2, 'Ring Video Doorbell with HD Video','Stay connected to home with motion-activated alerts, HD video and two-way talk from Ring Video Doorbell.', 'Ring Video Doorbell with HD Video.jpg', 99.99, 'USD');

INSERT INTO products (supplier_id, category_id, name, description, image, price, currency)
VALUES (1, 2, 'Blink XT2 Smart Security Camera','Camera with cloud storage included, 2-way audio, 2-year battery life – 3 camera kit.', 'Blink XT2 Smart Security Camera.jpg', 249.99, 'USD');

INSERT INTO products (supplier_id, category_id, name, description, image, price, currency)
VALUES (1, 2, 'Zmodo Wireless Security Camera System','Smarter and adjustable night vision allows you to see color image even in a dim environment by lowering the IR sensitivity', 'Zmodo Wireless Security Camera System.jpg', 75.99, 'USD');

INSERT INTO products (supplier_id, category_id, name, description, image, price, currency)
VALUES (3, 2, 'Arlo - Wireless Home Security Camera System','The Arlo camera is a 100 Percent Wire-Free, completely wireless, HD smart home security camera – so you can get exactly the shot you need – inside or out.', 'Arlo - Wireless Home Security Camera System.jpg', 120.99, 'USD');

INSERT INTO products (supplier_id, category_id, name, description, image, price, currency)
VALUES (3, 2, 'Panasonic HomeHawk Wireless','HomeHawk wire-free outdoor cameras include built-in rechargeable batteries to make it easier than ever to place and install your cameras around the home', 'Panasonic HomeHawk Wireless.jpg', 249.99, 'USD');

INSERT INTO products (supplier_id, category_id, name, description, image, price, currency)
VALUES (1, 3, 'Canal Heights Side Table','Perfect for accentuating any living area or home environment', 'livingRoomFurniture.jpg', 99.99, 'USD');

INSERT INTO products (supplier_id, category_id, name, description, image, price, currency)
VALUES (3, 3, 'Zigzag Coffee table','With the wood and dark panel finish, this is a classy addition to the Zigzag range with the added benefit of a shelf underneath for storage.', 'Zigzag Coffee table.jpg', 99.99, 'USD');

INSERT INTO products (supplier_id, category_id, name, description, image, price, currency)
VALUES (3, 3, 'Jonas & James Ralla TV Unit','The combination of Artisan Oak and dark accents will feel right at home in practically any interior', 'Jonas & James Ralla TV Unit.jpg', 99.99, 'USD');

INSERT INTO products (supplier_id, category_id, name, description, image, price, currency)
VALUES (1, 3, 'Kinsale Two Door Two Drawer Sideboard','A sturdy solid oak construction and natural wax finish makes our Kinsale Two Door Two Drawer Sideboard a desirably rustic and traditional storage solution', 'Kinsale Two Door Two Drawer Sideboard.jpg', 249.99, 'USD');

INSERT INTO products (supplier_id, category_id, name, description, image, price, currency)
VALUES (1, 3, 'Zigzag Wide One Door Bookcase','A bookcase with a single cupboard door in the signature Zigzag design, as well as open shelving to display your most read, or maybe some of the more high-brow titles to impress guests.', 'Zigzag Wide One Door Bookcase.jpg', 289.99, 'USD');

INSERT INTO products (supplier_id, category_id, name, description, image, price, currency)
VALUES (3, 3, 'Kinsale Large TV Unit','Our luxury Kinsale Large TV Unit is fashioned from beautiful and sturdy solid oak wood with a natural wax finish.', 'Kinsale Large TV Unit.jpg', 189.99, 'USD');

INSERT INTO products (supplier_id, category_id, name, description, image, price, currency)
VALUES (3, 3, 'Camford Coffee Table','A great way to tie your living room together, this is an excellent location to keep magazines, decorations or place a drink while you watch TV.', 'Camford Coffee Table.jpg', 99.99, 'USD');

INSERT INTO products (supplier_id, category_id, name, description, image, price, currency)
VALUES (1, 3, 'Puro Coffee Table - Charcoal','Made with style and practicality in mind, this Puro Coffee Table is beautifully crafted with a high gloss lacquer finish.', 'Puro Coffee Table - Charcoal.jpg', 129.99, 'USD');

INSERT INTO products (supplier_id, category_id, name, description, image, price, currency)
VALUES (1, 3, 'Sevenoaks Coffee Table','Simple and elegant collection with laminated board (resistant to damage and scratching, moisture and high temperatures) and modern handle-less system.', 'Sevenoaks Coffee Table.jpg', 129.99, 'USD');

INSERT INTO products (supplier_id, category_id, name, description, image, price, currency)
VALUES (3, 3, 'Curve Bookcase - Oak','Crafted from solid oak and oak veneers, the Curve combines clean lines with a gentle curved edging, adding a modern, retro feel to any dining area.', 'Curve Bookcase - Oak.jpg', 359.99, 'USD');

INSERT INTO products (supplier_id, category_id, name, description, image, price, currency)
VALUES (1, 4, 'HP VH240a 23.8-inch Full HD 1080p','Get the best productivity from home or at the office with the virtually borderless HP VH240a 23.8-Inch display featuring an ergonomic stand, built-in speakers and an ultra-slim design at a competitively low price point.', 'HP VH240a 23.8-inch Full HD 1080p.jpg', 109.99, 'USD');

INSERT INTO users (name, email, password, phone_number, billing_address, shipping_address) VALUES ('John', 'a@a.com', 'asdasdasdasd', '0735678921','45 Left', '6753 Up');

INSERT INTO carts (user_id)
VALUES (1);
