CREATE TABLE IF NOT EXISTS person (
    id       VARCHAR(60)  DEFAULT RANDOM_UUID() PRIMARY KEY,
    name     VARCHAR      NOT NULL,
    company  VARCHAR      NOT NULL,
    avatar   VARCHAR      NOT NULL,
    liked    BOOLEAN          NOT NULL,
    fired    BOOLEAN          NOT NULL
    );