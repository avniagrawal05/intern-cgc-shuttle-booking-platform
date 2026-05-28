package com.hms.service.impl;

import com.hms.dto.BillingDto;
import com.hms.entity.Billing;
import com.hms.exception.BusinessException;
import com.hms.exception.InvalidInputException;
import com.hms.exception.ResourceNotFoundException;
import com.hms.mapper.BillingMapper;
import com.hms.repository.BillingRepository;
import com.hms.repository.PatientRepository;
import com.hms.service.BillingService;
import com.hms.util.PagedResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of BillingService
 * Contains business logic for billing management
 */
@Slf4j
@Service
@Transactional
public class BillingServiceImpl implements BillingService {
    
    private final BillingRepository billingRepository;
    private final PatientRepository patientRepository;
    
    public BillingServiceImpl(BillingRepository billingRepository,
                            PatientRepository patientRepository) {
        this.billingRepository = billingRepository;
        this.patientRepository = patientRepository;
    }
    
    @Override
    @Transactional(readOnly = true)
    public PagedResponse<BillingDto> getAllBillings(int page, int size) {
        log.info("Fetching all billings - page: {}, size: {}", page, size);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Billing> billingsPage = billingRepository.findAll(pageable);
        
        List<BillingDto> billingDtos = billingsPage.getContent()
                .stream()
                .filter(b -> !b.getIsDeleted())
                .map(BillingMapper::toDto)
                .collect(Collectors.toList());
        
        return PagedResponse.of(billingDtos, page, size, billingsPage.getTotalElements());
    }
    
    @Override
    @Transactional(readOnly = true)
    public BillingDto getBillingById(Long id) {
        log.info("Fetching billing with id: {}", id);
        
        Billing billing = billingRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Billing not found with id: {}", id);
                    return new ResourceNotFoundException("Billing", id);
                });
        
        if (billing.getIsDeleted()) {
            throw new ResourceNotFoundException("Billing", id);
        }
        
        return BillingMapper.toDto(billing);
    }
    
    @Override
    public BillingDto createBilling(BillingDto billingDto) {
        log.info("Creating new billing for patient: {}", billingDto.getPatientId());
        
        // Validate patient exists
        if (!patientRepository.existsById(billingDto.getPatientId())) {
            throw new ResourceNotFoundException("Patient", billingDto.getPatientId());
        }
        
        // Validate amount
        if (billingDto.getAmount() == null || billingDto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidInputException("amount", "Amount must be greater than 0");
        }
        
        Billing billing = BillingMapper.toEntity(billingDto);
        billing.setIsDeleted(false);
        
        // Generate invoice number if not provided
        if (billing.getInvoiceNumber() == null) {
            billing.setInvoiceNumber(generateInvoiceNumber());
        }
        
        Billing savedBilling = billingRepository.save(billing);
        
        log.info("Billing created successfully with id: {}", savedBilling.getId());
        return BillingMapper.toDto(savedBilling);
    }
    
    @Override
    public BillingDto updateBilling(Long id, BillingDto billingDto) {
        log.info("Updating billing with id: {}", id);
        
        Billing billing = billingRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Billing not found with id: {}", id);
                    return new ResourceNotFoundException("Billing", id);
                });
        
        if (billing.getIsDeleted()) {
            throw new ResourceNotFoundException("Billing", id);
        }
        
        if ("PAID".equals(billing.getPaymentStatus())) {
            throw new BusinessException("BILLING_ALREADY_PAID", "Cannot update a paid billing");
        }
        
        BillingMapper.updateEntityFromDto(billingDto, billing);
        Billing updatedBilling = billingRepository.save(billing);
        
        log.info("Billing updated successfully with id: {}", id);
        return BillingMapper.toDto(updatedBilling);
    }
    
    @Override
    public void deleteBilling(Long id) {
        log.info("Deleting billing with id: {}", id);
        
        Billing billing = billingRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Billing not found with id: {}", id);
                    return new ResourceNotFoundException("Billing", id);
                });
        
        if ("PAID".equals(billing.getPaymentStatus())) {
            throw new BusinessException("CANNOT_DELETE_PAID_BILLING", "Cannot delete a paid billing");
        }
        
        billing.setIsDeleted(true);
        billingRepository.save(billing);
        
        log.info("Billing deleted successfully with id: {}", id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BillingDto> getPatientBillings(Long patientId) {
        log.info("Fetching billings for patient: {}", patientId);
        
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient", patientId);
        }
        
        return billingRepository.findByPatientIdAndIsDeletedFalseOrderByCreatedAtDesc(patientId)
                .stream()
                .map(BillingMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalPendingAmount(Long patientId) {
        log.info("Fetching total pending amount for patient: {}", patientId);
        
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient", patientId);
        }
        
        return billingRepository.getTotalPendingAmount(patientId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalPaidAmount(Long patientId) {
        log.info("Fetching total paid amount for patient: {}", patientId);
        
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient", patientId);
        }
        
        return billingRepository.getTotalPaidAmount(patientId);
    }
    
    @Override
    public BillingDto markAsPaid(Long id, String paymentMethod) {
        log.info("Marking billing as paid with id: {} and payment method: {}", id, paymentMethod);
        
        Billing billing = billingRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Billing not found with id: {}", id);
                    return new ResourceNotFoundException("Billing", id);
                });
        
        if ("PAID".equals(billing.getPaymentStatus())) {
            throw new BusinessException("BILLING_ALREADY_PAID", "Billing is already marked as paid");
        }
        
        billing.setPaymentStatus("PAID");
        billing.setPaymentMethod(paymentMethod);
        billing.setPaymentDate(LocalDate.now());
        
        Billing updatedBilling = billingRepository.save(billing);
        
        log.info("Billing marked as paid successfully with id: {}", id);
        return BillingMapper.toDto(updatedBilling);
    }
    
    @Override
    @Transactional(readOnly = true)
    public PagedResponse<BillingDto> getPendingBillings(int page, int size) {
        log.info("Fetching pending billings - page: {}, size: {}", page, size);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Billing> billingsPage = billingRepository.findByPaymentStatusAndIsDeletedFalseOrderByCreatedAtDesc("PENDING", pageable);
        
        List<BillingDto> billingDtos = billingsPage.getContent()
                .stream()
                .map(BillingMapper::toDto)
                .collect(Collectors.toList());
        
        return PagedResponse.of(billingDtos, page, size, billingsPage.getTotalElements());
    }
    
    @Override
    public String generateInvoiceNumber() {
        return "INV-" + System.currentTimeMillis();
    }
}
