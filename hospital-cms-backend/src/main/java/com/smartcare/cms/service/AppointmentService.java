package com.smartcare.cms.service;

import com.smartcare.cms.dto.AppointmentRequest;
import com.smartcare.cms.dto.AppointmentResponse;
import com.smartcare.cms.dto.AppointmentUpdateRequest;
import com.smartcare.cms.exception.BadRequestException;
import com.smartcare.cms.exception.ResourceNotFoundException;
import com.smartcare.cms.model.Appointment;
import com.smartcare.cms.model.Appointment.Status;
import com.smartcare.cms.model.Doctor;
import com.smartcare.cms.model.Patient;
import com.smartcare.cms.repository.AppointmentRepository;
import com.smartcare.cms.repository.DoctorRepository;
import com.smartcare.cms.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository      doctorRepository;
    private final PatientRepository     patientRepository;

    /** Book a new appointment (checks slot availability first). */
    @Transactional
    public AppointmentResponse createAppointment(AppointmentRequest req) {
        // Validate no conflict
        if (appointmentRepository.isSlotTaken(req.getDoctorId(),
                                               req.getAppointmentDate(),
                                               req.getTimeSlot())) {
            throw new BadRequestException(
                    "Time slot " + req.getTimeSlot() + " on " + req.getAppointmentDate()
                    + " is already booked for this doctor.");
        }

        Doctor  doctor  = doctorRepository.findById(req.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", req.getDoctorId()));
        Patient patient = patientRepository.findById(req.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", req.getPatientId()));

        Appointment appt = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentDate(req.getAppointmentDate())
                .timeSlot(req.getTimeSlot())
                .notes(req.getNotes())
                .build();

        return toResponse(appointmentRepository.save(appt));
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAllAppointments() {
        return appointmentRepository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AppointmentResponse getAppointmentById(Long id) {
        return toResponse(findById(id));
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAppointmentsForPatient(Long patientId) {
        return appointmentRepository.findByPatientIdOrderByAppointmentDateDesc(patientId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAppointmentsForDoctor(Long doctorId) {
        return appointmentRepository.findByDoctorIdOrderByAppointmentDateAsc(doctorId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    /** Partial update – only non-null fields are applied. */
    @Transactional
    public AppointmentResponse updateAppointment(Long id, AppointmentUpdateRequest req) {
        Appointment appt = findById(id);

        if (req.getAppointmentDate() != null) appt.setAppointmentDate(req.getAppointmentDate());
        if (StringUtils.hasText(req.getTimeSlot())) appt.setTimeSlot(req.getTimeSlot());
        if (req.getStatus() != null) appt.setStatus(req.getStatus());
        if (req.getNotes() != null)  appt.setNotes(req.getNotes());

        return toResponse(appointmentRepository.save(appt));
    }

    @Transactional
    public void cancelAppointment(Long id) {
        Appointment appt = findById(id);
        appt.setStatus(Status.CANCELLED);
        appointmentRepository.save(appt);
    }

    @Transactional
    public void deleteAppointment(Long id) {
        appointmentRepository.delete(findById(id));
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private Appointment findById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", id));
    }

    public AppointmentResponse toResponse(Appointment a) {
        return AppointmentResponse.builder()
                .id(a.getId())
                .patientId(a.getPatient().getId())
                .patientName(a.getPatient().getFullName())
                .doctorId(a.getDoctor().getId())
                .doctorName(a.getDoctor().getFullName())
                .doctorSpecialty(a.getDoctor().getSpecialization())
                .appointmentDate(a.getAppointmentDate())
                .timeSlot(a.getTimeSlot())
                .status(a.getStatus())
                .notes(a.getNotes())
                .createdAt(a.getCreatedAt())
                .updatedAt(a.getUpdatedAt())
                .build();
    }
}
