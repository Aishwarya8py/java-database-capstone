package com.smartcare.cms.controller;

import com.smartcare.cms.dto.ApiResponse;
import com.smartcare.cms.dto.PatientResponse;
import com.smartcare.cms.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Patient REST controller.
 *
 * GET    /api/patients          – list all patients (ADMIN, DOCTOR)
 * GET    /api/patients/{id}     – get one patient
 * PATCH  /api/patients/{id}     – update patient profile
 * DELETE /api/patients/{id}     – delete patient (ADMIN only)
 */
@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<ApiResponse<List<PatientResponse>>> getAllPatients(
            @RequestParam(required = false) String search) {
        return ResponseEntity.ok(
                ApiResponse.ok("Patients retrieved", patientService.getAllPatients(search)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PATIENT')")
    public ResponseEntity<ApiResponse<PatientResponse>> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.ok("Patient retrieved", patientService.getPatientById(id)));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PATIENT')")
    public ResponseEntity<ApiResponse<PatientResponse>> updatePatient(
            @PathVariable Long id,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String bloodGroup,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String medicalHistory) {
        return ResponseEntity.ok(ApiResponse.ok("Patient updated",
                patientService.updatePatient(id, phone, bloodGroup, address, medicalHistory)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.ok(ApiResponse.ok("Patient deleted"));
    }
}
