package com.smartcare.cms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class DoctorResponse {
    private Long         id;
    private String       fullName;
    private String       email;
    private String       specialization;
    private String       phone;
    private List<String> availableSlots;
    private boolean      active;
}
