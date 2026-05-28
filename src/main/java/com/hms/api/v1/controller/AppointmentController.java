package com.hms.api.v1.controller;

import com.hms.constants.AppConstants;
import com.hms.dto.AppointmentDto;
import com.hms.service.AppointmentService;
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
 * REST Controller for Appointment Management
 * Handles all appointment-related API endpoints
 */
@Slf4j
@RestController
@RequestMapping(AppConstants.API_V1 + "/appointments")
@Tag(name = "Appointment Management", description = "APIs for managing appointments")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AppointmentController {
    
    private final AppointmentService appointmentService;
    
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }
    
    /**
     * Get all appointments with pagination
     */
    @GetMapping
    @Operation(summary = "Get all appointments", description = "Retrieve all appointments with pagination")
    public ResponseEntity<ApiResponse<PagedResponse<AppointmentDto>>> getAllAppointments(
            @Parameter(description = "Page number (0-indexed)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "20") int size) {
        
        log.info("API call: GET /appointments - page: {}, size: {}", page, size);
        PagedResponse<AppointmentDto> response = appointmentService.getAllAppointments(page, size);
        
        return ResponseEntity.ok(ApiResponse.success(response, "Appointments retrieved successfully"));
    }
    
    /**
     * Get appointment by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get appointment by ID", description = "Retrieve a specific appointment by its ID")
    public ResponseEntity<ApiResponse<AppointmentDto>> getAppointmentById(
            @Parameter(description = "Appointment ID")
            @PathVariable Long id) {
        
        log.info("API call: GET /appointments/{}", id);
        AppointmentDto appointment = appointmentService.getAppointmentById(id);
        
        return ResponseEntity.ok(ApiResponse.success(appointment, "Appointment retrieved successfully"));
    }
    
    /**
     * Create new appointment
     */
    @PostMapping
    @Operation(summary = "Create new appointment", description = "Create a new appointment")
    public ResponseEntity<ApiResponse<AppointmentDto>> createAppointment(
            @Valid @RequestBody AppointmentDto appointmentDto) {
        
        log.info("API call: POST /appointments for patient: {}", appointmentDto.getPatientId());
        AppointmentDto createdAppointment = appointmentService.createAppointment(appointmentDto);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(createdAppointment, "Appointment created successfully"));
    }
    
    /**
     * Update appointment
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update appointment", description = "Update an existing appointment")
    public ResponseEntity<ApiResponse<AppointmentDto>> updateAppointment(
            @Parameter(description = "Appointment ID")
            @PathVariable Long id,
            @Valid @RequestBody AppointmentDto appointmentDto) {
        
        log.info("API call: PUT /appointments/{}", id);
        AppointmentDto updatedAppointment = appointmentService.updateAppointment(id, appointmentDto);
        
        return ResponseEntity.ok(ApiResponse.success(updatedAppointment, "Appointment updated successfully"));
    }
    
    /**
     * Cancel appointment
     */
    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel appointment", description = "Cancel an existing appointment")
    public ResponseEntity<ApiResponse<?>> cancelAppointment(
            @Parameter(description = "Appointment ID")
            @PathVariable Long id) {
        
        log.info("API call: POST /appointments/{}/cancel", id);
        appointmentService.cancelAppointment(id);
        
        return ResponseEntity.ok(ApiResponse.success(null, "Appointment cancelled successfully"));
    }
    
    /**
     * Get patient's appointments
     */
    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get patient appointments", description = "Retrieve all appointments for a patient")
    public ResponseEntity<ApiResponse<List<AppointmentDto>>> getPatientAppointments(
            @Parameter(description = "Patient ID")
            @PathVariable Long patientId) {
        
        log.info("API call: GET /appointments/patient/{}", patientId);
        List<AppointmentDto> appointments = appointmentService.getPatientAppointments(patientId);
        
        return ResponseEntity.ok(ApiResponse.success(appointments, "Appointments found: " + appointments.size()));
    }
    
    /**
     * Get doctor's appointments
     */
    @GetMapping("/doctor/{doctorId}")
    @Operation(summary = "Get doctor appointments", description = "Retrieve all appointments for a doctor")
    public ResponseEntity<ApiResponse<List<AppointmentDto>>> getDoctorAppointments(
            @Parameter(description = "Doctor ID")
            @PathVariable Long doctorId) {
        
        log.info("API call: GET /appointments/doctor/{}", doctorId);
        List<AppointmentDto> appointments = appointmentService.getDoctorAppointments(doctorId);
        
        return ResponseEntity.ok(ApiResponse.success(appointments, "Appointments found: " + appointments.size()));
    }
    
    /**
     * Get doctor's appointments for today
     */
    @GetMapping("/doctor/{doctorId}/today")
    @Operation(summary = "Get doctor's today appointments", description = "Retrieve all appointments for a doctor today")
    public ResponseEntity<ApiResponse<List<AppointmentDto>>> getDoctorAppointmentsForToday(
            @Parameter(description = "Doctor ID")
            @PathVariable Long doctorId) {
        
        log.info("API call: GET /appointments/doctor/{}/today", doctorId);
        List<AppointmentDto> appointments = appointmentService.getDoctorAppointmentsForToday(doctorId);
        
        return ResponseEntity.ok(ApiResponse.success(appointments, "Today's appointments found: " + appointments.size()));
    }
    
    /**
     * Check slot availability
     */
    @GetMapping("/check-availability")
    @Operation(summary = "Check slot availability", description = "Check if a doctor has available slot at specified time")
    public ResponseEntity<ApiResponse<Boolean>> checkSlotAvailability(
            @Parameter(description = "Doctor ID")
            @RequestParam Long doctorId,
            @Parameter(description = "Appointment DateTime (yyyy-MM-dd HH:mm:ss)")
            @RequestParam String appointmentDateTime) {
        
        log.info("API call: GET /appointments/check-availability for doctor: {} at: {}", doctorId, appointmentDateTime);
        boolean available = appointmentService.isSlotAvailable(doctorId, appointmentDateTime);
        
        return ResponseEntity.ok(ApiResponse.success(available, available ? "Slot is available" : "Slot is not available"));
    }
}
