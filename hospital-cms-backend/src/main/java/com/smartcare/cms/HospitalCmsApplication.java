package com.smartcare.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hospital CMS – Spring Boot Entry Point
 * <p>
 * Smart Clinic Management System by SmartCare Solutions.
 * Provides REST APIs for Doctors, Patients, Appointments, and Prescriptions.
 */
@SpringBootApplication
public class HospitalCmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(HospitalCmsApplication.class, args);
    }
}
