package com.hms.com.hms.repository;


import com.hms.com.hms.models.billing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface billingRepository extends JpaRepository<billing,Long> {
}
