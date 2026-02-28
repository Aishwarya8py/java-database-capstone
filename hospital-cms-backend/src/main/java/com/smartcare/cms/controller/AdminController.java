package com.smartcare.cms.controller;

import com.smartcare.cms.dto.ApiResponse;
import com.smartcare.cms.dto.DashboardStats;
import com.smartcare.cms.model.Appointment.Status;
import com.smartcare.cms.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Admin-only controller.
 *
 * GET /api/admin/dashboard  – aggregate statistics for the admin dashboard
 */
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository        userRepository;
    private final DoctorRepository      doctorRepository;
    private final PatientRepository     patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final PrescriptionRepository prescriptionRepository;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<DashboardStats>> getDashboardStats() {
        DashboardStats stats = DashboardStats.builder()
                .totalDoctors(doctorRepository.count())
                .totalPatients(patientRepository.count())
                .totalAppointments(appointmentRepository.count())
                .scheduledAppointments(appointmentRepository.countByStatus(Status.SCHEDULED))
                .completedAppointments(appointmentRepository.countByStatus(Status.COMPLETED))
                .cancelledAppointments(appointmentRepository.countByStatus(Status.CANCELLED))
                .totalPrescriptions(prescriptionRepository.count())
                .build();

        return ResponseEntity.ok(ApiResponse.ok("Dashboard stats retrieved", stats));
    }
}
