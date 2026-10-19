
package com.payflow.invoice.repository;

import com.payflow.invoice.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findByCompany_Id(Long companyId);

    Optional<Invoice> findByIdAndCompany_Id(
            Long invoiceId,
            Long companyId
    );

    boolean existsByCompany_IdAndInvoiceNumber(
            Long companyId,
            String invoiceNumber
    );
}
