package com.hms.com.hms.service;

import com.hms.com.hms.models.appointment;
import com.hms.com.hms.models.Patient;
import com.hms.com.hms.models.billing;
import com.hms.com.hms.repository.appointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class appointmentService {

    @Autowired
    public appointmentRepository appointmentRepository;

    public List<appointment> getAllAppointments() {
        try {
            System.out.println("into the service layer");
            return appointmentRepository.findAll();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public appointment getAllAppointmentsById(Long id) {
        try {
            Optional<appointment> appointment = appointmentRepository.findById(id);
            return null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public appointment createAppointment(appointment newAppointment) {
        try {
            appointmentRepository.save(newAppointment);
            return newAppointment;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void deleteAppointment(Long id) {
        try {
            appointmentRepository.deleteById(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public Patient updateAppointment(Long id, @RequestBody appointment newAppointment) {
        try {
            Optional<appointment> existingAppointment = appointmentRepository.findById(id);
            if (existingAppointment.isPresent()) {
                appointment a = existingAppointment.get();
                a.setDate(newAppointment.getDate());
                a.setDoctor_name(newAppointment.getDoctor_name());
                a.setPatient_name(newAppointment.getPatient_name());

            } else {
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return null;

        }


        return null;
    }
}
