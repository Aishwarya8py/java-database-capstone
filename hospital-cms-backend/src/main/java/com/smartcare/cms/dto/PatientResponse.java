package com.smartcare.cms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PatientResponse {
    private Long      id;
    private String    fullName;
    private String    email;
    private LocalDate dateOfBirth;
    private String    phone;
    private String    bloodGroup;
    private String    address;
    private String    medicalHistory;
}
