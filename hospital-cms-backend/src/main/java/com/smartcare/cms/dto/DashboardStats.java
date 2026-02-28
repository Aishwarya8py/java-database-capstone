package com.smartcare.cms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class DashboardStats {
    private long totalDoctors;
    private long totalPatients;
    private long totalAppointments;
    private long scheduledAppointments;
    private long completedAppointments;
    private long cancelledAppointments;
    private long totalPrescriptions;
}
