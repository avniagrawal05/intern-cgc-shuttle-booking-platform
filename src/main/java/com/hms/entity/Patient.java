package com.hms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Patient Entity representing hospital patients
 */
@Entity
@Table(name = "patients", 
       indexes = {
           @Index(name = "idx_patient_email", columnList = "email"),
           @Index(name = "idx_patient_phone", columnList = "phone_number")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = "createdAt")
public class Patient extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Patient name cannot be blank")
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Min(value = 0, message = "Age must be greater than 0")
    @Column(name = "age", nullable = false)
    private Integer age;
    
    @NotBlank(message = "Disease cannot be blank")
    @Column(name = "disease", nullable = false, length = 255)
    private String disease;
    
    @Email(message = "Email should be valid")
    @Column(name = "email", unique = true, length = 100)
    private String email;
    
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;
    
    @Column(name = "address", length = 255)
    private String address;
    
    @Column(name = "gender", length = 10)
    private String gender;
    
    @Column(name = "blood_group", length = 10)
    private String bloodGroup;
    
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private String status = "ACTIVE";
}
