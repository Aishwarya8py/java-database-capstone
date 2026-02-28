package com.smartcare.cms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class MedicineEntryDto {
    private String  name;
    private String  dosage;
    private String  frequency;
    private Integer durationDays;
}
