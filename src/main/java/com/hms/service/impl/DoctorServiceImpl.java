package com.hms.service.impl;

import com.hms.dto.DoctorDto;
import com.hms.entity.Doctor;
import com.hms.exception.DuplicateResourceException;
import com.hms.exception.InvalidInputException;
import com.hms.exception.ResourceNotFoundException;
import com.hms.mapper.DoctorMapper;
import com.hms.repository.DoctorRepository;
import com.hms.service.DoctorService;
import com.hms.util.PagedResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of DoctorService
 * Contains business logic for doctor management
 */
@Slf4j
@Service
@Transactional
public class DoctorServiceImpl implements DoctorService {
    
    private final DoctorRepository doctorRepository;
    
    public DoctorServiceImpl(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }
    
    @Override
    @Transactional(readOnly = true)
    public PagedResponse<DoctorDto> getAllDoctors(int page, int size) {
        log.info("Fetching all doctors - page: {}, size: {}", page, size);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Doctor> doctorsPage = doctorRepository.findByIsDeletedFalseOrderByCreatedAtDesc(pageable);
        
        List<DoctorDto> doctorDtos = doctorsPage.getContent()
                .stream()
                .map(DoctorMapper::toDto)
                .collect(Collectors.toList());
        
        return PagedResponse.of(doctorDtos, page, size, doctorsPage.getTotalElements());
    }
    
    @Override
    @Transactional(readOnly = true)
    public DoctorDto getDoctorById(Long id) {
        log.info("Fetching doctor with id: {}", id);
        
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Doctor not found with id: {}", id);
                    return new ResourceNotFoundException("Doctor", id);
                });
        
        if (doctor.getIsDeleted()) {
            throw new ResourceNotFoundException("Doctor", id);
        }
        
        return DoctorMapper.toDto(doctor);
    }
    
    @Override
    public DoctorDto createDoctor(DoctorDto doctorDto) {
        log.info("Creating new doctor with name: {}", doctorDto.getName());
        
        // Validate input
        if (doctorDto.getName() == null || doctorDto.getName().trim().isEmpty()) {
            throw new InvalidInputException("name", "Doctor name cannot be empty");
        }
        
        if (doctorDto.getSpecialization() == null || doctorDto.getSpecialization().trim().isEmpty()) {
            throw new InvalidInputException("specialization", "Specialization cannot be empty");
        }
        
        // Check for duplicate email
        if (doctorDto.getEmail() != null && !doctorDto.getEmail().isEmpty()) {
            if (doctorRepository.findByEmail(doctorDto.getEmail()).isPresent()) {
                throw new DuplicateResourceException("Doctor", "email", doctorDto.getEmail());
            }
        }
        
        Doctor doctor = DoctorMapper.toEntity(doctorDto);
        doctor.setIsDeleted(false);
        Doctor savedDoctor = doctorRepository.save(doctor);
        
        log.info("Doctor created successfully with id: {}", savedDoctor.getId());
        return DoctorMapper.toDto(savedDoctor);
    }
    
    @Override
    public DoctorDto updateDoctor(Long id, DoctorDto doctorDto) {
        log.info("Updating doctor with id: {}", id);
        
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Doctor not found with id: {}", id);
                    return new ResourceNotFoundException("Doctor", id);
                });
        
        if (doctor.getIsDeleted()) {
            throw new ResourceNotFoundException("Doctor", id);
        }
        
        // Check for duplicate email
        if (doctorDto.getEmail() != null && !doctorDto.getEmail().isEmpty() && !doctorDto.getEmail().equals(doctor.getEmail())) {
            if (doctorRepository.findByEmail(doctorDto.getEmail()).isPresent()) {
                throw new DuplicateResourceException("Doctor", "email", doctorDto.getEmail());
            }
        }
        
        DoctorMapper.updateEntityFromDto(doctorDto, doctor);
        Doctor updatedDoctor = doctorRepository.save(doctor);
        
        log.info("Doctor updated successfully with id: {}", id);
        return DoctorMapper.toDto(updatedDoctor);
    }
    
    @Override
    public void deleteDoctor(Long id) {
        log.info("Deleting doctor with id: {}", id);
        
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Doctor not found with id: {}", id);
                    return new ResourceNotFoundException("Doctor", id);
                });
        
        doctor.setIsDeleted(true);
        doctorRepository.save(doctor);
        
        log.info("Doctor deleted successfully with id: {}", id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DoctorDto> getDoctorsBySpecialization(String specialization) {
        log.info("Fetching doctors with specialization: {}", specialization);
        
        if (specialization == null || specialization.trim().isEmpty()) {
            throw new InvalidInputException("specialization", "Specialization cannot be empty");
        }
        
        return doctorRepository.findBySpecializationAndIsDeletedFalse(specialization)
                .stream()
                .map(DoctorMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DoctorDto> searchDoctorsByName(String name) {
        log.info("Searching doctors with name: {}", name);
        
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("name", "Search name cannot be empty");
        }
        
        return doctorRepository.searchByName(name)
                .stream()
                .map(DoctorMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getTotalDoctorCount() {
        log.info("Fetching total doctor count");
        return doctorRepository.countByIsDeletedFalse();
    }
}
