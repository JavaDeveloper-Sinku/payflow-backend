
package com.payflow.invoice.controller;

import com.payflow.invoice.dto.CreateInvoiceRequest;
import com.payflow.invoice.dto.InvoiceResponse;
import com.payflow.invoice.dto.UpdateInvoiceStatusRequest;
import com.payflow.invoice.service.InvoiceService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InvoiceResponse createInvoice(
            @RequestParam Long companyId,
            @Valid @RequestBody CreateInvoiceRequest request
    ) {
        return invoiceService.createInvoice(companyId, request);
    }

    @GetMapping
    public List<InvoiceResponse> getInvoices(
            @RequestParam Long companyId
    ) {
        return invoiceService.getInvoices(companyId);
    }

    @GetMapping("/{invoiceId}")
    public InvoiceResponse getInvoice(
            @RequestParam Long companyId,
            @PathVariable Long invoiceId
    ) {
        return invoiceService.getInvoice(companyId, invoiceId);
    }

    @PatchMapping("/{invoiceId}/status")
    public InvoiceResponse updateStatus(
            @RequestParam Long companyId,
            @PathVariable Long invoiceId,
            @Valid @RequestBody UpdateInvoiceStatusRequest request
    ) {
        return invoiceService.updateStatus(
                companyId, invoiceId, request
        );
    }

    @DeleteMapping("/{invoiceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInvoice(
            @RequestParam Long companyId,
            @PathVariable Long invoiceId
    ) {
        invoiceService.deleteInvoice(companyId, invoiceId);
    }
}
