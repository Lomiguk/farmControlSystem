CREATE TYPE value_type AS ENUM ('LITER', 'KILOGRAM', 'PIECE');

CREATE TABLE product
(
    id        bigserial PRIMARY KEY,
    name      text       NOT NULL,
    value     value_type NOT NULL,
    is_actual bool DEFAULT false,

    CONSTRAINT product_name_u UNIQUE (name)
);

COMMENT ON COLUMN product.id IS 'Unique numerical product identifier';
COMMENT ON COLUMN product.name IS 'Product name';
COMMENT ON COLUMN product.value IS 'Type of product quantity measurement';
COMMENT ON COLUMN product.is_actual IS 'flag - is the product actual';