-- ============================================================
-- OGOS — Complete Database Schema
-- Spring Boot with ddl-auto=update will auto-create tables,
-- but this file is provided for reference / manual setup.
-- ============================================================

CREATE DATABASE IF NOT EXISTS ogos;
USE ogos;

-- Users
CREATE TABLE IF NOT EXISTS users (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name   VARCHAR(100) NOT NULL,
    last_name    VARCHAR(100),
    email        VARCHAR(255) NOT NULL UNIQUE,
    password     VARCHAR(255) NOT NULL,
    phone        VARCHAR(20)  NOT NULL,
    role         ENUM('ROLE_CUSTOMER','ROLE_VENDOR','ROLE_ADMIN') NOT NULL DEFAULT 'ROLE_CUSTOMER',
    status       ENUM('ACTIVE','BLOCKED') NOT NULL DEFAULT 'ACTIVE',
    created_at   DATETIME,
    updated_at   DATETIME
);

-- Categories
CREATE TABLE IF NOT EXISTS categories (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    image_url   VARCHAR(500),
    created_at  DATETIME,
    updated_at  DATETIME
);

-- Products
CREATE TABLE IF NOT EXISTS products (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    description  TEXT         NOT NULL,
    price        DOUBLE       NOT NULL,
    quantity     INT          NOT NULL,
    category_id  BIGINT,
    image_url    VARCHAR(500) NOT NULL,
    vendor_id    BIGINT,
    created_at   DATETIME,
    updated_at   DATETIME,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL,
    FOREIGN KEY (vendor_id)   REFERENCES users(id)      ON DELETE SET NULL
);

-- Addresses
CREATE TABLE IF NOT EXISTS addresses (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    street     VARCHAR(255) NOT NULL,
    city       VARCHAR(100) NOT NULL,
    state      VARCHAR(100) NOT NULL,
    zip_code   VARCHAR(20)  NOT NULL,
    country    VARCHAR(100) NOT NULL,
    is_default TINYINT(1)   DEFAULT 0,
    user_id    BIGINT       NOT NULL,
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Carts
CREATE TABLE IF NOT EXISTS carts (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT NOT NULL UNIQUE,
    total_price DOUBLE DEFAULT 0,
    created_at  DATETIME,
    updated_at  DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Cart Items
CREATE TABLE IF NOT EXISTS cart_items (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    cart_id    BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity   INT    NOT NULL,
    price      DOUBLE NOT NULL,
    FOREIGN KEY (cart_id)    REFERENCES carts(id)    ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

-- Orders
CREATE TABLE IF NOT EXISTS orders (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT NOT NULL,
    address_id  BIGINT,
    status      ENUM('PENDING','CONFIRMED','SHIPPED','DELIVERED','CANCELLED') NOT NULL DEFAULT 'PENDING',
    total_amount DOUBLE NOT NULL,
    created_at  DATETIME,
    updated_at  DATETIME,
    FOREIGN KEY (user_id)    REFERENCES users(id)     ON DELETE CASCADE,
    FOREIGN KEY (address_id) REFERENCES addresses(id) ON DELETE SET NULL
);

-- Order Items
CREATE TABLE IF NOT EXISTS order_items (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id    BIGINT NOT NULL,
    product_id  BIGINT NOT NULL,
    quantity    INT    NOT NULL,
    price       DOUBLE NOT NULL,
    total_price DOUBLE NOT NULL,
    FOREIGN KEY (order_id)   REFERENCES orders(id)   ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE RESTRICT
);

-- Notifications
CREATE TABLE IF NOT EXISTS notifications (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT,
    message    TEXT,
    is_read    TINYINT(1) DEFAULT 0,
    created_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
