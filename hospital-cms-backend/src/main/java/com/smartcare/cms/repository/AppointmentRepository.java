package com.smartcare.cms.repository;

import com.smartcare.cms.model.Appointment;
import com.smartcare.cms.model.Appointment.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByDoctorId(Long doctorId);

    List<Appointment> findByPatientIdOrderByAppointmentDateDesc(Long patientId);

    List<Appointment> findByDoctorIdOrderByAppointmentDateAsc(Long doctorId);

    List<Appointment> findByStatus(Status status);

    List<Appointment> findByAppointmentDate(LocalDate date);

    List<Appointment> findByDoctorIdAndAppointmentDate(Long doctorId, LocalDate date);

    /** Check for slot conflict before booking. */
    @Query("SELECT COUNT(a) > 0 FROM Appointment a WHERE " +
           "a.doctor.id = :doctorId AND " +
           "a.appointmentDate = :date AND " +
           "a.timeSlot = :slot AND " +
           "a.status != 'CANCELLED'")
    boolean isSlotTaken(@Param("doctorId") Long doctorId,
                        @Param("date")     LocalDate date,
                        @Param("slot")     String slot);

    /** Dashboard counts. */
    long countByStatus(Status status);
}
