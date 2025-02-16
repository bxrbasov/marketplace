--liquibase formatted sql

--changeset bxrbasov:1
CREATE TYPE categories AS ENUM ('CAR', 'SMARTPHONE', 'LAPTOP','HEADPHONES','KEYBOARD','SCREEN');

--changeset bxrbasov:2
CREATE TYPE roles AS ENUM ('USER', 'ADMIN');

----changeset bxrbasov:3
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY ,
    username VARCHAR (128) NOT NULL UNIQUE ,
    email VARCHAR (128) NOT NULL UNIQUE ,
    password VARCHAR (128) NOT NULL ,
    role roles NOT NULL DEFAULT 'USER'
);

--changeset bxrbasov:4
CREATE TABLE products (
    id UUID PRIMARY KEY ,
    category categories NOT NULL ,
    name VARCHAR (128) NOT NULL ,
    description TEXT NOT NULL ,
    price DECIMAL NOT NULL ,
    quantity BIGINT NOT NULL ,
    sku VARCHAR (128) NOT NULL ,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    owner BIGINT REFERENCES users (id)
)
