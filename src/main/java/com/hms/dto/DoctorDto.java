package com.hms.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Doctor Data Transfer Object for request/response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorDto implements Serializable {
    
    @JsonProperty("id")
    private Long id;
    
    @NotBlank(message = "Doctor name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @JsonProperty("name")
    private String name;
    
    @NotBlank(message = "Specialization is required")
    @Size(min = 2, max = 100, message = "Specialization must be between 2 and 100 characters")
    @JsonProperty("specialization")
    private String specialization;
    
    @Email(message = "Email should be valid")
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("phoneNumber")
    private String phoneNumber;
    
    @Min(value = 0, message = "Experience years must be non-negative")
    @JsonProperty("experienceYears")
    private Integer experienceYears;
    
    @JsonProperty("qualification")
    private String qualification;
    
    @JsonProperty("clinicAddress")
    private String clinicAddress;
    
    @JsonProperty("status")
    private String status;
    
    @DecimalMin(value = "0.0", inclusive = false, message = "Consultation fee must be greater than 0")
    @JsonProperty("consultationFee")
    private Double consultationFee;
    
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;
    
    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;
}
