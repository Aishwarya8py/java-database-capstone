package com.smartcare.cms.controller;

import com.smartcare.cms.dto.ApiResponse;
import com.smartcare.cms.dto.DoctorResponse;
import com.smartcare.cms.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Doctor REST controller.
 *
 * GET    /api/doctors            – list all active doctors (public)
 * GET    /api/doctors/{id}       – get one doctor (public)
 * PATCH  /api/doctors/{id}       – update doctor profile (ADMIN or self)
 * DELETE /api/doctors/{id}       – deactivate doctor (ADMIN only)
 */
@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    @Autowired
    private final DoctorService doctorService;
    @Autowired
    private final AuthService authService;


    @GetMapping("/{doctorId}/availability")
    public List<String> getDoctorAvailability(
            @PathVariable Long doctorId,
            @RequestParam String date,
            @RequestParam String role,
            @RequestHeader("Authorization") String token
    ) {
        // Validate JWT token
        authService.validateToken(token.replace("Bearer ", ""));

        // Role-based access (example logic)
        if (!role.equalsIgnoreCase("ADMIN") && !role.equalsIgnoreCase("PATIENT")) {
            throw new RuntimeException("Unauthorized role");
        }

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // Return available times (stored as ElementCollection)
        return doctor.getAvailableTimes();
    }

   

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DoctorResponse>> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.ok("Doctor retrieved", doctorService.getDoctorById(id)));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<ApiResponse<DoctorResponse>> updateDoctor(
            @PathVariable Long id,
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String availableSlots,
            @RequestParam(required = false) Boolean active) {
        return ResponseEntity.ok(ApiResponse.ok("Doctor updated",
                doctorService.updateDoctor(id, specialization, phone, availableSlots, active)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deactivateDoctor(@PathVariable Long id) {
        doctorService.deactivateDoctor(id);
        return ResponseEntity.ok(ApiResponse.ok("Doctor deactivated"));
    }
}
