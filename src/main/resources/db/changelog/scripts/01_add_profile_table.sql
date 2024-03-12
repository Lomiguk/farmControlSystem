CREATE TYPE profile_status AS ENUM('ADMIN', 'USER');

CREATE TABLE profile
(
    id         bigserial         PRIMARY KEY,
    fio        VARCHAR(50)       NOT NULL,
    email      VARCHAR(255)      NOT NULL,
    password   text              NOT NULL,
    role       profile_status    NOT NULL DEFAULT 'USER'::profile_status,
    is_actual  BOOLEAN           NOT NULL DEFAULT true,

    CONSTRAINT profile_email_u UNIQUE (email)
);

COMMENT ON COLUMN profile.id IS 'Unique numerical profile identifier';
COMMENT ON COLUMN profile.fio IS 'Surname, Name, Patronymic';
COMMENT ON COLUMN profile.email IS 'Employee email';
COMMENT ON COLUMN profile.password IS 'Encoded password';
COMMENT ON COLUMN profile.role IS 'Employee role - set of permissions';
COMMENT ON COLUMN profile.is_actual IS 'flag - is the employee profile actual';