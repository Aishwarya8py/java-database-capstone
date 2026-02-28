package com.smartcare.cms.dto;

import com.smartcare.cms.model.Appointment.Status;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AppointmentUpdateRequest {
    private LocalDate appointmentDate;
    private String    timeSlot;
    private Status    status;
    @Size(max = 500)
    private String    notes;
}
