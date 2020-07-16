--liquibase formatted sql
--changeset thabani:create_task_table
CREATE TABLE task(
    id                  UUID PRIMARY KEY,
    version             BIGINT NOT NULL,
    deleted             BOOLEAN NOT NULL,
    created_date        TIMESTAMP,
    created_by          VARCHAR(255),
    last_modified_by    VARCHAR(255),
    last_modified_date  TIMESTAMP,
    code                VARCHAR(255),
    name                VARCHAR(255) NOT NULL,
    project_id          UUID NOT NULL;
);