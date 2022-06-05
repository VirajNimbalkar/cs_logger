CREATE TABLE log_alerts
(
    id    INT NOT NULL,
    log_id  VARCHAR(10),
    duration VARCHAR(50),
    is_alert INT,
    type VARCHAR(50),
    host VARCHAR(50),
    PRIMARY KEY (ID)
);