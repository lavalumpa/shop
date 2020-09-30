
create TABLE user (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR (100) NOT NULL UNIQUE,
    password VARCHAR (50) NOT NULL,
    createdAt TIMESTAMP,
    lastModifiedAt TIMESTAMP
);

create TABLE cart (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    userId INTEGER NOT NULL
);

create TABLE cart_item (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    cartId INTEGER NOT NULL,
    itemId INTEGER NOT NULL,
    quantity INTEGER NOT NULL
);

create TABLE receipt(
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    userId INTEGER NOT NULL,
    createdAt TIMESTAMP,
    totalPrice INTEGER
);

create TABLE receipt_item(
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    receiptID INTEGER NOT NULL,
    itemID INTEGER NOT NULL,
    quantity INTEGER NOT NULL
);

create TABLE item(
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price INTEGER NOT NULL
);

create TABLE viewed_item(
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    userId INTEGER NOT NULL,
    itemId INTEGER NOT NULL,
    lastViewed TIMESTAMP
);