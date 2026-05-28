package com.hms.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hms.dto.PatientDto;
import com.hms.entity.Patient;
import com.hms.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for PatientController
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("Patient Controller Integration Tests")
class PatientControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Patient patient;

    @BeforeEach
    void setUp() {
        patient = Patient.builder()
                .name("John Doe")
                .age(30)
                .email("john.doe@example.com")
                .phoneNumber("1234567890")
                .address("123 Main St")
                .disease("Flu")
                .registrationDate(LocalDate.now())
                .isDeleted(false)
                .build();
        patientRepository.save(patient);
    }

    @Test
    @DisplayName("Should get all patients with pagination")
    void getAllPatients_Success() throws Exception {
        mockMvc.perform(get("/hms/api/v1/patients")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content[0].name").value("John Doe"));
    }

    @Test
    @DisplayName("Should get patient by ID")
    void getPatientById_Success() throws Exception {
        mockMvc.perform(get("/hms/api/v1/patients/{id}", patient.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("John Doe"))
                .andExpect(jsonPath("$.data.email").value("john.doe@example.com"));
    }

    @Test
    @DisplayName("Should return 404 for non-existent patient")
    void getPatientById_NotFound() throws Exception {
        mockMvc.perform(get("/hms/api/v1/patients/{id}", 99999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    @DisplayName("Should create new patient")
    void createPatient_Success() throws Exception {
        PatientDto newPatient = PatientDto.builder()
                .name("Jane Doe")
                .age(25)
                .email("jane.doe@example.com")
                .phoneNumber("0987654321")
                .address("456 Oak St")
                .disease("Cold")
                .registrationDate(LocalDate.now())
                .build();

        mockMvc.perform(post("/hms/api/v1/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newPatient)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Jane Doe"))
                .andExpect(jsonPath("$.data.id").exists());
    }

    @Test
    @DisplayName("Should return 400 for invalid patient data")
    void createPatient_InvalidData() throws Exception {
        PatientDto invalidPatient = PatientDto.builder()
                .name("")
                .build();

        mockMvc.perform(post("/hms/api/v1/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidPatient)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("Should update existing patient")
    void updatePatient_Success() throws Exception {
        PatientDto updateDto = PatientDto.builder()
                .name("John Updated")
                .age(31)
                .email("john.doe@example.com")
                .phoneNumber("1234567890")
                .address("123 Main St")
                .disease("Flu")
                .build();

        mockMvc.perform(put("/hms/api/v1/patients/{id}", patient.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("John Updated"));
    }

    @Test
    @DisplayName("Should delete patient")
    void deletePatient_Success() throws Exception {
        mockMvc.perform(delete("/hms/api/v1/patients/{id}", patient.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("Should search patients by name")
    void searchPatientsByName_Success() throws Exception {
        mockMvc.perform(get("/hms/api/v1/patients/search")
                .param("name", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].name").value("John Doe"));
    }
}
