package com.smartcare.cms.service;

import com.smartcare.cms.dto.PatientResponse;
import com.smartcare.cms.exception.ResourceNotFoundException;
import com.smartcare.cms.model.Patient;
import com.smartcare.cms.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    @Transactional(readOnly = true)
    public List<PatientResponse> getAllPatients(String search) {
        List<Patient> patients = StringUtils.hasText(search)
                ? patientRepository.search(search)
                : patientRepository.findAll();
        return patients.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PatientResponse getPatientById(Long id) {
        return toResponse(findById(id));
    }

    @Transactional
    public PatientResponse updatePatient(Long id, String phone, String bloodGroup,
                                         String address, String medicalHistory) {
        Patient patient = findById(id);
        if (StringUtils.hasText(phone))          patient.setPhone(phone);
        if (StringUtils.hasText(bloodGroup))     patient.setBloodGroup(bloodGroup);
        if (StringUtils.hasText(address))        patient.setAddress(address);
        if (StringUtils.hasText(medicalHistory)) patient.setMedicalHistory(medicalHistory);
        return toResponse(patientRepository.save(patient));
    }

    @Transactional
    public void deletePatient(Long id) {
        patientRepository.delete(findById(id));
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    public Patient findById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", id));
    }

    public PatientResponse toResponse(Patient p) {
        return PatientResponse.builder()
                .id(p.getId())
                .fullName(p.getFullName())
                .email(p.getEmail())
                .dateOfBirth(p.getDateOfBirth())
                .phone(p.getPhone())
                .bloodGroup(p.getBloodGroup())
                .address(p.getAddress())
                .medicalHistory(p.getMedicalHistory())
                .build();
    }
}
