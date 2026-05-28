package com.hms.repository;

import com.hms.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Patient entity database operations
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    
    /**
     * Find patient by email
     */
    Optional<Patient> findByEmail(String email);
    
    /**
     * Find patient by phone number
     */
    Optional<Patient> findByPhoneNumber(String phoneNumber);
    
    /**
     * Find all active patients with pagination
     */
    Page<Patient> findByIsDeletedFalseAndStatusOrderByCreatedAtDesc(String status, Pageable pageable);
    
    /**
     * Find patients by disease
     */
    List<Patient> findByDiseaseAndIsDeletedFalse(String disease);
    
    /**
     * Search patients by name
     */
    @Query("SELECT p FROM Patient p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) AND p.isDeleted = false")
    List<Patient> searchByName(@Param("name") String name);
    
    /**
     * Find active patients
     */
    Page<Patient> findByIsDeletedFalseOrderByCreatedAtDesc(Pageable pageable);
    
    /**
     * Count active patients
     */
    long countByIsDeletedFalse();
}
