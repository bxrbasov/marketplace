--liquibase formatted sql

--changeset bxrbasov:1
CREATE TABLE favorites (
    id BIGSERIAL PRIMARY KEY ,
    user_id BIGINT REFERENCES users (id) ,
    product_id UUID REFERENCES products (id) ,
    UNIQUE (user_id, product_id)
);

-- --changeset bxrbasov:2
-- alter table favorites
--     alter column id type NUMERIC(38) using id::NUMERIC(38);
