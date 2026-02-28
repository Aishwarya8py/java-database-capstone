package com.smartcare.cms.dto;

import com.smartcare.cms.model.Appointment.Status;
import com.smartcare.cms.model.User.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// ──────────────────────────────────────────────────────────────────────────────
//  AUTH DTOs
// ──────────────────────────────────────────────────────────────────────────────

class AuthDtos {}

/** Incoming login request. */
@Data @NoArgsConstructor @AllArgsConstructor
class LoginRequest {
    @NotBlank @Email
    public String email;
    @NotBlank @Size(min = 6)
    public String password;
}

/** Incoming registration request. */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
class RegisterRequest {
    @NotBlank @Size(min = 2, max = 100)
    public String fullName;
    @NotBlank @Email
    public String email;
    @NotBlank @Size(min = 6, max = 72)
    public String password;
    @NotNull
    public Role role;
    // Doctor-only fields (optional)
    public String specialization;
    public String phone;
    public String availableSlots;
    // Patient-only fields (optional)
    public LocalDate dateOfBirth;
    public String bloodGroup;
    public String address;
}

/** JWT response returned after successful auth. */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
class JwtResponse {
    public String token;
    public String type = "Bearer";
    public Long   id;
    public String fullName;
    public String email;
    public String role;
}

// ──────────────────────────────────────────────────────────────────────────────
//  DOCTOR DTOs
// ──────────────────────────────────────────────────────────────────────────────

@Data @NoArgsConstructor @AllArgsConstructor @Builder
class DoctorResponse {
    public Long   id;
    public String fullName;
    public String email;
    public String specialization;
    public String phone;
    public List<String> availableSlots;
    public boolean active;
}

@Data @NoArgsConstructor @AllArgsConstructor
class DoctorUpdateRequest {
    @Size(max = 100) public String specialization;
    @Size(max = 20)  public String phone;
    public String availableSlots;
    public Boolean active;
}

// ──────────────────────────────────────────────────────────────────────────────
//  PATIENT DTOs
// ──────────────────────────────────────────────────────────────────────────────

@Data @NoArgsConstructor @AllArgsConstructor @Builder
class PatientResponse {
    public Long        id;
    public String      fullName;
    public String      email;
    public LocalDate   dateOfBirth;
    public String      phone;
    public String      bloodGroup;
    public String      address;
    public String      medicalHistory;
}

@Data @NoArgsConstructor @AllArgsConstructor
class PatientUpdateRequest {
    public String      phone;
    public String      bloodGroup;
    public String      address;
    @Size(max = 500)
    public String      medicalHistory;
}

// ──────────────────────────────────────────────────────────────────────────────
//  APPOINTMENT DTOs
// ──────────────────────────────────────────────────────────────────────────────

@Data @NoArgsConstructor @AllArgsConstructor @Builder
class AppointmentRequest {
    @NotNull  public Long      patientId;
    @NotNull  public Long      doctorId;
    @NotNull  public LocalDate appointmentDate;
    @NotBlank public String    timeSlot;
    @Size(max = 500) public String notes;
}

@Data @NoArgsConstructor @AllArgsConstructor @Builder
class AppointmentResponse {
    public Long          id;
    public Long          patientId;
    public String        patientName;
    public Long          doctorId;
    public String        doctorName;
    public String        doctorSpecialty;
    public LocalDate     appointmentDate;
    public String        timeSlot;
    public Status        status;
    public String        notes;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
}

@Data @NoArgsConstructor @AllArgsConstructor
class AppointmentUpdateRequest {
    public LocalDate appointmentDate;
    public String    timeSlot;
    public Status    status;
    public String    notes;
}

// ──────────────────────────────────────────────────────────────────────────────
//  PRESCRIPTION DTOs
// ──────────────────────────────────────────────────────────────────────────────

@Data @NoArgsConstructor @AllArgsConstructor @Builder
class PrescriptionRequest {
    @NotNull  public Long   appointmentId;
    @NotBlank public String medicineNames;
    @NotBlank public String dosageInstructions;
    public String additionalNotes;
    public List<MedicineEntryDto> medicines;
}

@Data @NoArgsConstructor @AllArgsConstructor @Builder
class PrescriptionResponse {
    public String   id;
    public Long     appointmentId;
    public Long     patientId;
    public String   patientName;
    public Long     doctorId;
    public String   doctorName;
    public String   medicineNames;
    public String   dosageInstructions;
    public String   additionalNotes;
    public List<MedicineEntryDto> medicines;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
}

@Data @NoArgsConstructor @AllArgsConstructor @Builder
class MedicineEntryDto {
    public String  name;
    public String  dosage;
    public String  frequency;
    public Integer durationDays;
}

// ──────────────────────────────────────────────────────────────────────────────
//  DASHBOARD / ADMIN DTO
// ──────────────────────────────────────────────────────────────────────────────

@Data @NoArgsConstructor @AllArgsConstructor @Builder
class DashboardStats {
    public long totalDoctors;
    public long totalPatients;
    public long totalAppointments;
    public long scheduledAppointments;
    public long completedAppointments;
    public long cancelledAppointments;
    public long totalPrescriptions;
}

// ──────────────────────────────────────────────────────────────────────────────
//  GENERIC API RESPONSE WRAPPER
// ──────────────────────────────────────────────────────────────────────────────

@Data @NoArgsConstructor @AllArgsConstructor @Builder
class ApiResponse<T> {
    public boolean success;
    public String  message;
    public T       data;

    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
}
