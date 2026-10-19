
CREATE TABLE plans (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       product_id BIGINT NOT NULL,
                       name VARCHAR(150) NOT NULL,
                       description VARCHAR(500),
                       price DECIMAL(12, 2) NOT NULL,
                       billing_interval VARCHAR(20) NOT NULL,
                       enabled BOOLEAN NOT NULL DEFAULT TRUE,
                       created_at DATETIME NOT NULL,
                       updated_at DATETIME NOT NULL,

                       PRIMARY KEY (id),

                       CONSTRAINT fk_plans_product
                           FOREIGN KEY (product_id)
                               REFERENCES products(id),

                       CONSTRAINT chk_plans_price
                           CHECK (price >= 0),

                       CONSTRAINT chk_plans_billing_interval
                           CHECK (billing_interval IN ('MONTHLY', 'YEARLY'))
);
