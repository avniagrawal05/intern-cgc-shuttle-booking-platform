package com.hms.mapper;

import com.hms.dto.AppointmentDto;
import com.hms.entity.Appointment;
import lombok.experimental.UtilityClass;

/**
 * Mapper for Appointment Entity and Appointment DTO conversion
 */
@UtilityClass
public class AppointmentMapper {
    
    /**
     * Convert Appointment entity to AppointmentDto
     */
    public static AppointmentDto toDto(Appointment appointment) {
        if (appointment == null) {
            return null;
        }
        
        return AppointmentDto.builder()
                .id(appointment.getId())
                .patientId(appointment.getPatientId())
                .doctorId(appointment.getDoctorId())
                .appointmentDate(appointment.getAppointmentDate())
                .status(appointment.getStatus())
                .reason(appointment.getReason())
                .notes(appointment.getNotes())
                .durationMinutes(appointment.getDurationMinutes())
                .bookingDate(appointment.getBookingDate())
                .createdAt(appointment.getCreatedAt())
                .updatedAt(appointment.getUpdatedAt())
                .build();
    }
    
    /**
     * Convert AppointmentDto to Appointment entity
     */
    public static Appointment toEntity(AppointmentDto dto) {
        if (dto == null) {
            return null;
        }
        
        return Appointment.builder()
                .id(dto.getId())
                .patientId(dto.getPatientId())
                .doctorId(dto.getDoctorId())
                .appointmentDate(dto.getAppointmentDate())
                .status(dto.getStatus() != null ? dto.getStatus() : "SCHEDULED")
                .reason(dto.getReason())
                .notes(dto.getNotes())
                .durationMinutes(dto.getDurationMinutes())
                .build();
    }
    
    /**
     * Update appointment entity with DTO values
     */
    public static void updateEntityFromDto(AppointmentDto dto, Appointment appointment) {
        if (dto == null || appointment == null) {
            return;
        }
        
        if (dto.getAppointmentDate() != null) appointment.setAppointmentDate(dto.getAppointmentDate());
        if (dto.getStatus() != null) appointment.setStatus(dto.getStatus());
        if (dto.getReason() != null) appointment.setReason(dto.getReason());
        if (dto.getNotes() != null) appointment.setNotes(dto.getNotes());
        if (dto.getDurationMinutes() != null) appointment.setDurationMinutes(dto.getDurationMinutes());
    }
}
