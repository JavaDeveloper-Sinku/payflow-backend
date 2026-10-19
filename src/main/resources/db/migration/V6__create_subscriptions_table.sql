
CREATE TABLE subscriptions (
                               id BIGINT NOT NULL AUTO_INCREMENT,
                               company_id BIGINT NOT NULL,
                               customer_id BIGINT NOT NULL,
                               plan_id BIGINT NOT NULL,
                               status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
                               start_date DATE NOT NULL,
                               next_billing_date DATE,
                               created_at DATETIME NOT NULL,
                               updated_at DATETIME NOT NULL,

                               PRIMARY KEY (id),

                               CONSTRAINT fk_subscriptions_company
                                   FOREIGN KEY (company_id) REFERENCES companies(id),

                               CONSTRAINT fk_subscriptions_customer
                                   FOREIGN KEY (customer_id) REFERENCES customers(id),

                               CONSTRAINT fk_subscriptions_plan
                                   FOREIGN KEY (plan_id) REFERENCES plans(id),

                               CONSTRAINT chk_subscription_status
                                   CHECK (status IN ('ACTIVE', 'CANCELLED', 'EXPIRED'))
);
