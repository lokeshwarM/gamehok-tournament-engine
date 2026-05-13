-- ============================================================
-- V2__seed_data.sql
-- Initial seed data for development and testing
-- ============================================================

-- Default admin user (password: Admin@1234 — BCrypt encoded)
INSERT INTO users (username, email, password_hash, display_name, role, elo_rating, is_verified, is_active)
VALUES (
    'superadmin',
    'admin@gamehok.com',
    '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN9B9Yxz3v3bVr5EQ7GKu',
    'Super Admin',
    'SUPER_ADMIN',
    2000,
    TRUE,
    TRUE
);
