
CREATE TABLE app_user (
                          id BIGSERIAL PRIMARY KEY,
                          username VARCHAR(60) NOT NULL UNIQUE,
                          password_hash VARCHAR(100) NOT NULL,
                          enabled BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE user_role (
                           user_id BIGINT NOT NULL REFERENCES app_user(id),
                           role VARCHAR(20) NOT NULL,
                           PRIMARY KEY (user_id, role)
);

INSERT INTO app_user (username, password_hash)
VALUES
    ('user', '$2a$10$aqNgZCxPAWC762RBmBW39.JQfRTuRXs3jpmFlAd2Kfs130gVNhFJ6'),
    ('admin', '$2a$10$xn9K7dTY6gjM1Im8SsAb5eozPnfowvPIpWCmRoJo1z2zJjW5saUoe');

INSERT INTO user_role (user_id, role)
SELECT id, 'USER' FROM app_user;

INSERT INTO user_role (user_id, role)
SELECT id, 'ADMIN'
FROM app_user
WHERE username = 'admin';
