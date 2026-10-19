
package com.payflow.invoice.dto;

import com.payflow.invoice.entity.Invoice.InvoiceStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateInvoiceStatusRequest {

    @NotNull(message = "Invoice status is required")
    private InvoiceStatus status;
}
