create TABLE IF NOT EXISTS delivery(
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    userId INTEGER NOT NULL,
    city VARCHAR(50) NOT NULL,
    street VARCHAR(50) NOT NULL,
    number INTEGER NOT NULL,
    state VARCHAR(50) NOT NULL,
    createdAt TIMESTAMP,
    estimatedDate TIMESTAMP,
    delieveredOn TIMESTAMP
);