CREATE TABLE IF NOT EXISTS students (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    course VARCHAR(255),
    academic_year VARCHAR(100),
    level VARCHAR(100),
    bio VARCHAR(1000),
    teach_skills VARCHAR(1000),
    learn_skills VARCHAR(1000),
    coins INT DEFAULT 760,
    hours DOUBLE DEFAULT 18.4,
    sessions INT DEFAULT 18,
    streak INT DEFAULT 12,
    online BOOLEAN DEFAULT TRUE,
    category VARCHAR(100) DEFAULT 'coding',
    experience INT DEFAULT 85
);

CREATE TABLE IF NOT EXISTS skill_sessions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    partner_id BIGINT NOT NULL,
    partner_name VARCHAR(255),
    partner_initials VARCHAR(50),
    topic VARCHAR(255) NOT NULL,
    session_type VARCHAR(100),
    session_date VARCHAR(50),
    session_day VARCHAR(50),
    start_time VARCHAR(50),
    end_time VARCHAR(50),
    duration_minutes INT,
    session_mode VARCHAR(50),
    coins INT,
    status VARCHAR(50) DEFAULT 'SCHEDULED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS coin_transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    amount INT NOT NULL,
    transaction_type VARCHAR(50) NOT NULL,
    date_description VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS chat_messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sender_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    sender_name VARCHAR(255),
    message_text VARCHAR(2000) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
