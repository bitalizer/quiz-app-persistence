-- Insert test data into topics table
INSERT INTO topics (name) VALUES
    ('Mathematics'),
    ('Science'),
    ('History'),
    ('Geography');

-- Insert test data into questions table
INSERT INTO questions (difficulty, content, topic_id) VALUES
    (1, 'What is 2 + 2?', 1),
    (3, 'Who discovered penicillin?', 2),
    (5, 'When was the Declaration of Independence signed?', 3),
    (6, 'What is the capital of France?', 4);

-- Insert test data into responses table
INSERT INTO responses (question_id, correct, text) VALUES
    (1, true, '4'),
    (2, true, 'Alexander Fleming'),
    (3, true, '1776'),
    (4, true, 'Paris');

-- Insert test data into quizzes table
INSERT INTO quizzes (name) VALUES
    ('Math Quiz'),
    ('Science Quiz'),
    ('History Quiz');

-- Insert test data into quiz_question table (associating questions with quizzes)
INSERT INTO quiz_question (quiz_id, question_id) VALUES
    (1, 1),
    (2, 2),
    (3, 3);