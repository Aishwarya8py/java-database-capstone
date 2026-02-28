package com.smartcare.cms.controller;

import com.smartcare.cms.dto.ApiResponse;
import com.smartcare.cms.dto.PrescriptionRequest;
import com.smartcare.cms.dto.PrescriptionResponse;
import com.smartcare.cms.service.PrescriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Prescription REST controller (backed by MongoDB).
 *
 * POST   /api/prescriptions                              – create prescription (DOCTOR)
 * GET    /api/prescriptions/appointment/{appointmentId} – get by appointment
 * GET    /api/prescriptions/patient/{patientId}         – patient's prescriptions
 * GET    /api/prescriptions/doctor/{doctorId}           – doctor's prescriptions
 * PUT    /api/prescriptions/{id}                        – update prescription (DOCTOR)
 * DELETE /api/prescriptions/{id}                        – delete prescription (ADMIN)
 */
@RestController
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<ApiResponse<PrescriptionResponse>> createPrescription(
            @Valid @RequestBody PrescriptionRequest request) {
        PrescriptionResponse response = prescriptionService.createPrescription(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Prescription created", response));
    }

    @GetMapping("/appointment/{appointmentId}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PATIENT')")
    public ResponseEntity<ApiResponse<PrescriptionResponse>> getByAppointment(
            @PathVariable Long appointmentId) {
        return ResponseEntity.ok(ApiResponse.ok("Prescription retrieved",
                prescriptionService.getPrescriptionByAppointment(appointmentId)));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PATIENT')")
    public ResponseEntity<ApiResponse<List<PrescriptionResponse>>> getByPatient(
            @PathVariable Long patientId) {
        return ResponseEntity.ok(ApiResponse.ok("Prescriptions retrieved",
                prescriptionService.getPrescriptionsForPatient(patientId)));
    }

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<ApiResponse<List<PrescriptionResponse>>> getByDoctor(
            @PathVariable Long doctorId) {
        return ResponseEntity.ok(ApiResponse.ok("Prescriptions retrieved",
                prescriptionService.getPrescriptionsForDoctor(doctorId)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<ApiResponse<PrescriptionResponse>> updatePrescription(
            @PathVariable String id,
            @Valid @RequestBody PrescriptionRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Prescription updated",
                prescriptionService.updatePrescription(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deletePrescription(@PathVariable String id) {
        prescriptionService.deletePrescription(id);
        return ResponseEntity.ok(ApiResponse.ok("Prescription deleted"));
    }
}
