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

// ── Request ──────────────────────────────────────────────────────────────────

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class AppointmentRequestInner {
    @NotNull(message = "Patient ID is required")
    public Long patientId;
    @NotNull(message = "Doctor ID is required")
    public Long doctorId;
    @NotNull(message = "Date is required")
    public LocalDate appointmentDate;
    @NotBlank(message = "Time slot is required")
    public String timeSlot;
    @Size(max = 500) public String notes;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class AppointmentUpdateRequestInner {
    public LocalDate appointmentDate;
    public String    timeSlot;
    public Status    status;
    @Size(max = 500) public String notes;
}

// ── Response ─────────────────────────────────────────────────────────────────

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class AppointmentResponseInner {
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
