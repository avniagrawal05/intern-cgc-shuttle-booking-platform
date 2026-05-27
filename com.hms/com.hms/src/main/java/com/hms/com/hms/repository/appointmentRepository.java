package com.hms.com.hms.repository;


import com.hms.com.hms.models.appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface appointmentRepository extends JpaRepository<appointment,Long> {
}
