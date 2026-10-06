CREATE TABLE companies (
                           id BIGINT NOT NULL AUTO_INCREMENT,
                           name VARCHAR(150) NOT NULL,
                           created_at DATETIME NOT NULL,
                           updated_at DATETIME NOT NULL,
                           PRIMARY KEY (id)
);