CREATE TABLE task (
    id          bigserial PRIMARY KEY,
    start_date  date      NOT NULL,
    end_date    date,
    description text,
    profile_id  bigint    NOT NULL,
    product_id  bigint    NOT NULL,
    value       float4    NOT NULL,
    is_done     bool      DEFAULT false,
    is_abort    bool      DEFAULT false,

    CONSTRAINT task_profile_fk FOREIGN KEY (profile_id) REFERENCES profile(id),
    CONSTRAINT task_product_fk FOREIGN KEY (product_id) REFERENCES product(id),
    CONSTRAINT task_check_dates CHECK (start_date < end_date)
);

COMMENT ON COLUMN task.id IS 'Уникальный идентификатор задачи';
COMMENT ON COLUMN task.start_date IS 'Время начала выполнения задания';
COMMENT ON COLUMN task.end_date IS 'Время окончания выполнения задания';
COMMENT ON COLUMN task.description IS 'Описание задания';
COMMENT ON COLUMN task.profile_id IS 'Идентификатор профиля';
COMMENT ON COLUMN task.product_id IS 'Идентификатор продукта';
COMMENT ON COLUMN task.value IS 'Объём работ';
COMMENT ON COLUMN task.is_done IS 'Флаг - завершено ли задания';
COMMENT ON COLUMN task.is_abort IS 'Флаг - отменено ли задание';
