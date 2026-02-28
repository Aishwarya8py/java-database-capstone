package com.smartcare.cms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Prescription document – stored in MongoDB.
 * Flexible structure allows future extension (e.g. attachments, photos).
 * Linked to a MySQL Appointment by appointmentId.
 */
@Document(collection = "prescriptions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prescription {

    @Id
    private String id;

    /** Foreign key reference to Appointment.id in MySQL. */
    @NotNull(message = "Appointment ID is required")
    @Indexed(unique = true)
    private Long appointmentId;

    /** Denormalized for fast read without MySQL join. */
    private Long patientId;
    private String patientName;
    private Long doctorId;
    private String doctorName;

    @NotBlank(message = "At least one medicine name is required")
    private String medicineNames;

    @NotBlank(message = "Dosage instructions are required")
    private String dosageInstructions;

    private String additionalNotes;

    /** Structured medicine list (optional, richer alternative to medicineNames). */
    private List<MedicineEntry> medicines;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    /** Embedded medicine detail document. */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MedicineEntry {
        private String name;
        private String dosage;
        private String frequency;   // e.g. "Twice a day"
        private Integer durationDays;
    }
}
