package com.smartcare.cms;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartcare.cms.dto.LoginRequest;
import com.smartcare.cms.dto.RegisterRequest;
import com.smartcare.cms.model.User.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired private MockMvc       mockMvc;
    @Autowired private ObjectMapper  objectMapper;

    @Test
    void registerPatient_shouldReturn201() throws Exception {
        RegisterRequest req = new RegisterRequest();
        req.setFullName("Test Patient");
        req.setEmail("testpatient@example.com");
        req.setPassword("password123");
        req.setRole(Role.PATIENT);
        req.setDateOfBirth(LocalDate.of(1995, 6, 15));
        req.setPhone("+99999999999");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").isNotEmpty())
                .andExpect(jsonPath("$.data.role").value("PATIENT"));
    }

    @Test
    void registerDoctor_shouldReturn201() throws Exception {
        RegisterRequest req = new RegisterRequest();
        req.setFullName("Dr. Test");
        req.setEmail("testdoctor@example.com");
        req.setPassword("password123");
        req.setRole(Role.DOCTOR);
        req.setSpecialization("Radiologist");
        req.setPhone("+88888888888");
        req.setAvailableSlots("09:00-10:00,14:00-15:00");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.role").value("DOCTOR"));
    }

    @Test
    void login_withInvalidCredentials_shouldReturn401() throws Exception {
        LoginRequest req = new LoginRequest("nobody@example.com", "wrongpass");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void register_withDuplicateEmail_shouldReturn400() throws Exception {
        RegisterRequest req = new RegisterRequest();
        req.setFullName("Duplicate User");
        req.setEmail("dup@example.com");
        req.setPassword("password123");
        req.setRole(Role.PATIENT);
        req.setDateOfBirth(LocalDate.of(1990, 1, 1));
        req.setPhone("+77777777777");

        // First registration succeeds
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        // Second registration with same email fails
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }
}
