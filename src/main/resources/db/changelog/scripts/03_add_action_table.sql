CREATE TABLE action (
    id          bigserial                    PRIMARY KEY,
    profile_id  BIGINT                       NOT NULL,
    product_id  BIGINT                       NOT NULL,
    value       float4                       NOT NULL,
    time        timestamp without time zone  NOT NULL,
    is_actual   bool                         DEFAULT true,

    CONSTRAINT action_profile_fk FOREIGN KEY (profile_id) REFERENCES profile(id),
    CONSTRAINT action_product_fk FOREIGN KEY (product_id) REFERENCES product(id)
);

COMMENT ON COLUMN action.id IS 'Unique numerical action identifier';
COMMENT ON COLUMN action.profile_id IS 'ID of the responsible user';
COMMENT ON COLUMN action.product_id IS 'ID of the assembled product';
COMMENT ON COLUMN action.value IS 'Quantity / weight / volume';
COMMENT ON COLUMN action.time IS 'Moment of the check';
COMMENT ON COLUMN action.is_actual IS 'flag - is the action actual';