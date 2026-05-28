package com.hms.api.v1.controller;

import com.hms.constants.AppConstants;
import com.hms.dto.DoctorDto;
import com.hms.service.DoctorService;
import com.hms.util.ApiResponse;
import com.hms.util.PagedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Doctor Management
 * Handles all doctor-related API endpoints
 */
@Slf4j
@RestController
@RequestMapping(AppConstants.API_V1 + "/doctors")
@Tag(name = "Doctor Management", description = "APIs for managing doctors")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DoctorController {
    
    private final DoctorService doctorService;
    
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }
    
    /**
     * Get all doctors with pagination
     */
    @GetMapping
    @Operation(summary = "Get all doctors", description = "Retrieve all active doctors with pagination")
    public ResponseEntity<ApiResponse<PagedResponse<DoctorDto>>> getAllDoctors(
            @Parameter(description = "Page number (0-indexed)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "20") int size) {
        
        log.info("API call: GET /doctors - page: {}, size: {}", page, size);
        PagedResponse<DoctorDto> response = doctorService.getAllDoctors(page, size);
        
        return ResponseEntity.ok(ApiResponse.success(response, "Doctors retrieved successfully"));
    }
    
    /**
     * Get doctor by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get doctor by ID", description = "Retrieve a specific doctor by their ID")
    public ResponseEntity<ApiResponse<DoctorDto>> getDoctorById(
            @Parameter(description = "Doctor ID")
            @PathVariable Long id) {
        
        log.info("API call: GET /doctors/{}", id);
        DoctorDto doctor = doctorService.getDoctorById(id);
        
        return ResponseEntity.ok(ApiResponse.success(doctor, "Doctor retrieved successfully"));
    }
    
    /**
     * Create new doctor
     */
    @PostMapping
    @Operation(summary = "Create new doctor", description = "Create a new doctor record")
    public ResponseEntity<ApiResponse<DoctorDto>> createDoctor(
            @Valid @RequestBody DoctorDto doctorDto) {
        
        log.info("API call: POST /doctors with name: {}", doctorDto.getName());
        DoctorDto createdDoctor = doctorService.createDoctor(doctorDto);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(createdDoctor, "Doctor created successfully"));
    }
    
    /**
     * Update doctor
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update doctor", description = "Update an existing doctor record")
    public ResponseEntity<ApiResponse<DoctorDto>> updateDoctor(
            @Parameter(description = "Doctor ID")
            @PathVariable Long id,
            @Valid @RequestBody DoctorDto doctorDto) {
        
        log.info("API call: PUT /doctors/{}", id);
        DoctorDto updatedDoctor = doctorService.updateDoctor(id, doctorDto);
        
        return ResponseEntity.ok(ApiResponse.success(updatedDoctor, "Doctor updated successfully"));
    }
    
    /**
     * Delete doctor
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete doctor", description = "Delete a doctor record (soft delete)")
    public ResponseEntity<ApiResponse<?>> deleteDoctor(
            @Parameter(description = "Doctor ID")
            @PathVariable Long id) {
        
        log.info("API call: DELETE /doctors/{}", id);
        doctorService.deleteDoctor(id);
        
        return ResponseEntity.ok(ApiResponse.success(null, "Doctor deleted successfully"));
    }
    
    /**
     * Get doctors by specialization
     */
    @GetMapping("/specialization/{specialization}")
    @Operation(summary = "Get doctors by specialization", description = "Retrieve doctors with a specific specialization")
    public ResponseEntity<ApiResponse<List<DoctorDto>>> getDoctorsBySpecialization(
            @Parameter(description = "Specialization")
            @PathVariable String specialization) {
        
        log.info("API call: GET /doctors/specialization/{}", specialization);
        List<DoctorDto> doctors = doctorService.getDoctorsBySpecialization(specialization);
        
        return ResponseEntity.ok(ApiResponse.success(doctors, "Doctors found: " + doctors.size()));
    }
    
    /**
     * Search doctors by name
     */
    @GetMapping("/search/name")
    @Operation(summary = "Search doctors by name", description = "Search doctors using their name")
    public ResponseEntity<ApiResponse<List<DoctorDto>>> searchDoctorsByName(
            @Parameter(description = "Search name")
            @RequestParam String name) {
        
        log.info("API call: GET /doctors/search/name?name={}", name);
        List<DoctorDto> doctors = doctorService.searchDoctorsByName(name);
        
        return ResponseEntity.ok(ApiResponse.success(doctors, "Doctors found: " + doctors.size()));
    }
    
    /**
     * Get total doctor count
     */
    @GetMapping("/count/total")
    @Operation(summary = "Get total doctor count", description = "Get the total number of active doctors")
    public ResponseEntity<ApiResponse<Long>> getTotalDoctorCount() {
        
        log.info("API call: GET /doctors/count/total");
        long count = doctorService.getTotalDoctorCount();
        
        return ResponseEntity.ok(ApiResponse.success(count, "Total doctors count retrieved"));
    }
}
