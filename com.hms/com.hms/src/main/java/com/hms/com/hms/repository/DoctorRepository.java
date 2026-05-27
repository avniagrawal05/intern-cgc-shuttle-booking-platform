package com.hms.com.hms.repository;


import com.hms.com.hms.models.doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<doctor,Long> {
    Optional<doctor> findAllById(Long id);
}
