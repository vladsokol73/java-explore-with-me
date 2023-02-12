drop table IF EXISTS hits cascade;

CREATE TABLE IF NOT EXISTS hits
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    app       VARCHAR(100),
    uri       VARCHAR(255),
    ip        VARCHAR(50),
    timestamp TIMESTAMP,
    CONSTRAINT pk_hits PRIMARY KEY (id)
);

