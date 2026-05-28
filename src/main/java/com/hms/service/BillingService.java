package com.hms.service;

import com.hms.dto.BillingDto;
import com.hms.util.PagedResponse;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service interface for Billing operations
 * Defines business logic methods for billing management
 */
public interface BillingService {
    
    /**
     * Get all billings with pagination
     */
    PagedResponse<BillingDto> getAllBillings(int page, int size);
    
    /**
     * Get billing by ID
     */
    BillingDto getBillingById(Long id);
    
    /**
     * Create new billing
     */
    BillingDto createBilling(BillingDto billingDto);
    
    /**
     * Update existing billing
     */
    BillingDto updateBilling(Long id, BillingDto billingDto);
    
    /**
     * Delete billing (soft delete)
     */
    void deleteBilling(Long id);
    
    /**
     * Get patient's billings
     */
    List<BillingDto> getPatientBillings(Long patientId);
    
    /**
     * Get total pending amount for patient
     */
    BigDecimal getTotalPendingAmount(Long patientId);
    
    /**
     * Get total paid amount for patient
     */
    BigDecimal getTotalPaidAmount(Long patientId);
    
    /**
     * Mark billing as paid
     */
    BillingDto markAsPaid(Long id, String paymentMethod);
    
    /**
     * Get pending billings
     */
    PagedResponse<BillingDto> getPendingBillings(int page, int size);
    
    /**
     * Generate invoice number
     */
    String generateInvoiceNumber();
}
