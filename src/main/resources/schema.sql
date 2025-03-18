 CREATE TABLE IF NOT EXISTS product (
     id SERIAL PRIMARY KEY,
     code VARCHAR(10) NOT NULL UNIQUE,
     name VARCHAR(255) NOT NULL,
     price_eur DECIMAL(10, 2) NOT NULL,
     price_usd DECIMAL(10, 2) NOT NULL,
     is_available BOOLEAN NOT NULL
 );