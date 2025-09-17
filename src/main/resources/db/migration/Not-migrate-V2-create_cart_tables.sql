create table carts
(
    id uuid not null
        constraint carts_pk primary key,
    date_created date default CURRENT_DATE not null
);

create table cart_items
(
    id bigint not null constraint cart_items_pk unique,
    cart_id uuid not null constraint cart_items_carts_id_fk
     references carts (id) on delete cascade,
    product_id bigint not null constraint cart_items_products_id_fk
            references products on delete cascade,
    quantity   int default 1 not null,
    unique (),
    constraint cart_items_cart_product_unique
        unique (cart_id, product_id)
);

CREATE EXTENSION IF NOT EXISTS "pgcrypto";
ALTER TABLE carts
    ALTER COLUMN id SET DEFAULT gen_random_uuid();

ALTER TABLE carts
    ALTER COLUMN date_created SET DEFAULT CURRENT_DATE;

CREATE SEQUENCE cart_items_id_seq START 2 INCREMENT 1;
ALTER TABLE cart_items
    ALTER COLUMN id SET DEFAULT nextval('cart_items_id_seq');

