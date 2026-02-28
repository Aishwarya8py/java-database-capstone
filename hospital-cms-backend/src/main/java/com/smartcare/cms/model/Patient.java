package com.smartcare.cms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Patient profile entity – stored in MySQL.
 * One-to-one with a User account (PATIENT role).
 */
@Entity
@Table(name = "patients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Past(message = "Date of birth must be in the past")
    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @NotBlank(message = "Phone number is required")
    @Column(nullable = false, length = 20)
    private String phone;

    @Size(max = 10)
    @Column(length = 10)
    private String bloodGroup;

    @Size(max = 300)
    @Column(length = 300)
    private String address;

    @Size(max = 500)
    @Column(length = 500)
    private String medicalHistory;

    // ── Convenience getters ──────────────────
    public String getFullName() {
        return user != null ? user.getFullName() : "";
    }

    public String getEmail() {
        return user != null ? user.getEmail() : "";
    }
}
