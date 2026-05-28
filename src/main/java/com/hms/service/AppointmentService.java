package com.hms.service;

import com.hms.dto.AppointmentDto;
import com.hms.util.PagedResponse;

import java.util.List;

/**
 * Service interface for Appointment operations
 * Defines business logic methods for appointment management
 */
public interface AppointmentService {
    
    /**
     * Get all appointments with pagination
     */
    PagedResponse<AppointmentDto> getAllAppointments(int page, int size);
    
    /**
     * Get appointment by ID
     */
    AppointmentDto getAppointmentById(Long id);
    
    /**
     * Create new appointment
     */
    AppointmentDto createAppointment(AppointmentDto appointmentDto);
    
    /**
     * Update existing appointment
     */
    AppointmentDto updateAppointment(Long id, AppointmentDto appointmentDto);
    
    /**
     * Cancel appointment
     */
    void cancelAppointment(Long id);
    
    /**
     * Get patient's appointments
     */
    List<AppointmentDto> getPatientAppointments(Long patientId);
    
    /**
     * Get doctor's appointments
     */
    List<AppointmentDto> getDoctorAppointments(Long doctorId);
    
    /**
     * Get doctor's appointments for today
     */
    List<AppointmentDto> getDoctorAppointmentsForToday(Long doctorId);
    
    /**
     * Check if doctor has available slots at given time
     */
    boolean isSlotAvailable(Long doctorId, String appointmentDateTime);
}
