package com.hms.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Appointment Data Transfer Object for request/response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentDto implements Serializable {
    
    @JsonProperty("id")
    private Long id;
    
    @NotNull(message = "Patient ID is required")
    @JsonProperty("patientId")
    private Long patientId;
    
    @NotNull(message = "Doctor ID is required")
    @JsonProperty("doctorId")
    private Long doctorId;
    
    @NotNull(message = "Appointment date is required")
    @JsonProperty("appointmentDate")
    private LocalDateTime appointmentDate;
    
    @JsonProperty("status")
    private String status;
    
    @Size(max = 255, message = "Reason must not exceed 255 characters")
    @JsonProperty("reason")
    private String reason;
    
    @JsonProperty("notes")
    private String notes;
    
    @JsonProperty("durationMinutes")
    private Integer durationMinutes;
    
    @JsonProperty("bookingDate")
    private LocalDateTime bookingDate;
    
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;
    
    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;
}
