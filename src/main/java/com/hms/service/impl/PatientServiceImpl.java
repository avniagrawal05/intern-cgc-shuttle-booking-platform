package com.hms.service.impl;

import com.hms.constants.AppConstants;
import com.hms.dto.PatientDto;
import com.hms.entity.Patient;
import com.hms.exception.DuplicateResourceException;
import com.hms.exception.InvalidInputException;
import com.hms.exception.ResourceNotFoundException;
import com.hms.mapper.PatientMapper;
import com.hms.repository.PatientRepository;
import com.hms.service.PatientService;
import com.hms.util.PagedResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of PatientService
 * Contains business logic for patient management with Redis caching
 */
@Slf4j
@Service
@Transactional
@CacheConfig(cacheNames = AppConstants.CACHE_PATIENTS)
public class PatientServiceImpl implements PatientService {
    
    private final PatientRepository patientRepository;
    
    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }
    
    @Override
    @Transactional(readOnly = true)
    public PagedResponse<PatientDto> getAllPatients(int page, int size) {
        log.info("Fetching all patients - page: {}, size: {}", page, size);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Patient> patientsPage = patientRepository.findByIsDeletedFalseOrderByCreatedAtDesc(pageable);
        
        List<PatientDto> patientDtos = patientsPage.getContent()
                .stream()
                .map(PatientMapper::toDto)
                .collect(Collectors.toList());
        
        return PagedResponse.of(patientDtos, page, size, patientsPage.getTotalElements());
    }
    
    @Override
    @Transactional(readOnly = true)
    public PatientDto getPatientById(Long id) {
        log.info("Fetching patient with id: {}", id);
        
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Patient not found with id: {}", id);
                    return new ResourceNotFoundException("Patient", id);
                });
        
        if (patient.getIsDeleted()) {
            throw new ResourceNotFoundException("Patient", id);
        }
        
        return PatientMapper.toDto(patient);
    }
    
    @Override
    public PatientDto createPatient(PatientDto patientDto) {
        log.info("Creating new patient with name: {}", patientDto.getName());
        
        // Validate input
        if (patientDto.getName() == null || patientDto.getName().trim().isEmpty()) {
            throw new InvalidInputException("name", "Patient name cannot be empty");
        }
        
        // Check for duplicate email
        if (patientDto.getEmail() != null && !patientDto.getEmail().isEmpty()) {
            if (patientRepository.findByEmail(patientDto.getEmail()).isPresent()) {
                throw new DuplicateResourceException("Patient", "email", patientDto.getEmail());
            }
        }
        
        // Check for duplicate phone
        if (patientDto.getPhoneNumber() != null && !patientDto.getPhoneNumber().isEmpty()) {
            if (patientRepository.findByPhoneNumber(patientDto.getPhoneNumber()).isPresent()) {
                throw new DuplicateResourceException("Patient", "phoneNumber", patientDto.getPhoneNumber());
            }
        }
        
        Patient patient = PatientMapper.toEntity(patientDto);
        patient.setIsDeleted(false);
        Patient savedPatient = patientRepository.save(patient);
        
        log.info("Patient created successfully with id: {}", savedPatient.getId());
        return PatientMapper.toDto(savedPatient);
    }
    
    @Override
    public PatientDto updatePatient(Long id, PatientDto patientDto) {
        log.info("Updating patient with id: {}", id);
        
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Patient not found with id: {}", id);
                    return new ResourceNotFoundException("Patient", id);
                });
        
        if (patient.getIsDeleted()) {
            throw new ResourceNotFoundException("Patient", id);
        }
        
        // Check for duplicate email
        if (patientDto.getEmail() != null && !patientDto.getEmail().isEmpty() && !patientDto.getEmail().equals(patient.getEmail())) {
            if (patientRepository.findByEmail(patientDto.getEmail()).isPresent()) {
                throw new DuplicateResourceException("Patient", "email", patientDto.getEmail());
            }
        }
        
        PatientMapper.updateEntityFromDto(patientDto, patient);
        Patient updatedPatient = patientRepository.save(patient);
        
        log.info("Patient updated successfully with id: {}", id);
        return PatientMapper.toDto(updatedPatient);
    }
    
    @Override
    @CacheEvict(key = "#id")
    public void deletePatient(Long id) {
        log.info("Deleting patient with id: {}", id);
        
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Patient not found with id: {}", id);
                    return new ResourceNotFoundException("Patient", id);
                });
        
        patient.setIsDeleted(true);
        patientRepository.save(patient);
        
        log.info("Patient deleted successfully with id: {}", id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PatientDto> searchPatientsByName(String name) {
        log.info("Searching patients with name: {}", name);
        
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("name", "Search name cannot be empty");
        }
        
        return patientRepository.searchByName(name)
                .stream()
                .map(PatientMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PatientDto> getPatientsByDisease(String disease) {
        log.info("Fetching patients with disease: {}", disease);
        
        if (disease == null || disease.trim().isEmpty()) {
            throw new InvalidInputException("disease", "Disease cannot be empty");
        }
        
        return patientRepository.findByDiseaseAndIsDeletedFalse(disease)
                .stream()
                .map(PatientMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getTotalPatientCount() {
        log.info("Fetching total patient count");
        return patientRepository.countByIsDeletedFalse();
    }
}
yIsDeletedFalse();
    }
}
