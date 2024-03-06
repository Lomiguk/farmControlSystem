CREATE TABLE action (
    id          bigserial                    PRIMARY KEY,
    profile_id  BIGINT                       NOT NULL,
    product_id  BIGINT                       NOT NULL,
    value       float4                       NOT NULL,
    time        timestamp without time zone  NOT NULL,

    CONSTRAINT action_profile_fk FOREIGN KEY (profile_id) REFERENCES profile(id),
    CONSTRAINT action_product_fk FOREIGN KEY (product_id) REFERENCES product(id)
);

COMMENT ON COLUMN action.id IS 'Уникальный идентификатор действия сотрудника';
COMMENT ON COLUMN action.profile_id IS 'Идентификатор пользователя';
COMMENT ON COLUMN action.product_id IS 'Идентификатор продукта';
COMMENT ON COLUMN action.value IS 'Кол-во / масса / объём';
COMMENT ON COLUMN action.time IS 'Время выполнения';