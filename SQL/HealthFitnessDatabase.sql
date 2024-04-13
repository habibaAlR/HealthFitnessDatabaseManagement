
CREATE TABLE IF NOT EXISTS members (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    weight FLOAT ,
    height FLOAT,
    fitness_goal VARCHAR(100),
    balance DECIMAL(10, 2) DEFAULT 0
);

CREATE TABLE IF NOT EXISTS trainers (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS admins (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL
 );

CREATE TABLE IF NOT EXISTS training_sessions (
    id SERIAL PRIMARY KEY,
    session_name VARCHAR(50) NOT NULL,
    trainer_username VARCHAR(50),
    session_time TIMESTAMP,
    price DECIMAL(10, 2) DEFAULT 0,
    FOREIGN KEY (trainer_username) REFERENCES trainers(username)
);

CREATE TABLE room_availability (
    room_number INT UNIQUE NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    booked BOOLEAN DEFAULT FALSE,
    capacity INT
);


CREATE TABLE IF NOT EXISTS fitness_classes (
    id SERIAL PRIMARY KEY,
    class_name VARCHAR(50) NOT NULL,
    instructor_username VARCHAR(50),
    class_time TIMESTAMP,
    room_number INT,
    price DECIMAL(10, 2) DEFAULT 0,
    FOREIGN KEY (room_number) REFERENCES room_availability(room_number),
    FOREIGN KEY (instructor_username) REFERENCES trainers(username)
);

CREATE TABLE IF NOT EXISTS exercise_routines (
    id SERIAL PRIMARY KEY,
    push_ups BOOLEAN DEFAULT FALSE,
    push_ups_amount INT DEFAULT 0,
    jumping_jacks BOOLEAN DEFAULT FALSE,
    jumping_jacks_amount INT DEFAULT 0,
    sit_ups BOOLEAN DEFAULT FALSE,
    sit_ups_amount INT DEFAULT 0,
    squats BOOLEAN DEFAULT FALSE,
    squats_amount INT DEFAULT 0,
    lunges BOOLEAN DEFAULT FALSE,
    lunges_amount INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS equipment (
    equip_name VARCHAR(255) PRIMARY KEY,
    amount INT NOT NULL
);

CREATE TABLE registered_classes (
   registration_id SERIAL PRIMARY KEY,
   member_id INT NOT NULL,
   session_or_class_id INT NOT NULL,
   type VARCHAR(15) NOT NULL,
   charge DECIMAL(10, 2) NOT NULL,
   FOREIGN KEY (member_id) REFERENCES members(id)
);