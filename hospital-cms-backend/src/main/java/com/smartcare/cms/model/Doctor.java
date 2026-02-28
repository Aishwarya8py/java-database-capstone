package com.smartcare.cms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Doctor profile entity – stored in MySQL.
 * One-to-one with a User account (DOCTOR role).
 */
@Entity
@Table(name = "doctors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @NotBlank(message = "Specialization is required")
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String specialization;

    @NotBlank(message = "Phone number is required")
    @Column(nullable = false, length = 20)
    private String phone;

    /**
     * Comma-separated available time slots, e.g. "09:00-10:00,14:00-15:00".
     * Stored as a single column for simplicity; a join-table would be used in
     * a larger system.
     */
    @Column(nullable = false, length = 500)
    private String availableSlots;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    // ── Convenience getters ──────────────────
    public String getFullName() {
        return user != null ? user.getFullName() : "";
    }

    public String getEmail() {
        return user != null ? user.getEmail() : "";
    }

    public List<String> getSlotList() {
        if (availableSlots == null || availableSlots.isBlank()) return List.of();
        return List.of(availableSlots.split(","));
    }
}
