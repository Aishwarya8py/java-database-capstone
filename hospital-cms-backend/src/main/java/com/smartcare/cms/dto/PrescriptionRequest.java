package com.smartcare.cms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

// ── Request ───────────────────────────────────────────────────────────────────

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PrescriptionRequest {
    @NotNull(message = "Appointment ID is required")
    private Long   appointmentId;
    @NotBlank(message = "Medicine names are required")
    private String medicineNames;
    @NotBlank(message = "Dosage instructions are required")
    private String dosageInstructions;
    private String additionalNotes;
    private List<MedicineEntryDto> medicines;
}
