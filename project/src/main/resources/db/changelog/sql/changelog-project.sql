--liquibase formatted sql
--changeset thabani:create_project_table
CREATE TABLE project(
    id                  UUID PRIMARY KEY,
    version             BIGINT NOT NULL,
    deleted             BOOLEAN NOT NULL,
    created_date        TIMESTAMP,
    created_by          VARCHAR(255),
    last_modified_by    VARCHAR(255),
    last_modified_date  TIMESTAMP,
    code                VARCHAR(255) NOT NULL UNIQUE,
    name                VARCHAR(255) NOT NULL,
    client_code         VARCHAR(255) NOT NULL,
    start_date          TIMESTAMP NOT NULL,
    end_date            TIMESTAMP,
    type                VARCHAR(255) NOT NULL
);