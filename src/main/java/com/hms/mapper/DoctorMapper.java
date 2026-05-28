package com.hms.mapper;

import com.hms.dto.DoctorDto;
import com.hms.entity.Doctor;
import lombok.experimental.UtilityClass;

/**
 * Mapper for Doctor Entity and Doctor DTO conversion
 */
@UtilityClass
public class DoctorMapper {
    
    /**
     * Convert Doctor entity to DoctorDto
     */
    public static DoctorDto toDto(Doctor doctor) {
        if (doctor == null) {
            return null;
        }
        
        return DoctorDto.builder()
                .id(doctor.getId())
                .name(doctor.getName())
                .specialization(doctor.getSpecialization())
                .email(doctor.getEmail())
                .phoneNumber(doctor.getPhoneNumber())
                .experienceYears(doctor.getExperienceYears())
                .qualification(doctor.getQualification())
                .clinicAddress(doctor.getClinicAddress())
                .status(doctor.getStatus())
                .consultationFee(doctor.getConsultationFee())
                .createdAt(doctor.getCreatedAt())
                .updatedAt(doctor.getUpdatedAt())
                .build();
    }
    
    /**
     * Convert DoctorDto to Doctor entity
     */
    public static Doctor toEntity(DoctorDto dto) {
        if (dto == null) {
            return null;
        }
        
        return Doctor.builder()
                .id(dto.getId())
                .name(dto.getName())
                .specialization(dto.getSpecialization())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .experienceYears(dto.getExperienceYears())
                .qualification(dto.getQualification())
                .clinicAddress(dto.getClinicAddress())
                .status(dto.getStatus() != null ? dto.getStatus() : "ACTIVE")
                .consultationFee(dto.getConsultationFee())
                .build();
    }
    
    /**
     * Update doctor entity with DTO values
     */
    public static void updateEntityFromDto(DoctorDto dto, Doctor doctor) {
        if (dto == null || doctor == null) {
            return;
        }
        
        if (dto.getName() != null) doctor.setName(dto.getName());
        if (dto.getSpecialization() != null) doctor.setSpecialization(dto.getSpecialization());
        if (dto.getEmail() != null) doctor.setEmail(dto.getEmail());
        if (dto.getPhoneNumber() != null) doctor.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getExperienceYears() != null) doctor.setExperienceYears(dto.getExperienceYears());
        if (dto.getQualification() != null) doctor.setQualification(dto.getQualification());
        if (dto.getClinicAddress() != null) doctor.setClinicAddress(dto.getClinicAddress());
        if (dto.getStatus() != null) doctor.setStatus(dto.getStatus());
        if (dto.getConsultationFee() != null) doctor.setConsultationFee(dto.getConsultationFee());
    }
}
