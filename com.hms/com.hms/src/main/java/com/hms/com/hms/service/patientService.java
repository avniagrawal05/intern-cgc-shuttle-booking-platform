package com.hms.com.hms.service;

import com.hms.com.hms.models.Patient;
import com.hms.com.hms.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class patientService {

    @Autowired
    private PatientRepository patientRepository;


    public List<Patient> getAllPatients() {
        try {
            System.out.println("into the service layer");
            return patientRepository.findAll();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Patient getAllPatientsById(Long id){
        try { Optional<Patient> patient = patientRepository.findAllById(id);
            return null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Patient createPatient(Patient newpatient){
        try {
            patientRepository.save(newpatient);
            return newpatient;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void deletePatient(Long id) {
        try {
            patientRepository.deleteById(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public Patient updatePatient(Long id, @RequestBody Patient newpatient){
        try { Optional<Patient> existingPatient = patientRepository.findAllById(id);
            if (existingPatient.isPresent())
            {
                Patient p = existingPatient.get();
                p.setName(newpatient.getName());
                p.setAge(newpatient.getAge());
                p.setDisease(newpatient.getDisease());
                patientRepository.save(p);
                return newpatient;}
            else { return null;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;

        }
    }





}
