INSERT INTO members (username, password, first_name, last_name, email, weight, height, fitness_goal, balance) VALUES
('jameshen', 'james123', 'James', 'Henry', 'james_henry@email.com', 70, 180, 'Build muscle', 22.00),
('aubreyb', 'aubrey123', 'Aubrey', 'Bishop', 'aubrey_bishop@email.com', 65, 165, 'weight loss', 0.00),
('alexanderg', 'alexander123', 'Alexander', 'George', 'alexander_george@email.com', 78, 175, 'build muscle', 75.00),
('samanthaw', 'samantha123', 'Samantha', 'West', 'samantha_west@email.com', 54, 160, 'develop a healthy lifestyle', 60.00),
('oliverb', 'oliver123', 'Oliver', 'Barker', 'oliver_barker@email.com', 80, 182, 'be more active', 0.00),
('carrieb', 'carrie123', 'Carrie', 'Brooks', 'carrie_brooks@email.com', 59, 158, 'weight loss', 45.00),
('edwardh', 'edward123', 'Edward', 'Hart', 'edward_hart@email.com', 85, 190, 'build muscle', 0.00),
('ericaw', 'erica123', 'Erica', 'Wallis', 'erica_wallis@email.com', 62, 164, 'develop a healthy lifestyle', 65.00),
('mariap', 'maria123', 'Maria', 'Page', 'maria_page@email.com', 68, 170, 'be more active', 70.00),
('meganf', 'megan123', 'Megan', 'Finch', 'megan_finch@email.com', 60, 162, 'weight loss', 0.00);

INSERT INTO trainers (first_name, last_name, username, password) VALUES
('Fredrick', 'Paul', 'fredrickp', 'fredrick123'),
('Camilla', 'Hope', 'camillah', 'camilla123'),
('Patrick', 'Young', 'patricky', 'patrick123'),
('William', 'Parker', 'williamp', 'william123'),
('Jane', 'Rose', 'janer', 'jane123');

INSERT INTO admins (first_name, last_name, username, password) VALUES
('Christine', 'Adams', 'christinea', 'christine123'),
('Diane', 'Hardy', 'dianeh', 'diane123'),
('Owen', 'Shaw', 'owens', 'owen123');

INSERT INTO training_sessions (session_name, trainer_username, session_time, price) VALUES
('Upper-body strength training', 'fredrickp', '2024-04-20 12:00:00', 25.00),
('Lower-body strength training', NULL, '2024-04-21 13:00:00', 25.00),
('HIIT', 'williamp', '2024-04-22 14:00:00', 30.00),
('Total-body strength training', NULL, '2024-04-23 15:00:00', 35.00),
('Steady-state cardio', 'janer', '2024-04-24 16:00:00', 30.00);

INSERT INTO room_availability (room_number, start_time, end_time, booked, capacity) VALUES
(20, '2024-04-20 09:00:00', '2024-04-20 11:00:00', FALSE, 20),
(21, '2024-04-20 11:00:00', '2024-04-20 13:00:00', FALSE, 20),
(22, '2024-04-21 12:00:00', '2024-04-21 15:00:00', FALSE, 20),
(23, '2024-04-21 15:00:00', '2024-04-21 17:00:00', FALSE, 20),
(24, '2024-04-22 13:00:00', '2024-04-22 16:00:00', FALSE, 20),
(25, '2024-04-22 14:00:00', '2024-04-22 17:00:00', FALSE, 20),
(26, '2024-04-23 14:00:00', '2024-04-23 16:00:00', FALSE, 20),
(27, '2024-04-23 15:00:00', '2024-04-23 17:00:00', FALSE, 20),
(28, '2024-04-24 15:00:00', '2024-04-24 17:00:00', FALSE, 20),
(29, '2024-04-24 16:00:00', '2024-04-24 18:00:00', FALSE, 20),
(30, '2024-05-12 16:00:00', '2024-05-12 18:00:00', FALSE, 20),
(31, '2024-05-12 11:00:00', '2024-05-12 13:00:00', FALSE, 20),
(32, '2024-05-13 12:00:00', '2024-05-13 14:00:00', FALSE, 20),
(33, '2024-05-13 13:00:00', '2024-05-13 15:00:00', FALSE, 20),
(34, '2024-05-14 13:00:00', '2024-05-14 15:00:00', FALSE, 20),
(35, '2024-05-14 14:00:00', '2024-05-14 16:00:00', FALSE, 20),
(36, '2024-05-15 14:00:00', '2024-05-15 16:00:00', FALSE, 20),
(37, '2024-05-15 15:00:00', '2024-05-15 17:00:00', FALSE, 20),
(38, '2024-05-16 15:00:00', '2024-05-12 17:00:00', FALSE, 20),
(39, '2024-05-16 16:00:00', '2024-05-12 17:00:00', FALSE, 20);



INSERT INTO fitness_classes (class_name, instructor_username, class_time, price) VALUES
('Cardio Class', 'janer', '2024-05-12 12:00:00', 20.00),
('Spin Class', NULL, '2024-05-13 13:00:00', 20.00),
('Dance Class','patricky' , '2024-05-14 14:00:00', 25.00),
('Drums Class', NULL, '2024-05-15 15:00:00', 25.00),
('Zumba Class', 'camillah', '2024-05-16 16:00:00', 30.00);

INSERT INTO exercise_routines (push_ups, push_ups_amount, jumping_jacks, jumping_jacks_amount, sit_ups, sit_ups_amount, squats, squats_amount, lunges, lunges_amount) VALUES
(TRUE, 20, TRUE, 10, TRUE, 10, TRUE, 20, TRUE, 10),
(TRUE, 10, FALSE, 0, TRUE, 20, TRUE, 20, TRUE, 10),
(TRUE, 15, TRUE, 10, TRUE, 20, FALSE, 25, TRUE, 10),
(TRUE, 15, FALSE, 0, FALSE, 0, FALSE, 25, TRUE, 10),
(FALSE, 0, TRUE, 10, TRUE, 20, FALSE, 25, TRUE, 10);

INSERT INTO equipment (equip_name, amount) VALUES
('5kg dumbbells', 10),
('10kg dumbbells', 5),
('20kg dumbbells', 10),
('treadmills', 10),
('weight benches', 3),
('chest press machines', 3),
('Elliptical', 2);


