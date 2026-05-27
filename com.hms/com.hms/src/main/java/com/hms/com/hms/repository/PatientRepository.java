package com.hms.com.hms.repository;

import com.hms.com.hms.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient,Long> {


    Optional<Patient> findAllById(Long id);
}
