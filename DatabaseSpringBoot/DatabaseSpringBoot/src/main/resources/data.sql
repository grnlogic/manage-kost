INSERT INTO users (username, password, role) VALUES
('admin', '$2y$10$Hw9LO4ac/dpOTpewoM/T5u.ET/UDOl1rMH21PQVk75sFF1Ha6/tM6', 'ADMIN'),
('user1', '$2y$10$H2sASyXTy06q2hBSBVK21.dujCrNmzNKh1Xg2XuD0Zjk5/7vcSaPO', 'USER')
ON CONFLICT (username) DO NOTHING;
