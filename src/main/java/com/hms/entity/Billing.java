package com.hms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Billing Entity representing patient billing information
 */
@Entity
@Table(name = "billings",
       indexes = {
           @Index(name = "idx_billing_patient", columnList = "patient_id"),
           @Index(name = "idx_billing_appointment", columnList = "appointment_id"),
           @Index(name = "idx_billing_status", columnList = "payment_status")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = "createdAt")
public class Billing extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Patient ID cannot be null")
    @Column(name = "patient_id", nullable = false)
    private Long patientId;
    
    @Column(name = "appointment_id")
    private Long appointmentId;
    
    @NotNull(message = "Amount cannot be null")
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    
    @Column(name = "description", length = 255)
    private String description;
    
    @Column(name = "payment_status", nullable = false, length = 20)
    @Builder.Default
    private String paymentStatus = "PENDING";
    
    @Column(name = "payment_method", length = 50)
    private String paymentMethod;
    
    @Column(name = "payment_date")
    private LocalDate paymentDate;
    
    @Column(name = "invoice_number", unique = true, length = 50)
    private String invoiceNumber;
    
    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;
}
