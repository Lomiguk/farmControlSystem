CREATE TABLE task
(
    id          bigserial PRIMARY KEY,
    start_date  date   NOT NULL,
    end_date    date,
    description text,
    profile_id  bigint NOT NULL,
    product_id  bigint NOT NULL,
    value       float4 NOT NULL,
    is_done     bool DEFAULT false,
    is_abort    bool DEFAULT false,

    CONSTRAINT task_profile_fk FOREIGN KEY (profile_id) REFERENCES profile (id),
    CONSTRAINT task_product_fk FOREIGN KEY (product_id) REFERENCES product (id),
    CONSTRAINT task_check_dates CHECK (start_date < end_date)
);

COMMENT ON COLUMN task.id IS 'Unique numerical mark identifier';
COMMENT ON COLUMN task.start_date IS 'Day of the stat task';
COMMENT ON COLUMN task.end_date IS 'Day of the stat task';
COMMENT ON COLUMN task.description IS 'Task description';
COMMENT ON COLUMN task.profile_id IS 'ID of the Profile assigned to a task';
COMMENT ON COLUMN task.product_id IS 'ID of the Product assigned to a task';
COMMENT ON COLUMN task.value IS 'Scope of work';
COMMENT ON COLUMN task.is_done IS 'Flag - is the task completed';
COMMENT ON COLUMN task.is_abort IS 'Flag - is the task aborted';
