package com.smartcare.cms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PrescriptionResponse {
    private String   id;
    private Long     appointmentId;
    private Long     patientId;
    private String   patientName;
    private Long     doctorId;
    private String   doctorName;
    private String   medicineNames;
    private String   dosageInstructions;
    private String   additionalNotes;
    private List<MedicineEntryDto> medicines;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
