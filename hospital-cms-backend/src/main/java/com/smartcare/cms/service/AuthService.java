package com.smartcare.cms.service;

import com.smartcare.cms.dto.*;
import com.smartcare.cms.exception.BadRequestException;
import com.smartcare.cms.exception.ResourceNotFoundException;
import com.smartcare.cms.model.Doctor;
import com.smartcare.cms.model.Patient;
import com.smartcare.cms.model.User;
import com.smartcare.cms.model.User.Role;
import com.smartcare.cms.repository.DoctorRepository;
import com.smartcare.cms.repository.PatientRepository;
import com.smartcare.cms.repository.UserRepository;
import com.smartcare.cms.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handles user registration (with role-specific profile creation)
 * and JWT-based login.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository    userRepository;
    private final DoctorRepository  doctorRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder   passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider  jwtTokenProvider;

    /** Register a new user and create the appropriate profile (Doctor/Patient). */
    @Transactional
    public JwtResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new BadRequestException("Email is already in use: " + req.getEmail());
        }

        // 1. Create base User
        User user = User.builder()
                .fullName(req.getFullName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(req.getRole())
                .build();
        user = userRepository.save(user);
        log.info("Registered new user: {} ({})", user.getEmail(), user.getRole());

        // 2. Create role-specific profile
        if (req.getRole() == Role.DOCTOR) {
            Doctor doctor = Doctor.builder()
                    .user(user)
                    .specialization(req.getSpecialization() != null ? req.getSpecialization() : "General")
                    .phone(req.getPhone() != null ? req.getPhone() : "")
                    .availableSlots(req.getAvailableSlots() != null ? req.getAvailableSlots() : "09:00-10:00")
                    .build();
            doctorRepository.save(doctor);
        } else if (req.getRole() == Role.PATIENT) {
            Patient patient = Patient.builder()
                    .user(user)
                    .dateOfBirth(req.getDateOfBirth())
                    .phone(req.getPhone() != null ? req.getPhone() : "")
                    .bloodGroup(req.getBloodGroup())
                    .address(req.getAddress())
                    .build();
            patientRepository.save(patient);
        }

        // 3. Issue JWT immediately after registration
        String token = jwtTokenProvider.generateTokenFromEmail(user.getEmail());
        return JwtResponse.builder()
                .token(token)
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    /** Authenticate existing user and return JWT. */
    public JwtResponse login(LoginRequest req) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));

        String token = jwtTokenProvider.generateToken(authentication);

        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", req.getEmail()));

        log.info("User logged in: {}", user.getEmail());
        return JwtResponse.builder()
                .token(token)
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}
