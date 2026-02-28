package com.smartcare.cms.dto;

import com.smartcare.cms.model.User.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100)
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Must be a valid email")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 72, message = "Password must be 6-72 characters")
    private String password;

    @NotNull(message = "Role is required")
    private Role role;

    // ── Doctor-specific (required when role = DOCTOR) ──
    private String specialization;
    private String phone;
    private String availableSlots;

    // ── Patient-specific (required when role = PATIENT) ──
    private LocalDate dateOfBirth;
    private String    bloodGroup;
    private String    address;
}
