--liquibase formatted sql
--changeset thabani:create_allocation_table
CREATE TABLE IF NOT EXISTS allocation(
    id                  UUID PRIMARY KEY,
    version             BIGINT NOT NULL,
    deleted             BOOLEAN NOT NULL,
    employee_id         UUID NOT NULL,
    project_id          UUID NOT NULL,
    start_date          DATE NOT NULL,
    expected_end_date   DATE NOT NULL,
    end_date            DATE,
    created_date        TIMESTAMP,
    created_by          VARCHAR(255),
    last_modified_by    VARCHAR(255),
    last_modified_date  TIMESTAMP
);