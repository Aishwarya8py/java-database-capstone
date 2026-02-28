package com.smartcare.cms.service;

import com.smartcare.cms.dto.DoctorResponse;
import com.smartcare.cms.exception.ResourceNotFoundException;
import com.smartcare.cms.model.Doctor;
import com.smartcare.cms.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    /** Return all active doctors, with optional search/filter. */
    @Transactional(readOnly = true)
    public List<DoctorResponse> getAllDoctors(String search, String specialty) {
        List<Doctor> doctors;

        if (StringUtils.hasText(search)) {
            doctors = doctorRepository.searchByName(search);
        } else if (StringUtils.hasText(specialty)) {
            doctors = doctorRepository.findBySpecializationContaining(specialty);
        } else {
            doctors = doctorRepository.findByActiveTrue();
        }

        return doctors.stream().map(this::toResponse).collect(Collectors.toList());
    }

    /** Return a single doctor by ID. */
    @Transactional(readOnly = true)
    public DoctorResponse getDoctorById(Long id) {
        return toResponse(findById(id));
    }

    /** Update doctor profile fields. */
    @Transactional
    public DoctorResponse updateDoctor(Long id, String specialization,
                                       String phone, String availableSlots, Boolean active) {
        Doctor doctor = findById(id);
        if (StringUtils.hasText(specialization)) doctor.setSpecialization(specialization);
        if (StringUtils.hasText(phone))           doctor.setPhone(phone);
        if (StringUtils.hasText(availableSlots))  doctor.setAvailableSlots(availableSlots);
        if (active != null)                        doctor.setActive(active);
        return toResponse(doctorRepository.save(doctor));
    }

    /** Soft-delete (deactivate) a doctor. */
    @Transactional
    public void deactivateDoctor(Long id) {
        Doctor doctor = findById(id);
        doctor.setActive(false);
        doctorRepository.save(doctor);
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    public Doctor findById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id));
    }

    public DoctorResponse toResponse(Doctor d) {
        return DoctorResponse.builder()
                .id(d.getId())
                .fullName(d.getFullName())
                .email(d.getEmail())
                .specialization(d.getSpecialization())
                .phone(d.getPhone())
                .availableSlots(d.getSlotList())
                .active(d.isActive())
                .build();
    }
}
