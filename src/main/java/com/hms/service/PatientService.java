package com.hms.service;

import com.hms.dto.PatientDto;
import com.hms.util.PagedResponse;

import java.util.List;

/**
 * Service interface for Patient operations
 * Defines business logic methods for patient management
 */
public interface PatientService {
    
    /**
     * Get all active patients with pagination
     */
    PagedResponse<PatientDto> getAllPatients(int page, int size);
    
    /**
     * Get patient by ID
     */
    PatientDto getPatientById(Long id);
    
    /**
     * Create new patient
     */
    PatientDto createPatient(PatientDto patientDto);
    
    /**
     * Update existing patient
     */
    PatientDto updatePatient(Long id, PatientDto patientDto);
    
    /**
     * Delete patient (soft delete)
     */
    void deletePatient(Long id);
    
    /**
     * Search patients by name
     */
    List<PatientDto> searchPatientsByName(String name);
    
    /**
     * Get patients by disease
     */
    List<PatientDto> getPatientsByDisease(String disease);
    
    /**
     * Get total active patients count
     */
    long getTotalPatientCount();
}
