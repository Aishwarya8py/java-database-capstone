package com.smartcare.cms.controller;

import com.smartcare.cms.dto.ApiResponse;
import com.smartcare.cms.dto.AppointmentRequest;
import com.smartcare.cms.dto.AppointmentResponse;
import com.smartcare.cms.dto.AppointmentUpdateRequest;
import com.smartcare.cms.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Appointment REST controller.
 *
 * POST   /api/appointments                        – book appointment
 * GET    /api/appointments                        – list all (ADMIN)
 * GET    /api/appointments/{id}                   – get by ID
 * GET    /api/appointments/patient/{patientId}    – patient's appointments
 * GET    /api/appointments/doctor/{doctorId}      – doctor's appointments
 * PATCH  /api/appointments/{id}                   – update appointment
 * PATCH  /api/appointments/{id}/cancel            – cancel appointment
 * DELETE /api/appointments/{id}                   – delete (ADMIN/DOCTOR)
 */
@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','PATIENT')")
    public ResponseEntity<ApiResponse<AppointmentResponse>> createAppointment(
            @Valid @RequestBody AppointmentRequest request) {
        AppointmentResponse response = appointmentService.createAppointment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Appointment booked successfully", response));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getAllAppointments() {
        return ResponseEntity.ok(
                ApiResponse.ok("Appointments retrieved", appointmentService.getAllAppointments()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PATIENT')")
    public ResponseEntity<ApiResponse<AppointmentResponse>> getAppointmentById(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.ok("Appointment retrieved", appointmentService.getAppointmentById(id)));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN','PATIENT')")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getPatientAppointments(
            @PathVariable Long patientId) {
        return ResponseEntity.ok(ApiResponse.ok("Patient appointments retrieved",
                appointmentService.getAppointmentsForPatient(patientId)));
    }

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getDoctorAppointments(
            @PathVariable Long doctorId) {
        return ResponseEntity.ok(ApiResponse.ok("Doctor appointments retrieved",
                appointmentService.getAppointmentsForDoctor(doctorId)));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PATIENT')")
    public ResponseEntity<ApiResponse<AppointmentResponse>> updateAppointment(
            @PathVariable Long id,
            @RequestBody AppointmentUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Appointment updated",
                appointmentService.updateAppointment(id, request)));
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PATIENT')")
    public ResponseEntity<ApiResponse<Void>> cancelAppointment(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return ResponseEntity.ok(ApiResponse.ok("Appointment cancelled"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<ApiResponse<Void>> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.ok(ApiResponse.ok("Appointment deleted"));
    }
}
