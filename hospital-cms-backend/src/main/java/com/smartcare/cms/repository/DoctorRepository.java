package com.smartcare.cms.repository;

import com.smartcare.cms.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByUserId(Long userId);

    List<Doctor> findByActiveTrue();

    List<Doctor> findBySpecializationIgnoreCaseAndActiveTrue(String specialization);

    @Query("SELECT d FROM Doctor d WHERE d.active = true AND " +
           "LOWER(d.user.fullName) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Doctor> searchByName(@Param("query") String query);

    @Query("SELECT d FROM Doctor d WHERE d.active = true AND " +
           "LOWER(d.specialization) LIKE LOWER(CONCAT('%', :spec, '%'))")
    List<Doctor> findBySpecializationContaining(@Param("spec") String spec);
}
