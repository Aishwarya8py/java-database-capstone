package com.smartcare.cms.repository;

import com.smartcare.cms.model.Prescription;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrescriptionRepository extends MongoRepository<Prescription, String> {

    Optional<Prescription> findByAppointmentId(Long appointmentId);

    List<Prescription> findByPatientId(Long patientId);

    List<Prescription> findByDoctorId(Long doctorId);

    void deleteByAppointmentId(Long appointmentId);
}
