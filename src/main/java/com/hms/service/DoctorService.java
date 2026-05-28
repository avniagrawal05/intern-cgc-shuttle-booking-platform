package com.hms.service;

import com.hms.dto.DoctorDto;
import com.hms.util.PagedResponse;

import java.util.List;

/**
 * Service interface for Doctor operations
 * Defines business logic methods for doctor management
 */
public interface DoctorService {
    
    /**
     * Get all active doctors with pagination
     */
    PagedResponse<DoctorDto> getAllDoctors(int page, int size);
    
    /**
     * Get doctor by ID
     */
    DoctorDto getDoctorById(Long id);
    
    /**
     * Create new doctor
     */
    DoctorDto createDoctor(DoctorDto doctorDto);
    
    /**
     * Update existing doctor
     */
    DoctorDto updateDoctor(Long id, DoctorDto doctorDto);
    
    /**
     * Delete doctor (soft delete)
     */
    void deleteDoctor(Long id);
    
    /**
     * Get doctors by specialization
     */
    List<DoctorDto> getDoctorsBySpecialization(String specialization);
    
    /**
     * Search doctors by name
     */
    List<DoctorDto> searchDoctorsByName(String name);
    
    /**
     * Get total active doctors count
     */
    long getTotalDoctorCount();
}
