package com.hms.com.hms.Controller;


import com.hms.com.hms.models.Patient;
import com.hms.com.hms.service.patientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient")
public class patientController {

    @Autowired
    private patientService patientService;

    @GetMapping
    public List<Patient> getAllPatients(){
        return patientService.getAllPatients();
    }
    @PostMapping
    public Patient createPatient(@RequestBody Patient newpatient){
        return patientService.createPatient(newpatient);
    }

    @GetMapping("/{id}")
    public Patient getAllPatientsById(@PathVariable Long id){
        return patientService.getAllPatientsById(id);
    }

    @DeleteMapping("/{id}")
    public void deletePatient(@PathVariable Long id){
         patientService.deletePatient(id);

    }
    @PutMapping("/{id}")
    public void updatePatient(@PathVariable Long id, @RequestBody Patient newpatient){
        patientService.updatePatient(id,newpatient);

    }


}
