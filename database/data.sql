-- ============================================================
-- OGOS — Seed Data
-- Run AFTER schema is created.
-- Admin password is: Admin@1234  (BCrypt encoded)
-- ============================================================

USE ogos;

-- Insert Admin user (manually — cannot self-register as admin)
INSERT INTO users (first_name, last_name, email, password, phone, role, status, created_at, updated_at)
VALUES (
    'Admin',
    'OGOS',
    'admin@ogos.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    '9999999999',
    'ROLE_ADMIN',
    'ACTIVE',
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE id = id;

-- Insert sample categories
INSERT INTO categories (name, description, image_url, created_at, updated_at) VALUES
('Fruits & Vegetables', 'Fresh fruits and vegetables', '', NOW(), NOW()),
('Dairy & Eggs',        'Milk, cheese, butter, eggs', '', NOW(), NOW()),
('Bakery',              'Bread, cakes, pastries',     '', NOW(), NOW()),
('Beverages',           'Juices, water, soft drinks', '', NOW(), NOW()),
('Snacks',              'Chips, biscuits, namkeen',   '', NOW(), NOW()),
('Grains & Pulses',     'Rice, wheat, lentils',       '', NOW(), NOW())
ON DUPLICATE KEY UPDATE id = id;

-- ============================================================
-- NOTE:
--   Admin credentials:  admin@ogos.com  /  Admin@1234
--   To create a vendor, register via POST /api/auth/register
--   with role = "ROLE_VENDOR"
-- ============================================================
