CREATE TYPE value_type AS ENUM('LITER', 'KILOGRAM', 'PIECE');

CREATE TABLE product (
    id         bigserial   PRIMARY KEY,
    name       text        NOT NULL,
    value      value_type  NOT NULL,
    is_actual  bool        DEFAULT false,

    CONSTRAINT product_name_u UNIQUE (name)
);

COMMENT ON COLUMN product.id IS 'Уникальный идентификатор продукта';
COMMENT ON COLUMN product.name IS 'Наименование продукта';
COMMENT ON COLUMN product.value IS 'Тип оценки кол-ва продукта';
COMMENT ON COLUMN product.is_actual IS 'Флаг - удалён ли продукт';