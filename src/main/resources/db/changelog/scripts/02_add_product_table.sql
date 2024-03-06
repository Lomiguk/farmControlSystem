CREATE TYPE value_type AS ENUM('LITER', 'KILOGRAM', 'PIECE');

CREATE TABLE product (
    id     bigserial   PRIMARY KEY,
    name   text        NOT NULL,
    value  value_type  NOT NULL
);

COMMENT ON COLUMN product.id IS 'Уникальный идентификатор продукта';
COMMENT ON COLUMN product.name IS 'Наименование продукта';
COMMENT ON COLUMN product.value IS 'Тип оценки кол-ва продукта';