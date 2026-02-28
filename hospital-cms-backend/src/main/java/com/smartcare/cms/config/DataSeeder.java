package com.smartcare.cms.config;

import com.smartcare.cms.model.*;
import com.smartcare.cms.model.User.Role;
import com.smartcare.cms.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Seeds the database with demo data on first startup.
 * Skips seeding if records already exist (idempotent).
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
	
    @Autowired
    private  UserRepository userRepository ;
    @Autowired
    private  DoctorRepository      doctorRepository;
    @Autowired
    private  PatientRepository     patientRepository;
    @Autowired
    private  AppointmentRepository appointmentRepository;
    @Autowired
    private  PasswordEncoder       passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            Logger.info("Database already seeded – skipping.");
            return;
        }

        log.info("Seeding database with sample data...");

        // ── Admin ───────────────────────────────────────────────────────────
        User admin = saveUser("Admin User", "admin@hospital.com", "admin123", Role.ADMIN);

        // ── Doctors ─────────────────────────────────────────────────────────
        User u1 = saveUser("Emily Adams",  "dr.adams@example.com",   "password", Role.DOCTOR);
        User u2 = saveUser("Mark Johnson", "dr.johnson@example.com", "password", Role.DOCTOR);
        User u3 = saveUser("Sarah Lee",    "dr.lee@example.com",     "password", Role.DOCTOR);
        User u4 = saveUser("Tom Wilson",   "dr.wilson@example.com",  "password", Role.DOCTOR);
        User u5 = saveUser("Alice Brown",  "dr.brown@example.com",   "password", Role.DOCTOR);
        User u6 = saveUser("Taylor Grant", "dr.taylor@example.com",  "password", Role.DOCTOR);

        Doctor d1 = saveDoctor(u1, "Cardiologist",  "+10000000001", "09:00-10:00,10:00-11:00,11:00-12:00,14:00-15:00");
        Doctor d2 = saveDoctor(u2, "Neurologist",   "+10000000002", "10:00-11:00,11:00-12:00,14:00-15:00,15:00-16:00");
        Doctor d3 = saveDoctor(u3, "Orthopedist",   "+10000000003", "09:00-10:00,11:00-12:00,14:00-15:00,16:00-17:00");
        Doctor d4 = saveDoctor(u4, "Pediatrician",  "+10000000004", "09:00-10:00,15:00-16:00,16:00-17:00");
        Doctor d5 = saveDoctor(u5, "Dermatologist", "+10000000005", "09:00-10:00,10:00-11:00,14:00-15:00,15:00-16:00");
        Doctor d6 = saveDoctor(u6, "Cardiologist",  "+10000000006", "11:00-12:00,14:00-15:00,15:00-16:00");

        // ── Patients ─────────────────────────────────────────────────────────
        User pu1 = saveUser("John Smith",  "john.smith@example.com",  "password", Role.PATIENT);
        User pu2 = saveUser("Jane Doe",    "jane.doe@example.com",    "password", Role.PATIENT);
        User pu3 = saveUser("Bob Williams","bob.w@example.com",        "password", Role.PATIENT);

        Patient p1 = savePatient(pu1, LocalDate.of(1990, 3, 15), "+20000000001", "O+", "123 Main St");
        Patient p2 = savePatient(pu2, LocalDate.of(1985, 7, 22), "+20000000002", "A+", "456 Oak Ave");
        Patient p3 = savePatient(pu3, LocalDate.of(2000, 11, 5),"+20000000003", "B-", "789 Pine Rd");

        // ── Appointments ──────────────────────────────────────────────────────
        saveAppointment(p1, d1, LocalDate.of(2025, 5, 23), "09:00-10:00", Appointment.Status.SCHEDULED);
        saveAppointment(p2, d2, LocalDate.of(2025, 5, 22), "10:00-11:00", Appointment.Status.COMPLETED);
        saveAppointment(p3, d3, LocalDate.of(2025, 5, 22), "14:00-15:00", Appointment.Status.CANCELLED);

        log.info("Database seeding complete. Admin login: admin@hospital.com / admin123");
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private User saveUser(String name, String email, String password, Role role) {
        return userRepository.save(User.builder()
                .fullName(name).email(email)
                .password(passwordEncoder.encode(password))
                .role(role).build());
    }

    private Doctor saveDoctor(User user, String spec, String phone, String slots) {
        return doctorRepository.save(Doctor.builder()
                .user(user).specialization(spec).phone(phone).availableSlots(slots).build());
    }

    private Patient savePatient(User user, LocalDate dob, String phone, String blood, String addr) {
        return patientRepository.save(Patient.builder()
                .user(user).dateOfBirth(dob).phone(phone).bloodGroup(blood).address(addr).build());
    }

    private void saveAppointment(Patient patient, Doctor doctor,
                                 LocalDate date, String slot, Appointment.Status status) {
        appointmentRepository.save(Appointment.builder()
                .patient(patient).doctor(doctor)
                .appointmentDate(date).timeSlot(slot).status(status).build());
    }
}
