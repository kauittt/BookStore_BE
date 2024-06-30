SET FOREIGN_KEY_CHECKS = 0;

INSERT INTO authorities (id, authority) VALUES ('1', 'ROLE_USER');
INSERT INTO authorities (id, authority) VALUES ('2', 'ROLE_ADMIN');

INSERT INTO users (id, username, password, enabled, email, name, phone, address, dateCreated, dateUpdated)
VALUES ('1', 'user', 'user', 1, 'user1@example.com', 'User One', '1234567890', 'Address One', '2024-06-30 09:00:00', '2024-06-30 09:00:00');
INSERT INTO users (id, username, password, enabled, email, name, phone, address, dateCreated, dateUpdated)
VALUES ('2', 'admin', 'admin', 1, 'user2@example.com', 'User Two', '0987654321', 'Address Two', '2024-06-30 09:30:00', '2024-06-30 09:30:00');

INSERT INTO users_authorities (userId, authorityId) VALUES ('1', '1');
INSERT INTO users_authorities (userId, authorityId) VALUES ('2', '2');

-- Insert data into books
INSERT INTO books (id, name, image, description, price) VALUES ('1', 'Book One', 'image1.jpg', 'Description of Book One', 19.99);
INSERT INTO books (id, name, image, description, price) VALUES ('2', 'Book Two', 'image2.jpg', 'Description of Book Two', 29.99);
INSERT INTO books (id, name, image, description, price) VALUES ('3', 'Book Three', 'image3.jpg', 'Description of Book Three', 39.99);
INSERT INTO books (id, name, image, description, price) VALUES ('4', 'Book Four', 'image4.jpg', 'Description of Book Four', 49.99);

-- Insert data into authors
INSERT INTO authors (id, name) VALUES ('1', 'Author One');
INSERT INTO authors (id, name) VALUES ('2', 'Author Two');
INSERT INTO authors (id, name) VALUES ('3', 'Author Three');
INSERT INTO authors (id, name) VALUES ('4', 'Author Four');

-- Insert data into books_authors
INSERT INTO books_authors (bookId, authorId) VALUES ('1', '1');
INSERT INTO books_authors (bookId, authorId) VALUES ('1', '2');
INSERT INTO books_authors (bookId, authorId) VALUES ('2', '2');
INSERT INTO books_authors (bookId, authorId) VALUES ('3', '3');
INSERT INTO books_authors (bookId, authorId) VALUES ('4', '4');

-- Insert data into carts
INSERT INTO carts (id, userId) VALUES ('1', '1');
INSERT INTO carts (id, userId) VALUES ('2', '2');
INSERT INTO carts (id, userId) VALUES ('3', '3');
INSERT INTO carts (id, userId) VALUES ('4', '4');

-- Insert data into carts_books
INSERT INTO carts_books (cartId, bookId) VALUES ('1', '1');
INSERT INTO carts_books (cartId, bookId) VALUES ('1', '2');
INSERT INTO carts_books (cartId, bookId) VALUES ('2', '2');
INSERT INTO carts_books (cartId, bookId) VALUES ('3', '3');
INSERT INTO carts_books (cartId, bookId) VALUES ('4', '4');

-- Insert data into orders
INSERT INTO orders (id, userId, dateCreate, total, quantity) VALUES ('1', '1', '2024-06-30 10:00:00', 39.98, 2);
INSERT INTO orders (id, userId, dateCreate, total, quantity) VALUES ('2', '2', '2024-06-30 11:00:00', 59.98, 2);
INSERT INTO orders (id, userId, dateCreate, total, quantity) VALUES ('3', '3', '2024-06-30 12:00:00', 79.98, 2);
INSERT INTO orders (id, userId, dateCreate, total, quantity) VALUES ('4', '4', '2024-06-30 13:00:00', 99.98, 2);

-- Insert data into orders_books
INSERT INTO orders_books (orderId, bookId) VALUES ('1', '1');
INSERT INTO orders_books (orderId, bookId) VALUES ('1', '2');
INSERT INTO orders_books (orderId, bookId) VALUES ('2', '2');
INSERT INTO orders_books (orderId, bookId) VALUES ('2', '3');
INSERT INTO orders_books (orderId, bookId) VALUES ('3', '3');
INSERT INTO orders_books (orderId, bookId) VALUES ('3', '4');
INSERT INTO orders_books (orderId, bookId) VALUES ('4', '1');
INSERT INTO orders_books (orderId, bookId) VALUES ('4', '4');

SET FOREIGN_KEY_CHECKS = 1;