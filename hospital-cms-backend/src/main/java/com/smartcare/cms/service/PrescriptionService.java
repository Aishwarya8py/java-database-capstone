package com.smartcare.cms.service;

import com.smartcare.cms.dto.MedicineEntryDto;
import com.smartcare.cms.dto.PrescriptionRequest;
import com.smartcare.cms.dto.PrescriptionResponse;
import com.smartcare.cms.exception.ResourceNotFoundException;
import com.smartcare.cms.model.Appointment;
import com.smartcare.cms.model.Prescription;
import com.smartcare.cms.model.Prescription.MedicineEntry;
import com.smartcare.cms.repository.AppointmentRepository;
import com.smartcare.cms.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentRepository  appointmentRepository;

    /** Create a new prescription for an appointment. */
    public PrescriptionResponse createPrescription(PrescriptionRequest req) {
        Appointment appt = appointmentRepository.findById(req.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appointment", "id", req.getAppointmentId()));

        Prescription prescription = Prescription.builder()
                .appointmentId(req.getAppointmentId())
                .patientId(appt.getPatient().getId())
                .patientName(appt.getPatient().getFullName())
                .doctorId(appt.getDoctor().getId())
                .doctorName(appt.getDoctor().getFullName())
                .medicineNames(req.getMedicineNames())
                .dosageInstructions(req.getDosageInstructions())
                .additionalNotes(req.getAdditionalNotes())
                .medicines(mapMedicines(req.getMedicines()))
                .build();

        return toResponse(prescriptionRepository.save(prescription));
    }

    public PrescriptionResponse getPrescriptionByAppointment(Long appointmentId) {
        Prescription rx = prescriptionRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Prescription", "appointmentId", appointmentId));
        return toResponse(rx);
    }

    public List<PrescriptionResponse> getPrescriptionsForPatient(Long patientId) {
        return prescriptionRepository.findByPatientId(patientId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<PrescriptionResponse> getPrescriptionsForDoctor(Long doctorId) {
        return prescriptionRepository.findByDoctorId(doctorId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public PrescriptionResponse updatePrescription(String id, PrescriptionRequest req) {
        Prescription rx = prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription", "id", id));
        if (req.getMedicineNames()       != null) rx.setMedicineNames(req.getMedicineNames());
        if (req.getDosageInstructions()  != null) rx.setDosageInstructions(req.getDosageInstructions());
        if (req.getAdditionalNotes()     != null) rx.setAdditionalNotes(req.getAdditionalNotes());
        if (req.getMedicines()           != null) rx.setMedicines(mapMedicines(req.getMedicines()));
        return toResponse(prescriptionRepository.save(rx));
    }

    public void deletePrescription(String id) {
        if (!prescriptionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Prescription", "id", id);
        }
        prescriptionRepository.deleteById(id);
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private List<MedicineEntry> mapMedicines(List<MedicineEntryDto> dtos) {
        if (dtos == null) return null;
        return dtos.stream().map(d -> MedicineEntry.builder()
                        .name(d.getName())
                        .dosage(d.getDosage())
                        .frequency(d.getFrequency())
                        .durationDays(d.getDurationDays())
                        .build())
                .collect(Collectors.toList());
    }

    private List<MedicineEntryDto> mapMedicineDtos(List<MedicineEntry> entries) {
        if (entries == null) return null;
        return entries.stream().map(e -> MedicineEntryDto.builder()
                        .name(e.getName())
                        .dosage(e.getDosage())
                        .frequency(e.getFrequency())
                        .durationDays(e.getDurationDays())
                        .build())
                .collect(Collectors.toList());
    }

    private PrescriptionResponse toResponse(Prescription rx) {
        return PrescriptionResponse.builder()
                .id(rx.getId())
                .appointmentId(rx.getAppointmentId())
                .patientId(rx.getPatientId())
                .patientName(rx.getPatientName())
                .doctorId(rx.getDoctorId())
                .doctorName(rx.getDoctorName())
                .medicineNames(rx.getMedicineNames())
                .dosageInstructions(rx.getDosageInstructions())
                .additionalNotes(rx.getAdditionalNotes())
                .medicines(mapMedicineDtos(rx.getMedicines()))
                .createdAt(rx.getCreatedAt())
                .updatedAt(rx.getUpdatedAt())
                .build();
    }
}
