CREATE TABLE IF NOT EXISTS products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    description TEXT NOT NULL,
    value FLOAT NOT NULL,
    image VARCHAR(255) NOT NULL,
    amount FLOAT NOT NULL,
    category TEXT NOT NULL
);
