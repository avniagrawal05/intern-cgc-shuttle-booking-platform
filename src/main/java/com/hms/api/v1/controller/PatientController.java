package com.hms.api.v1.controller;

import com.hms.constants.AppConstants;
import com.hms.constants.HttpResponseConstants;
import com.hms.dto.PatientDto;
import com.hms.service.PatientService;
import com.hms.util.ApiResponse;
import com.hms.util.PagedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Patient Management
 * Handles all patient-related API endpoints
 */
@Slf4j
@RestController
@RequestMapping(AppConstants.API_V1 + "/patients")
@Tag(name = "Patient Management", description = "APIs for managing patients")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PatientController {
    
    private final PatientService patientService;
    
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }
    
    /**
     * Get all patients with pagination
     */
    @GetMapping
    @Operation(summary = "Get all patients", description = "Retrieve all active patients with pagination")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Patients retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponse<PagedResponse<PatientDto>>> getAllPatients(
            @Parameter(description = "Page number (0-indexed)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "20") int size) {
        
        log.info("API call: GET /patients - page: {}, size: {}", page, size);
        PagedResponse<PatientDto> response = patientService.getAllPatients(page, size);
        
        return ResponseEntity.ok(ApiResponse.success(response, "Patients retrieved successfully"));
    }
    
    /**
     * Get patient by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get patient by ID", description = "Retrieve a specific patient by their ID")
    public ResponseEntity<ApiResponse<PatientDto>> getPatientById(
            @Parameter(description = "Patient ID")
            @PathVariable Long id) {
        
        log.info("API call: GET /patients/{}", id);
        PatientDto patient = patientService.getPatientById(id);
        
        return ResponseEntity.ok(ApiResponse.success(patient, "Patient retrieved successfully"));
    }
    
    /**
     * Create new patient
     */
    @PostMapping
    @Operation(summary = "Create new patient", description = "Create a new patient record")
    public ResponseEntity<ApiResponse<PatientDto>> createPatient(
            @Valid @RequestBody PatientDto patientDto) {
        
        log.info("API call: POST /patients with name: {}", patientDto.getName());
        PatientDto createdPatient = patientService.createPatient(patientDto);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(createdPatient, "Patient created successfully"));
    }
    
    /**
     * Update patient
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update patient", description = "Update an existing patient record")
    public ResponseEntity<ApiResponse<PatientDto>> updatePatient(
            @Parameter(description = "Patient ID")
            @PathVariable Long id,
            @Valid @RequestBody PatientDto patientDto) {
        
        log.info("API call: PUT /patients/{}", id);
        PatientDto updatedPatient = patientService.updatePatient(id, patientDto);
        
        return ResponseEntity.ok(ApiResponse.success(updatedPatient, "Patient updated successfully"));
    }
    
    /**
     * Delete patient
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete patient", description = "Delete a patient record (soft delete)")
    public ResponseEntity<ApiResponse<?>> deletePatient(
            @Parameter(description = "Patient ID")
            @PathVariable Long id) {
        
        log.info("API call: DELETE /patients/{}", id);
        patientService.deletePatient(id);
        
        return ResponseEntity.ok(ApiResponse.success(null, "Patient deleted successfully"));
    }
    
    /**
     * Search patients by name
     */
    @GetMapping("/search/name")
    @Operation(summary = "Search patients by name", description = "Search patients using their name")
    public ResponseEntity<ApiResponse<List<PatientDto>>> searchPatientsByName(
            @Parameter(description = "Search name")
            @RequestParam String name) {
        
        log.info("API call: GET /patients/search/name?name={}", name);
        List<PatientDto> patients = patientService.searchPatientsByName(name);
        
        return ResponseEntity.ok(ApiResponse.success(patients, "Patients found: " + patients.size()));
    }
    
    /**
     * Get patients by disease
     */
    @GetMapping("/disease/{disease}")
    @Operation(summary = "Get patients by disease", description = "Retrieve patients with a specific disease")
    public ResponseEntity<ApiResponse<List<PatientDto>>> getPatientsByDisease(
            @Parameter(description = "Disease name")
            @PathVariable String disease) {
        
        log.info("API call: GET /patients/disease/{}", disease);
        List<PatientDto> patients = patientService.getPatientsByDisease(disease);
        
        return ResponseEntity.ok(ApiResponse.success(patients, "Patients found: " + patients.size()));
    }
    
    /**
     * Get total patient count
     */
    @GetMapping("/count/total")
    @Operation(summary = "Get total patient count", description = "Get the total number of active patients")
    public ResponseEntity<ApiResponse<Long>> getTotalPatientCount() {
        
        log.info("API call: GET /patients/count/total");
        long count = patientService.getTotalPatientCount();
        
        return ResponseEntity.ok(ApiResponse.success(count, "Total patients count retrieved"));
    }
}
