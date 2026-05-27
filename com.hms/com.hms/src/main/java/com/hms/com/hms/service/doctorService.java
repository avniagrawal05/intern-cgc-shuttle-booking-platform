package com.hms.com.hms.service;


import com.hms.com.hms.models.doctor;
import com.hms.com.hms.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class doctorService {

    @Autowired
    public DoctorRepository DoctorRepository;

    public List<doctor> getAllDoctors() {
        try {
            System.out.println("into the service layer");
            return DoctorRepository.findAll();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public doctor getAllDoctorsById(Long id) {
        try { Optional<doctor> doctor = DoctorRepository.findAllById(id);
            return null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public doctor createDoctor(doctor newDoctor) {
        try {
            DoctorRepository.save(newDoctor);
            return newDoctor;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void deleteDoctor(Long id) {
        try {DoctorRepository.deleteById(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateDoctor(Long id, @RequestBody doctor newDoctor) {
        try { Optional<doctor> existingDoctor = DoctorRepository.findAllById(id);
            if(existingDoctor.isPresent()){
                doctor d = existingDoctor.get();
                d.setName(newDoctor.getName());
                d.setExperience(newDoctor.getExperience());
                d.setSpeciality(newDoctor.getSpeciality());
                DoctorRepository.save(d);

            }else{
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}


