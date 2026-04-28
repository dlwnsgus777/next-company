CREATE TABLE IF NOT EXISTS company
(
    id                BIGSERIAL PRIMARY KEY,
    name              VARCHAR(255) NOT NULL,
    job_change_status VARCHAR(50)  NOT NULL DEFAULT 'NOT_APPLIED',
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