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
 * Patient Data Transfer Object for request/response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientDto implements Serializable {
    
    @JsonProperty("id")
    private Long id;
    
    @NotBlank(message = "Patient name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @JsonProperty("name")
    private String name;
    
    @NotNull(message = "Age is required")
    @Min(value = 1, message = "Age must be greater than 0")
    @Max(value = 150, message = "Age must be less than 150")
    @JsonProperty("age")
    private Integer age;
    
    @NotBlank(message = "Disease is required")
    @Size(min = 2, max = 255, message = "Disease must be between 2 and 255 characters")
    @JsonProperty("disease")
    private String disease;
    
    @Email(message = "Email should be valid")
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("phoneNumber")
    private String phoneNumber;
    
    @JsonProperty("address")
    private String address;
    
    @JsonProperty("gender")
    private String gender;
    
    @JsonProperty("bloodGroup")
    private String bloodGroup;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;
    
    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;
}
