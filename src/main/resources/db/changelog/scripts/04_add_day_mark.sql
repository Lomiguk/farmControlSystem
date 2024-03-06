CREATE TABLE day_mark (
    id          bigserial   PRIMARY KEY,
    profile_id  bigint      NOT NULL,
    mark        int         NOT NULL,
    date        date        NOT NULL,

    UNIQUE (profile_id, date),
    CONSTRAINT day_mark_profile_fk FOREIGN KEY (profile_id) REFERENCES profile(id)
);

COMMENT ON COLUMN day_mark.id IS 'Уникальный идентификатор оценки';
COMMENT ON COLUMN day_mark.profile_id IS 'Идентификатор профиля';
COMMENT ON COLUMN day_mark.mark IS 'Оценка';
COMMENT ON COLUMN day_mark.date IS 'Дата выставления оценки';