CREATE TABLE jwt_token
(
    id         bigserial PRIMARY KEY,
    profile_id bigint    NOT NULL,
    token      text      NOT NULL,
    type       text      NOT NULL DEFAULT 'ACCESS'::text,

    CONSTRAINT jwt_token_profile_id_fk FOREIGN KEY (profile_id) REFERENCES public.profile(id)
);

COMMENT ON COLUMN jwt_token.id IS 'Unique numerical token identifier';
COMMENT ON COLUMN jwt_token.profile_id IS 'Profile id associated with token';
COMMENT ON COLUMN jwt_token.token IS 'Token value';
COMMENT ON COLUMN jwt_token.type IS 'Token type';