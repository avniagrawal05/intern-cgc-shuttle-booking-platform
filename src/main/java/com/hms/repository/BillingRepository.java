package com.hms.repository;

import com.hms.entity.Billing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Billing entity database operations
 */
@Repository
public interface BillingRepository extends JpaRepository<Billing, Long> {
    
    /**
     * Find billings by patient ID
     */
    List<Billing> findByPatientIdAndIsDeletedFalseOrderByCreatedAtDesc(Long patientId);
    
    /**
     * Find billing by invoice number
     */
    Optional<Billing> findByInvoiceNumberAndIsDeletedFalse(String invoiceNumber);
    
    /**
     * Find billings by payment status
     */
    List<Billing> findByPaymentStatusAndIsDeletedFalse(String paymentStatus);
    
    /**
     * Get total amount pending for a patient
     */
    @Query("SELECT COALESCE(SUM(b.amount), 0) FROM Billing b WHERE b.patientId = :patientId AND b.paymentStatus = 'PENDING' AND b.isDeleted = false")
    BigDecimal getTotalPendingAmount(@Param("patientId") Long patientId);
    
    /**
     * Get total amount received from a patient
     */
    @Query("SELECT COALESCE(SUM(b.amount), 0) FROM Billing b WHERE b.patientId = :patientId AND b.paymentStatus = 'PAID' AND b.isDeleted = false")
    BigDecimal getTotalPaidAmount(@Param("patientId") Long patientId);
    
    /**
     * Find pending billings with pagination
     */
    Page<Billing> findByPaymentStatusAndIsDeletedFalseOrderByCreatedAtDesc(String paymentStatus, Pageable pageable);
    
    /**
     * Count pending billings for a patient
     */
    long countByPatientIdAndPaymentStatusAndIsDeletedFalse(Long patientId, String paymentStatus);
}
