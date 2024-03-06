CREATE TABLE profile
(
    id         bigserial     PRIMARY KEY,
    fio        VARCHAR(50)   NOT NULL,
    email      VARCHAR(255)  NOT NULL,
    password   text          NOT NULL,
    is_admin   BOOLEAN       NOT NULL DEFAULT false,
    is_actual  BOOLEAN       NOT NULL DEFAULT true,

    CONSTRAINT profile_email_u UNIQUE (email)
);

COMMENT ON COLUMN profile.id IS 'Уникальный идентификатор профиля';
COMMENT ON COLUMN profile.fio IS 'Фамилия, Имя, Отчество сотрудника';
COMMENT ON COLUMN profile.email IS 'Электронная почта сотрудника';
COMMENT ON COLUMN profile.password IS 'Хеш пароля сотрудника';
COMMENT ON COLUMN profile.is_admin IS 'Флаг обозначающий наличие или отсутствие расширенных прав доступа';
COMMENT ON COLUMN profile.is_actual IS 'Флаг является ли человек отражённый в профиле сотрудником на данный момент';