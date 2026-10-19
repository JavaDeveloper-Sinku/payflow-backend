CREATE TABLE invoices (
                          id BIGINT NOT NULL AUTO_INCREMENT,
                          company_id BIGINT NOT NULL,
                          subscription_id BIGINT NOT NULL,
                          invoice_number VARCHAR(50) NOT NULL,
                          amount DECIMAL(12, 2) NOT NULL,
                          currency VARCHAR(3) NOT NULL DEFAULT 'INR',
                          status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
                          issue_date DATE NOT NULL,
                          due_date DATE NOT NULL,
                          created_at DATETIME NOT NULL,
                          updated_at DATETIME NOT NULL,

                          PRIMARY KEY (id),

                          CONSTRAINT uq_invoice_number
                              UNIQUE (company_id, invoice_number),

                          CONSTRAINT fk_invoices_company
                              FOREIGN KEY (company_id) REFERENCES companies(id),

                          CONSTRAINT fk_invoices_subscription
                              FOREIGN KEY (subscription_id) REFERENCES subscriptions(id),

                          CONSTRAINT chk_invoice_amount
                              CHECK (amount >= 0),

                          CONSTRAINT chk_invoice_status
                              CHECK (status IN ('PENDING', 'PAID', 'OVERDUE', 'CANCELLED'))
);