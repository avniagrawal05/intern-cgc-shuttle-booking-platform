package com.hms.api.v1.controller;

import com.hms.constants.AppConstants;
import com.hms.dto.BillingDto;
import com.hms.service.BillingService;
import com.hms.util.ApiResponse;
import com.hms.util.PagedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST Controller for Billing Management
 * Handles all billing-related API endpoints
 */
@Slf4j
@RestController
@RequestMapping(AppConstants.API_V1 + "/billings")
@Tag(name = "Billing Management", description = "APIs for managing billings")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BillingController {
    
    private final BillingService billingService;
    
    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }
    
    /**
     * Get all billings with pagination
     */
    @GetMapping
    @Operation(summary = "Get all billings", description = "Retrieve all billings with pagination")
    public ResponseEntity<ApiResponse<PagedResponse<BillingDto>>> getAllBillings(
            @Parameter(description = "Page number (0-indexed)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "20") int size) {
        
        log.info("API call: GET /billings - page: {}, size: {}", page, size);
        PagedResponse<BillingDto> response = billingService.getAllBillings(page, size);
        
        return ResponseEntity.ok(ApiResponse.success(response, "Billings retrieved successfully"));
    }
    
    /**
     * Get billing by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get billing by ID", description = "Retrieve a specific billing by its ID")
    public ResponseEntity<ApiResponse<BillingDto>> getBillingById(
            @Parameter(description = "Billing ID")
            @PathVariable Long id) {
        
        log.info("API call: GET /billings/{}", id);
        BillingDto billing = billingService.getBillingById(id);
        
        return ResponseEntity.ok(ApiResponse.success(billing, "Billing retrieved successfully"));
    }
    
    /**
     * Create new billing
     */
    @PostMapping
    @Operation(summary = "Create new billing", description = "Create a new billing record")
    public ResponseEntity<ApiResponse<BillingDto>> createBilling(
            @Valid @RequestBody BillingDto billingDto) {
        
        log.info("API call: POST /billings for patient: {}", billingDto.getPatientId());
        BillingDto createdBilling = billingService.createBilling(billingDto);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(createdBilling, "Billing created successfully"));
    }
    
    /**
     * Update billing
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update billing", description = "Update an existing billing record")
    public ResponseEntity<ApiResponse<BillingDto>> updateBilling(
            @Parameter(description = "Billing ID")
            @PathVariable Long id,
            @Valid @RequestBody BillingDto billingDto) {
        
        log.info("API call: PUT /billings/{}", id);
        BillingDto updatedBilling = billingService.updateBilling(id, billingDto);
        
        return ResponseEntity.ok(ApiResponse.success(updatedBilling, "Billing updated successfully"));
    }
    
    /**
     * Delete billing
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete billing", description = "Delete a billing record (soft delete)")
    public ResponseEntity<ApiResponse<?>> deleteBilling(
            @Parameter(description = "Billing ID")
            @PathVariable Long id) {
        
        log.info("API call: DELETE /billings/{}", id);
        billingService.deleteBilling(id);
        
        return ResponseEntity.ok(ApiResponse.success(null, "Billing deleted successfully"));
    }
    
    /**
     * Get patient's billings
     */
    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get patient billings", description = "Retrieve all billings for a patient")
    public ResponseEntity<ApiResponse<List<BillingDto>>> getPatientBillings(
            @Parameter(description = "Patient ID")
            @PathVariable Long patientId) {
        
        log.info("API call: GET /billings/patient/{}", patientId);
        List<BillingDto> billings = billingService.getPatientBillings(patientId);
        
        return ResponseEntity.ok(ApiResponse.success(billings, "Billings found: " + billings.size()));
    }
    
    /**
     * Get total pending amount for patient
     */
    @GetMapping("/patient/{patientId}/pending-amount")
    @Operation(summary = "Get pending amount", description = "Get total pending amount for a patient")
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalPendingAmount(
            @Parameter(description = "Patient ID")
            @PathVariable Long patientId) {
        
        log.info("API call: GET /billings/patient/{}/pending-amount", patientId);
        BigDecimal amount = billingService.getTotalPendingAmount(patientId);
        
        return ResponseEntity.ok(ApiResponse.success(amount, "Pending amount retrieved"));
    }
    
    /**
     * Get total paid amount for patient
     */
    @GetMapping("/patient/{patientId}/paid-amount")
    @Operation(summary = "Get paid amount", description = "Get total paid amount for a patient")
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalPaidAmount(
            @Parameter(description = "Patient ID")
            @PathVariable Long patientId) {
        
        log.info("API call: GET /billings/patient/{}/paid-amount", patientId);
        BigDecimal amount = billingService.getTotalPaidAmount(patientId);
        
        return ResponseEntity.ok(ApiResponse.success(amount, "Paid amount retrieved"));
    }
    
    /**
     * Mark billing as paid
     */
    @PostMapping("/{id}/mark-paid")
    @Operation(summary = "Mark billing as paid", description = "Mark a billing as paid")
    public ResponseEntity<ApiResponse<BillingDto>> markAsPaid(
            @Parameter(description = "Billing ID")
            @PathVariable Long id,
            @Parameter(description = "Payment method")
            @RequestParam String paymentMethod) {
        
        log.info("API call: POST /billings/{}/mark-paid", id);
        BillingDto billing = billingService.markAsPaid(id, paymentMethod);
        
        return ResponseEntity.ok(ApiResponse.success(billing, "Billing marked as paid successfully"));
    }
    
    /**
     * Get pending billings with pagination
     */
    @GetMapping("/pending/list")
    @Operation(summary = "Get pending billings", description = "Retrieve all pending billings with pagination")
    public ResponseEntity<ApiResponse<PagedResponse<BillingDto>>> getPendingBillings(
            @Parameter(description = "Page number (0-indexed)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "20") int size) {
        
        log.info("API call: GET /billings/pending/list - page: {}, size: {}", page, size);
        PagedResponse<BillingDto> response = billingService.getPendingBillings(page, size);
        
        return ResponseEntity.ok(ApiResponse.success(response, "Pending billings retrieved successfully"));
    }
}
