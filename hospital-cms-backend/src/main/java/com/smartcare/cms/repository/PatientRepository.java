package com.smartcare.cms.repository;

import com.smartcare.cms.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByUserId(Long userId);

    @Query("SELECT p FROM Patient p WHERE " +
           "LOWER(p.user.fullName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.user.email)    LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Patient> search(@Param("query") String query);
}
