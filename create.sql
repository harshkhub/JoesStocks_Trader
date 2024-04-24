CREATE TABLE Users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    balance DECIMAL(10, 2) 
);

CREATE TABLE Portfolio (
    trade_id INT PRIMARY KEY,
    user_id INT,
    ticker VARCHAR(10),
    numStock INT,
    price DECIMAL(10, 2), 
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);