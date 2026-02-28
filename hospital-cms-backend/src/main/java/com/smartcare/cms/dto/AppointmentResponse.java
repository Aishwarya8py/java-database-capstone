package com.smartcare.cms.dto;

import com.smartcare.cms.model.Appointment.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AppointmentResponse {
    private Long         id;
    private Long         patientId;
    private String       patientName;
    private Long         doctorId;
    private String       doctorName;
    private String       doctorSpecialty;
    private LocalDate    appointmentDate;
    private String       timeSlot;
    private Status       status;
    private String       notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
