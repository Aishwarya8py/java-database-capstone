package com.smartcare.cms.dto;

import com.smartcare.cms.model.Appointment.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// ── Appointment update ────────────────────────────────────────────────────────

@Data @NoArgsConstructor @AllArgsConstructor @Builder
class AppointmentUpdateRequestDto {
    public LocalDate appointmentDate;
    public String    timeSlot;
    public Status    status;
    @Size(max = 500) public String notes;
}

// ── Doctor ────────────────────────────────────────────────────────────────────

@Data @NoArgsConstructor @AllArgsConstructor @Builder
class DoctorResponseDto {
    public Long         id;
    public String       fullName;
    public String       email;
    public String       specialization;
    public String       phone;
    public List<String> availableSlots;
    public boolean      active;
}

// ── Patient ───────────────────────────────────────────────────────────────────

@Data @NoArgsConstructor @AllArgsConstructor @Builder
class PatientResponseDto {
    public Long      id;
    public String    fullName;
    public String    email;
    public LocalDate dateOfBirth;
    public String    phone;
    public String    bloodGroup;
    public String    address;
    public String    medicalHistory;
}

// ── Prescription ──────────────────────────────────────────────────────────────

@Data @NoArgsConstructor @AllArgsConstructor @Builder
class PrescriptionRequestDto {
    @NotNull  public Long   appointmentId;
    @NotBlank public String medicineNames;
    @NotBlank public String dosageInstructions;
    public String additionalNotes;
    public List<MedicineEntryDtoInner> medicines;
}

@Data @NoArgsConstructor @AllArgsConstructor @Builder
class PrescriptionResponseDto {
    public String   id;
    public Long     appointmentId;
    public Long     patientId;
    public String   patientName;
    public Long     doctorId;
    public String   doctorName;
    public String   medicineNames;
    public String   dosageInstructions;
    public String   additionalNotes;
    public List<MedicineEntryDtoInner> medicines;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
}

@Data @NoArgsConstructor @AllArgsConstructor @Builder
class MedicineEntryDtoInner {
    public String  name;
    public String  dosage;
    public String  frequency;
    public Integer durationDays;
}

// ── Dashboard ─────────────────────────────────────────────────────────────────

@Data @NoArgsConstructor @AllArgsConstructor @Builder
class DashboardStatsDto {
    public long totalDoctors;
    public long totalPatients;
    public long totalAppointments;
    public long scheduledAppointments;
    public long completedAppointments;
    public long cancelledAppointments;
    public long totalPrescriptions;
}
