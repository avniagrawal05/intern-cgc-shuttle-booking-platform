package com.hms.service.impl;

import com.hms.dto.AppointmentDto;
import com.hms.entity.Appointment;
import com.hms.exception.BusinessException;
import com.hms.exception.InvalidInputException;
import com.hms.exception.ResourceNotFoundException;
import com.hms.mapper.AppointmentMapper;
import com.hms.repository.AppointmentRepository;
import com.hms.repository.DoctorRepository;
import com.hms.repository.PatientRepository;
import com.hms.service.AppointmentService;
import com.hms.util.PagedResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of AppointmentService
 * Contains business logic for appointment management
 */
@Slf4j
@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {
    
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
                                PatientRepository patientRepository,
                                DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }
    
    @Override
    @Transactional(readOnly = true)
    public PagedResponse<AppointmentDto> getAllAppointments(int page, int size) {
        log.info("Fetching all appointments - page: {}, size: {}", page, size);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Appointment> appointmentsPage = appointmentRepository.findByIsDeletedFalseOrderByAppointmentDateDesc(pageable);
        
        List<AppointmentDto> appointmentDtos = appointmentsPage.getContent()
                .stream()
                .map(AppointmentMapper::toDto)
                .collect(Collectors.toList());
        
        return PagedResponse.of(appointmentDtos, page, size, appointmentsPage.getTotalElements());
    }
    
    @Override
    @Transactional(readOnly = true)
    public AppointmentDto getAppointmentById(Long id) {
        log.info("Fetching appointment with id: {}", id);
        
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Appointment not found with id: {}", id);
                    return new ResourceNotFoundException("Appointment", id);
                });
        
        if (appointment.getIsDeleted()) {
            throw new ResourceNotFoundException("Appointment", id);
        }
        
        return AppointmentMapper.toDto(appointment);
    }
    
    @Override
    public AppointmentDto createAppointment(AppointmentDto appointmentDto) {
        log.info("Creating new appointment for patient: {} with doctor: {}", 
                appointmentDto.getPatientId(), appointmentDto.getDoctorId());
        
        // Validate patient exists
        if (!patientRepository.existsById(appointmentDto.getPatientId())) {
            throw new ResourceNotFoundException("Patient", appointmentDto.getPatientId());
        }
        
        // Validate doctor exists
        if (!doctorRepository.existsById(appointmentDto.getDoctorId())) {
            throw new ResourceNotFoundException("Doctor", appointmentDto.getDoctorId());
        }
        
        // Validate appointment date is in future
        if (appointmentDto.getAppointmentDate().isBefore(LocalDateTime.now())) {
            throw new InvalidInputException("appointmentDate", "Appointment date cannot be in the past");
        }
        
        // Check if slot is available
        if (!isSlotAvailable(appointmentDto.getDoctorId(), appointmentDto.getAppointmentDate().format(DATE_TIME_FORMATTER))) {
            throw new BusinessException("SLOT_NOT_AVAILABLE", 
                    "Doctor is not available at the requested time");
        }
        
        Appointment appointment = AppointmentMapper.toEntity(appointmentDto);
        appointment.setIsDeleted(false);
        Appointment savedAppointment = appointmentRepository.save(appointment);
        
        log.info("Appointment created successfully with id: {}", savedAppointment.getId());
        return AppointmentMapper.toDto(savedAppointment);
    }
    
    @Override
    public AppointmentDto updateAppointment(Long id, AppointmentDto appointmentDto) {
        log.info("Updating appointment with id: {}", id);
        
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Appointment not found with id: {}", id);
                    return new ResourceNotFoundException("Appointment", id);
                });
        
        if (appointment.getIsDeleted()) {
            throw new ResourceNotFoundException("Appointment", id);
        }
        
        AppointmentMapper.updateEntityFromDto(appointmentDto, appointment);
        Appointment updatedAppointment = appointmentRepository.save(appointment);
        
        log.info("Appointment updated successfully with id: {}", id);
        return AppointmentMapper.toDto(updatedAppointment);
    }
    
    @Override
    public void cancelAppointment(Long id) {
        log.info("Cancelling appointment with id: {}", id);
        
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Appointment not found with id: {}", id);
                    return new ResourceNotFoundException("Appointment", id);
                });
        
        if ("CANCELLED".equals(appointment.getStatus())) {
            throw new BusinessException("APPOINTMENT_ALREADY_CANCELLED", "Appointment is already cancelled");
        }
        
        appointment.setStatus("CANCELLED");
        appointmentRepository.save(appointment);
        
        log.info("Appointment cancelled successfully with id: {}", id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDto> getPatientAppointments(Long patientId) {
        log.info("Fetching appointments for patient: {}", patientId);
        
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient", patientId);
        }
        
        return appointmentRepository.findByPatientIdAndIsDeletedFalseOrderByAppointmentDateDesc(patientId)
                .stream()
                .map(AppointmentMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDto> getDoctorAppointments(Long doctorId) {
        log.info("Fetching appointments for doctor: {}", doctorId);
        
        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor", doctorId);
        }
        
        return appointmentRepository.findByDoctorIdAndIsDeletedFalseOrderByAppointmentDateDesc(doctorId)
                .stream()
                .map(AppointmentMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDto> getDoctorAppointmentsForToday(Long doctorId) {
        log.info("Fetching today's appointments for doctor: {}", doctorId);
        
        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor", doctorId);
        }
        
        return appointmentRepository.findDoctorAppointmentsForToday(doctorId)
                .stream()
                .map(AppointmentMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isSlotAvailable(Long doctorId, String appointmentDateTime) {
        log.debug("Checking slot availability for doctor: {} at: {}", doctorId, appointmentDateTime);
        
        LocalDateTime dateTime = LocalDateTime.parse(appointmentDateTime, DATE_TIME_FORMATTER);
        long count = appointmentRepository.countDoctorAppointmentsAtTime(doctorId, dateTime);
        return count == 0;
    }
}
