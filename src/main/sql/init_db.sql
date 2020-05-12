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
VALUES (1, 1, 'Amazon Fire',
        'Fantastic price. Large content ecosystem. Good parental controls. Helpful technical support.', 'product_1.jpg',
        49.9, 'USD');
INSERT INTO products (supplier_id, category_id, name, description, image, price, currency)
VALUES (2, 1, 'Lenovo IdeaPad Miix 700',
        'Keyboard cover is included. Fanless Core m5 processor. Full-size USB ports. Adjustable kickstand.',
        'product_2.jpg', 479, 'USD');

INSERT INTO products (supplier_id, category_id, name, description, image, price, currency)
VALUES (1, 2, 'Ring Video Doorbell with HD Video',
        'Stay connected to home with motion-activated alerts, HD video and two-way talk from Ring Video Doorbell.',
        'Ring Video Doorbell with HD Video.jpg', 99.99, 'USD');

INSERT INTO products (supplier_id, category_id, name, description, image, price, currency)
VALUES (1, 3, 'Canal Heights Side Table', 'Perfect for accentuating any living area or home environment',
        'livingRoomFurniture.jpg', 99.99, 'USD');

INSERT INTO products (supplier_id, category_id, name, description, image, price, currency)
VALUES (1, 4, 'HP VH240a 23.8-inch Full HD 1080p',
        'Get the best productivity from home or at the office with the virtually borderless HP VH240a 23.8-Inch display featuring an ergonomic stand, built-in speakers and an ultra-slim design at a competitively low price point',
        'HP VH240a 23.8-inch Full HD 1080p.jpg', 109.99, 'USD');

INSERT INTO users (name, email, password, billing_address, shipping_address) VALUES ('John', 'a@a.com', 'asdasdasdasd', '45 Left', '6753 Up');

INSERT INTO carts (user_id)
VALUES (1);
