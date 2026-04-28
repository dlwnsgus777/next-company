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
