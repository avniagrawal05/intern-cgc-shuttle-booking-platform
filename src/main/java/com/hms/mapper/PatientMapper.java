package com.hms.mapper;

import com.hms.dto.PatientDto;
import com.hms.entity.Patient;
import lombok.experimental.UtilityClass;

/**
 * Mapper for Patient Entity and Patient DTO conversion
 */
@UtilityClass
public class PatientMapper {
    
    /**
     * Convert Patient entity to PatientDto
     */
    public static PatientDto toDto(Patient patient) {
        if (patient == null) {
            return null;
        }
        
        return PatientDto.builder()
                .id(patient.getId())
                .name(patient.getName())
                .age(patient.getAge())
                .disease(patient.getDisease())
                .email(patient.getEmail())
                .phoneNumber(patient.getPhoneNumber())
                .address(patient.getAddress())
                .gender(patient.getGender())
                .bloodGroup(patient.getBloodGroup())
                .status(patient.getStatus())
                .createdAt(patient.getCreatedAt())
                .updatedAt(patient.getUpdatedAt())
                .build();
    }
    
    /**
     * Convert PatientDto to Patient entity
     */
    public static Patient toEntity(PatientDto dto) {
        if (dto == null) {
            return null;
        }
        
        return Patient.builder()
                .id(dto.getId())
                .name(dto.getName())
                .age(dto.getAge())
                .disease(dto.getDisease())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .address(dto.getAddress())
                .gender(dto.getGender())
                .bloodGroup(dto.getBloodGroup())
                .status(dto.getStatus() != null ? dto.getStatus() : "ACTIVE")
                .build();
    }
    
    /**
     * Update patient entity with DTO values
     */
    public static void updateEntityFromDto(PatientDto dto, Patient patient) {
        if (dto == null || patient == null) {
            return;
        }
        
        if (dto.getName() != null) patient.setName(dto.getName());
        if (dto.getAge() != null) patient.setAge(dto.getAge());
        if (dto.getDisease() != null) patient.setDisease(dto.getDisease());
        if (dto.getEmail() != null) patient.setEmail(dto.getEmail());
        if (dto.getPhoneNumber() != null) patient.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getAddress() != null) patient.setAddress(dto.getAddress());
        if (dto.getGender() != null) patient.setGender(dto.getGender());
        if (dto.getBloodGroup() != null) patient.setBloodGroup(dto.getBloodGroup());
        if (dto.getStatus() != null) patient.setStatus(dto.getStatus());
    }
}
