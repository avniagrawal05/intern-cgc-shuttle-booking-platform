package com.hms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Appointment Entity representing doctor-patient appointments
 */
@Entity
@Table(name = "appointments",
       indexes = {
           @Index(name = "idx_appointment_patient", columnList = "patient_id"),
           @Index(name = "idx_appointment_doctor", columnList = "doctor_id"),
           @Index(name = "idx_appointment_date", columnList = "appointment_date"),
           @Index(name = "idx_appointment_status", columnList = "status")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = "createdAt")
public class Appointment extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Patient ID cannot be null")
    @Column(name = "patient_id", nullable = false)
    private Long patientId;
    
    @NotNull(message = "Doctor ID cannot be null")
    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;
    
    @NotNull(message = "Appointment date cannot be null")
    @Column(name = "appointment_date", nullable = false)
    private LocalDateTime appointmentDate;
    
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private String status = "SCHEDULED";
    
    @Column(name = "reason", length = 255)
    private String reason;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "duration_minutes")
    private Integer durationMinutes;
    
    @CreationTimestamp
    @Column(name = "booking_date", nullable = false, updatable = false)
    private LocalDateTime bookingDate;
}
