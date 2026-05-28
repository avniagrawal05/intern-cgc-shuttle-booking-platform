package com.hms.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Billing Data Transfer Object for request/response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillingDto implements Serializable {
    
    @JsonProperty("id")
    private Long id;
    
    @NotNull(message = "Patient ID is required")
    @JsonProperty("patientId")
    private Long patientId;
    
    @JsonProperty("appointmentId")
    private Long appointmentId;
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @JsonProperty("amount")
    private BigDecimal amount;
    
    @Size(max = 255, message = "Description must not exceed 255 characters")
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("paymentStatus")
    private String paymentStatus;
    
    @JsonProperty("paymentMethod")
    private String paymentMethod;
    
    @JsonProperty("paymentDate")
    private LocalDate paymentDate;
    
    @JsonProperty("invoiceNumber")
    private String invoiceNumber;
    
    @JsonProperty("remarks")
    private String remarks;
    
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;
    
    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;
}
