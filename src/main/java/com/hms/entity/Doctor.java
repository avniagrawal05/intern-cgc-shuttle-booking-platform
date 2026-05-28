package com.hms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Doctor Entity representing hospital doctors
 */
@Entity
@Table(name = "doctors",
       indexes = {
           @Index(name = "idx_doctor_email", columnList = "email"),
           @Index(name = "idx_doctor_specialization", columnList = "specialization")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = "createdAt")
public class Doctor extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Doctor name cannot be blank")
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @NotBlank(message = "Specialization cannot be blank")
    @Column(name = "specialization", nullable = false, length = 100)
    private String specialization;
    
    @Email(message = "Email should be valid")
    @Column(name = "email", unique = true, length = 100)
    private String email;
    
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;
    
    @Column(name = "experience_years")
    private Integer experienceYears;
    
    @Column(name = "qualification", length = 100)
    private String qualification;
    
    @Column(name = "clinic_address", length = 255)
    private String clinicAddress;
    
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private String status = "ACTIVE";
    
    @Column(name = "consultation_fee")
    private Double consultationFee;
}
