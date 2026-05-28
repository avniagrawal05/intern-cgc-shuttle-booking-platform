package com.hms.repository;

import com.hms.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Doctor entity database operations
 */
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    
    /**
     * Find doctor by email
     */
    Optional<Doctor> findByEmail(String email);
    
    /**
     * Find doctor by phone number
     */
    Optional<Doctor> findByPhoneNumber(String phoneNumber);
    
    /**
     * Find doctors by specialization
     */
    List<Doctor> findBySpecializationAndIsDeletedFalse(String specialization);
    
    /**
     * Find all active doctors with pagination
     */
    Page<Doctor> findByIsDeletedFalseAndStatusOrderByCreatedAtDesc(String status, Pageable pageable);
    
    /**
     * Search doctors by name
     */
    @Query("SELECT d FROM Doctor d WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%')) AND d.isDeleted = false")
    List<Doctor> searchByName(@Param("name") String name);
    
    /**
     * Find active doctors
     */
    Page<Doctor> findByIsDeletedFalseOrderByCreatedAtDesc(Pageable pageable);
    
    /**
     * Count active doctors
     */
    long countByIsDeletedFalse();
}
