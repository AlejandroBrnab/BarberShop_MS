USE `reviews-db`;

-- Table for Review entity
CREATE TABLE IF NOT EXISTS reviews (
    id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    review_id VARCHAR(36),
    description VARCHAR(250),
    score INTEGER
);