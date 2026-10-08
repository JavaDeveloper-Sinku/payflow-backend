CREATE TABLE customers (
                           id BIGINT NOT NULL AUTO_INCREMENT,

                           company_id BIGINT NOT NULL,

                           name VARCHAR(150) NOT NULL,
                           email VARCHAR(150) NOT NULL,
                           phone VARCHAR(20),

                           enabled BOOLEAN NOT NULL DEFAULT TRUE,

                           created_at DATETIME NOT NULL,
                           updated_at DATETIME NOT NULL,

                           PRIMARY KEY (id),

                           CONSTRAINT fk_customers_company
                               FOREIGN KEY (company_id)
                                   REFERENCES companies(id)
);