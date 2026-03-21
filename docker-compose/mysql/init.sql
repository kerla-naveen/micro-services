CREATE DATABASE order_db;
CREATE DATABASE product_db;
CREATE DATABASE payment_db;

CREATE USER 'app_user'@'%' IDENTIFIED BY 'password';

GRANT ALL PRIVILEGES ON order_db.* TO 'app_user'@'%';
GRANT ALL PRIVILEGES ON product_db.* TO 'app_user'@'%';
GRANT ALL PRIVILEGES ON payment_db.* TO 'app_user'@'%';

FLUSH PRIVILEGES;