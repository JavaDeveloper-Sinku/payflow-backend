
package com.payflow.invoice.dto;

import com.payflow.invoice.entity.Invoice.InvoiceStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class InvoiceResponse {

    private Long invoiceId;
    private Long companyId;

    private Long subscriptionId;
    private Long customerId;
    private String customerName;

    private String invoiceNumber;
    private BigDecimal amount;
    private String currency;

    private InvoiceStatus status;

    private LocalDate issueDate;
    private LocalDate dueDate;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
