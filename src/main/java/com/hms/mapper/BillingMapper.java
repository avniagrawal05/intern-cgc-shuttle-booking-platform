package com.hms.mapper;

import com.hms.dto.BillingDto;
import com.hms.entity.Billing;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

/**
 * Mapper for Billing Entity and Billing DTO conversion
 */
@UtilityClass
public class BillingMapper {
    
    /**
     * Convert Billing entity to BillingDto
     */
    public static BillingDto toDto(Billing billing) {
        if (billing == null) {
            return null;
        }
        
        return BillingDto.builder()
                .id(billing.getId())
                .patientId(billing.getPatientId())
                .appointmentId(billing.getAppointmentId())
                .amount(billing.getAmount())
                .description(billing.getDescription())
                .paymentStatus(billing.getPaymentStatus())
                .paymentMethod(billing.getPaymentMethod())
                .paymentDate(billing.getPaymentDate())
                .invoiceNumber(billing.getInvoiceNumber())
                .remarks(billing.getRemarks())
                .createdAt(billing.getCreatedAt())
                .updatedAt(billing.getUpdatedAt())
                .build();
    }
    
    /**
     * Convert BillingDto to Billing entity
     */
    public static Billing toEntity(BillingDto dto) {
        if (dto == null) {
            return null;
        }
        
        return Billing.builder()
                .id(dto.getId())
                .patientId(dto.getPatientId())
                .appointmentId(dto.getAppointmentId())
                .amount(dto.getAmount())
                .description(dto.getDescription())
                .paymentStatus(dto.getPaymentStatus() != null ? dto.getPaymentStatus() : "PENDING")
                .paymentMethod(dto.getPaymentMethod())
                .paymentDate(dto.getPaymentDate())
                .invoiceNumber(dto.getInvoiceNumber())
                .remarks(dto.getRemarks())
                .build();
    }
    
    /**
     * Update billing entity with DTO values
     */
    public static void updateEntityFromDto(BillingDto dto, Billing billing) {
        if (dto == null || billing == null) {
            return;
        }
        
        if (dto.getPatientId() != null) billing.setPatientId(dto.getPatientId());
        if (dto.getAppointmentId() != null) billing.setAppointmentId(dto.getAppointmentId());
        if (dto.getAmount() != null) billing.setAmount(dto.getAmount());
        if (dto.getDescription() != null) billing.setDescription(dto.getDescription());
        if (dto.getPaymentStatus() != null) billing.setPaymentStatus(dto.getPaymentStatus());
        if (dto.getPaymentMethod() != null) billing.setPaymentMethod(dto.getPaymentMethod());
        if (dto.getPaymentDate() != null) billing.setPaymentDate(dto.getPaymentDate());
        if (dto.getInvoiceNumber() != null) billing.setInvoiceNumber(dto.getInvoiceNumber());
        if (dto.getRemarks() != null) billing.setRemarks(dto.getRemarks());
        
        billing.setUpdatedAt(LocalDateTime.now());
    }
}
