package com.hms.unit.service;

import com.hms.dto.PatientDto;
import com.hms.entity.Patient;
import com.hms.exception.DuplicateResourceException;
import com.hms.exception.InvalidInputException;
import com.hms.exception.ResourceNotFoundException;
import com.hms.mapper.PatientMapper;
import com.hms.repository.PatientRepository;
import com.hms.service.impl.PatientServiceImpl;
import com.hms.util.PagedResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for PatientService
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Patient Service Tests")
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientServiceImpl patientService;

    private Patient patient;
    private PatientDto patientDto;

    @BeforeEach
    void setUp() {
        patient = Patient.builder()
                .id(1L)
                .name("John Doe")
                .age(30)
                .email("john.doe@example.com")
                .phoneNumber("1234567890")
                .address("123 Main St")
                .disease("Flu")
                .registrationDate(LocalDate.now())
                .createdAt(LocalDateTime.now())
                .isDeleted(false)
                .build();

        patientDto = PatientDto.builder()
                .id(1L)
                .name("John Doe")
                .age(30)
                .email("john.doe@example.com")
                .phoneNumber("1234567890")
                .address("123 Main St")
                .disease("Flu")
                .registrationDate(LocalDate.now())
                .build();
    }

    @Test
    @DisplayName("Should return paginated list of patients")
    void getAllPatients_Success() {
        // Given
        Pageable pageable = PageRequest.of(0, 20);
        List<Patient> patients = Arrays.asList(patient);
        Page<Patient> patientPage = new PageImpl<>(patients, pageable, patients.size());
        
        when(patientRepository.findByIsDeletedFalseOrderByCreatedAtDesc(any(Pageable.class)))
                .thenReturn(patientPage);

        // When
        PagedResponse<PatientDto> result = patientService.getAllPatients(0, 20);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("John Doe");
        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(patientRepository, times(1)).findByIsDeletedFalseOrderByCreatedAtDesc(any(Pageable.class));
    }

    @Test
    @DisplayName("Should return patient by ID")
    void getPatientById_Success() {
        // Given
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        // When
        PatientDto result = patientService.getPatientById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("John Doe");
        verify(patientRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when patient not found")
    void getPatientById_NotFound() {
        // Given
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> patientService.getPatientById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Patient");
    }

    @Test
    @DisplayName("Should create new patient")
    void createPatient_Success() {
        // Given
        when(patientRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(patientRepository.findByPhoneNumber(anyString())).thenReturn(Optional.empty());
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        // When
        PatientDto result = patientService.createPatient(patientDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("John Doe");
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    @DisplayName("Should throw exception for duplicate email")
    void createPatient_DuplicateEmail() {
        // Given
        when(patientRepository.findByEmail(anyString())).thenReturn(Optional.of(patient));

        // When & Then
        assertThatThrownBy(() -> patientService.createPatient(patientDto))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("email");
    }

    @Test
    @DisplayName("Should throw exception for empty name")
    void createPatient_EmptyName() {
        // Given
        patientDto.setName("");

        // When & Then
        assertThatThrownBy(() -> patientService.createPatient(patientDto))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining("name");
    }

    @Test
    @DisplayName("Should update existing patient")
    void updatePatient_Success() {
        // Given
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        // When
        PatientDto result = patientService.updatePatient(1L, patientDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("John Doe");
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    @DisplayName("Should soft delete patient")
    void deletePatient_Success() {
        // Given
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        // When
        patientService.deletePatient(1L);

        // Then
        assertThat(patient.getIsDeleted()).isTrue();
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    @DisplayName("Should search patients by name")
    void searchPatientsByName_Success() {
        // Given
        when(patientRepository.searchByName(anyString())).thenReturn(Arrays.asList(patient));

        // When
        List<PatientDto> result = patientService.searchPatientsByName("John");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("John Doe");
    }

    @Test
    @DisplayName("Should throw exception for empty search name")
    void searchPatientsByName_EmptyName() {
        // When & Then
        assertThatThrownBy(() -> patientService.searchPatientsByName(""))
                .isInstanceOf(InvalidInputException.class);
    }
}
