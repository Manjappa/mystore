CREATE TABLE cart
(
    id uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    dateCreated     DATE NOT NULL,
    email    VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE addresses
(
    id      BIGSERIAL PRIMARY KEY,
    street  VARCHAR(255) NOT NULL,
    city    VARCHAR(255) NOT NULL,
    state   VARCHAR(255) NOT NULL,
    zip     VARCHAR(255) NOT NULL,
    user_id BIGINT       NOT NULL,
    CONSTRAINT addresses_users_id_fk FOREIGN KEY (user_id)
        REFERENCES users (id) ON DELETE NO ACTION
);
CREATE INDEX addresses_users_id_fk_idx ON addresses (user_id);

CREATE TABLE categories
(
    id   SMALLSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE products
(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255)   NOT NULL,
    price NUMERIC(10, 2) NOT NULL,
    description TEXT NOT NULL,
    category_id SMALLINT NULL,
    CONSTRAINT fk_category FOREIGN KEY (category_id)
        REFERENCES categories (id) ON DELETE NO ACTION
);

CREATE INDEX fk_category_idx ON products (category_id);

CREATE TABLE profiles
(
    id             BIGINT PRIMARY KEY,
    bio            TEXT NULL,
    phone_number   VARCHAR(15) NULL,
    date_of_birth  DATE NULL,
    loyalty_points INTEGER DEFAULT 0,
    CONSTRAINT profiles_id_fk_1 FOREIGN KEY (id)
        REFERENCES users (id) ON DELETE NO ACTION
);


CREATE TABLE wishlist
(
    product_id BIGINT NOT NULL,
    user_id    BIGINT NOT NULL,
    CONSTRAINT fk_wishlist_on_product FOREIGN KEY (product_id)
        REFERENCES products (id) ON DELETE CASCADE,
    CONSTRAINT fk_wishlist_on_user FOREIGN KEY (user_id)
        REFERENCES users (id) ON DELETE NO ACTION
);

--ALTER TABLE addresses
--    ADD CONSTRAINT addresses_users_id_fk FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE NO ACTION;

CREATE INDEX addresses_users_id_fk ON addresses (user_id);

--ALTER TABLE products
--    ADD CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE NO ACTION;

--CREATE INDEX fk_category ON products (category_id);

--ALTER TABLE wishlist
 --   ADD CONSTRAINT fk_wishlist_on_product FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE;

--ALTER TABLE wishlist
 --   ADD CONSTRAINT fk_wishlist_on_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE NO ACTION;

CREATE INDEX fk_wishlist_on_user_idx ON wishlist (user_id);

--ALTER TABLE profiles
 --   ADD CONSTRAINT profiles_id_fk_1 FOREIGN KEY (id) REFERENCES users (id) ON DELETE NO ACTION;