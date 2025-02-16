--liquibase formatted sql

--changeset bxrbasov:1
CREATE TABLE chats (
    id BIGSERIAL PRIMARY KEY ,
    name VARCHAR (256) NOT NULL ,
    topic UUID REFERENCES products (id)
);

--changeset bxrbasov:2
CREATE TABLE users_chats (
    id BIGSERIAL PRIMARY KEY ,
    user_id BIGINT REFERENCES users (id) ,
    chat_id BIGINT REFERENCES chats (id) ,
    UNIQUE (user_id, chat_id)
);

--changeset bxrbasov:3
CREATE TABLE messages (
    id BIGSERIAL PRIMARY KEY ,
    content TEXT NOT NULL ,
    owner BIGINT REFERENCES users (id) ,
    chat BIGINT REFERENCES chats (id) ,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)
