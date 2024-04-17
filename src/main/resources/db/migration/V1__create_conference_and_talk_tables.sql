CREATE SEQUENCE conference_seq START WITH 5 INCREMENT BY 1;

CREATE TABLE conference (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    topic VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    number_of_participants INT NOT NULL CHECK (number_of_participants > 100)
);

CREATE SEQUENCE talk_seq START WITH 15 INCREMENT BY 1;

CREATE TABLE talk (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    speaker_name VARCHAR(255) NOT NULL,
    talk_type VARCHAR(20) NOT NULL,
    conference_id INT NOT NULL,
    CONSTRAINT fk_conference_id FOREIGN KEY (conference_id) REFERENCES conference(id),
    CONSTRAINT uc_talk_title UNIQUE (title),
    CONSTRAINT chk_talk_type CHECK (talk_type IN ('REPORT', 'MASTER_CLASS', 'WORKSHOP'))
);

-- Insert sample conferences
INSERT INTO conference (name, topic, start_date, end_date, number_of_participants)
VALUES ('Conference A', 'Technology', '2024-05-15', '2024-05-17', 200),
       ('Conference B', 'Science', '2024-06-10', '2024-06-12', 150),
       ('Conference C', 'Business', '2024-07-20', '2024-07-22', 120);

-- Insert sample talks
INSERT INTO talk (title, description, speaker_name, talk_type, conference_id)
VALUES ('Talk 1', 'Introduction to Spring Boot', 'John Doe', 'REPORT', 1),
       ('Talk 2', 'Advanced SQL Queries', 'Jane Smith', 'MASTER_CLASS', 1),
       ('Talk 3', 'JavaScript Workshop', 'Alice Brown', 'WORKSHOP', 1),
       ('Talk 4', 'Microservices Architecture', 'David Johnson', 'REPORT', 2),
       ('Talk 5', 'Machine Learning in Healthcare', 'Emily White', 'MASTER_CLASS', 2),
       ('Talk 6', 'Agile Project Management', 'Chris Green', 'WORKSHOP', 2),
       ('Talk 7', 'Startup Funding Strategies', 'Michael Clark', 'REPORT', 3),
       ('Talk 8', 'Leadership Skills', 'Sophia Lee', 'MASTER_CLASS', 3),
       ('Talk 9', 'Digital Marketing Trends', 'Robert Taylor', 'WORKSHOP', 3);
