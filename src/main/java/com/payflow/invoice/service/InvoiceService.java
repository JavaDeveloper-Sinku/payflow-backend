
package com.payflow.invoice.service;

import com.payflow.invoice.dto.CreateInvoiceRequest;
import com.payflow.invoice.dto.InvoiceResponse;
import com.payflow.invoice.dto.UpdateInvoiceStatusRequest;
import com.payflow.invoice.entity.Invoice;
import com.payflow.invoice.repository.InvoiceRepository;
import com.payflow.subscription.entity.Subscription;
import com.payflow.subscription.repository.SubscriptionRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final SubscriptionRepository subscriptionRepository;

    public InvoiceService(
            InvoiceRepository invoiceRepository,
            SubscriptionRepository subscriptionRepository
    ) {
        this.invoiceRepository = invoiceRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @Transactional
    public InvoiceResponse createInvoice(
            Long companyId,
            CreateInvoiceRequest request
    ) {
        if (request.getDueDate().isBefore(request.getIssueDate())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Due date cannot be before issue date"
            );
        }

        Subscription subscription = subscriptionRepository
                .findByIdAndCompany_Id(
                        request.getSubscriptionId(),
                        companyId
                )
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Subscription not found"
                ));

        String invoiceNumber = generateInvoiceNumber(companyId);

        Invoice invoice = Invoice.builder()
                .company(subscription.getCompany())
                .subscription(subscription)
                .invoiceNumber(invoiceNumber)
                .amount(subscription.getPlan().getPrice())
                .currency("INR")
                .status(Invoice.InvoiceStatus.PENDING)
                .issueDate(request.getIssueDate())
                .dueDate(request.getDueDate())
                .build();

        return toResponse(invoiceRepository.save(invoice));
    }

    @Transactional(readOnly = true)
    public List<InvoiceResponse> getInvoices(Long companyId) {
        return invoiceRepository.findByCompany_Id(companyId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public InvoiceResponse getInvoice(
            Long companyId,
            Long invoiceId
    ) {
        return toResponse(findInvoice(companyId, invoiceId));
    }

    @Transactional
    public InvoiceResponse updateStatus(
            Long companyId,
            Long invoiceId,
            UpdateInvoiceStatusRequest request
    ) {
        Invoice invoice = findInvoice(companyId, invoiceId);
        Invoice.InvoiceStatus newStatus = request.getStatus();

        if (invoice.getStatus() == Invoice.InvoiceStatus.PAID
                && newStatus != Invoice.InvoiceStatus.PAID) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A paid invoice cannot be changed to another status"
            );
        }

        invoice.setStatus(newStatus);

        return toResponse(invoiceRepository.save(invoice));
    }

    @Transactional
    public void deleteInvoice(Long companyId, Long invoiceId) {
        Invoice invoice = findInvoice(companyId, invoiceId);
        invoiceRepository.delete(invoice);
    }

    private Invoice findInvoice(Long companyId, Long invoiceId) {
        return invoiceRepository.findByIdAndCompany_Id(
                        invoiceId,
                        companyId
                )
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Invoice not found"
                ));
    }

    private String generateInvoiceNumber(Long companyId) {
        String invoiceNumber;

        do {
            invoiceNumber = "INV-" + LocalDate.now().getYear()
                    + "-" + java.util.UUID.randomUUID()
                    .toString()
                    .substring(0, 8)
                    .toUpperCase();
        } while (invoiceRepository.existsByCompany_IdAndInvoiceNumber(
                companyId,
                invoiceNumber
        ));

        return invoiceNumber;
    }

    private InvoiceResponse toResponse(Invoice invoice) {
        Subscription subscription = invoice.getSubscription();

        return InvoiceResponse.builder()
                .invoiceId(invoice.getId())
                .companyId(invoice.getCompany().getId())
                .subscriptionId(subscription.getId())
                .customerId(subscription.getCustomer().getId())
                .customerName(subscription.getCustomer().getName())
                .invoiceNumber(invoice.getInvoiceNumber())
                .amount(invoice.getAmount())
                .currency(invoice.getCurrency())
                .status(invoice.getStatus())
                .issueDate(invoice.getIssueDate())
                .dueDate(invoice.getDueDate())
                .createdAt(invoice.getCreatedAt())
                .updatedAt(invoice.getUpdatedAt())
                .build();
    }
}
