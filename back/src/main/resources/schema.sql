CREATE TABLE IF NOT EXISTS member
(
    id          BIGSERIAL PRIMARY KEY,
    provider    VARCHAR(30)   NOT NULL,
    provider_id VARCHAR(255)  NOT NULL,
    email       VARCHAR(255)  NOT NULL,
    name        VARCHAR(100)  NOT NULL,
    picture_url VARCHAR(2048),
    created_at  TIMESTAMP     NOT NULL,
    updated_at  TIMESTAMP     NOT NULL,
    CONSTRAINT uk_member_provider_provider_id UNIQUE (provider, provider_id),
    CONSTRAINT uk_member_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS company
(
    id                BIGSERIAL PRIMARY KEY,
    name              VARCHAR(255) NOT NULL,
    target_status     VARCHAR(10)  NOT NULL DEFAULT 'O',
    job_posting_url   VARCHAR(2048),
    recruitment_deadline DATE,
    job_change_status VARCHAR(50)  NOT NULL DEFAULT 'NOT_APPLIED',
    scores            TEXT         NOT NULL DEFAULT '[]',
    memo              TEXT,
    created_at        TIMESTAMP    NOT NULL,
    updated_at        TIMESTAMP    NOT NULL
);

CREATE TABLE IF NOT EXISTS kanban_column_config
(
    id         BIGSERIAL PRIMARY KEY,
    columns    TEXT      NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
