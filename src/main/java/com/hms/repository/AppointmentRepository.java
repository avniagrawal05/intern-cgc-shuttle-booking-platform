package com.hms.repository;

import com.hms.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Appointment entity database operations
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    /**
     * Find appointments by patient ID
     */
    List<Appointment> findByPatientIdAndIsDeletedFalseOrderByAppointmentDateDesc(Long patientId);
    
    /**
     * Find appointments by doctor ID
     */
    List<Appointment> findByDoctorIdAndIsDeletedFalseOrderByAppointmentDateDesc(Long doctorId);
    
    /**
     * Find appointments by status
     */
    List<Appointment> findByStatusAndIsDeletedFalse(String status);
    
    /**
     * Find appointments within date range
     */
    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate BETWEEN :startDate AND :endDate AND a.isDeleted = false ORDER BY a.appointmentDate")
    List<Appointment> findAppointmentsInDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
    
    /**
     * Find appointments for a doctor on a specific date
     */
    @Query("SELECT a FROM Appointment a WHERE a.doctorId = :doctorId AND DATE(a.appointmentDate) = CURRENT_DATE AND a.isDeleted = false")
    List<Appointment> findDoctorAppointmentsForToday(@Param("doctorId") Long doctorId);
    
    /**
     * Check if slot is available for doctor
     */
    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.doctorId = :doctorId AND a.appointmentDate = :appointmentDate AND a.isDeleted = false AND a.status != 'CANCELLED'")
    long countDoctorAppointmentsAtTime(@Param("doctorId") Long doctorId, @Param("appointmentDate") LocalDateTime appointmentDate);
    
    /**
     * Find active appointments with pagination
     */
    Page<Appointment> findByIsDeletedFalseOrderByAppointmentDateDesc(Pageable pageable);
}
