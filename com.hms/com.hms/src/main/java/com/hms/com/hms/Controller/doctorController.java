package com.hms.com.hms.Controller;

import com.hms.com.hms.models.doctor;
import com.hms.com.hms.service.doctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctor")
public class doctorController {

    @Autowired
    private doctorService doctorService;

    @GetMapping
    public List<doctor> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @PostMapping
    public doctor createDoctor(@RequestBody doctor newDoctor) {
        return doctorService.createDoctor(newDoctor);
    }

    @GetMapping("/{id}")
    public doctor getAllDoctorsById(@PathVariable Long id) {
        return doctorService.getAllDoctorsById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);

    }

    @PutMapping("/{id}")
    public void updateDoctor(@PathVariable long id,@RequestBody doctor newDoctor) {
        doctorService.updateDoctor(id,newDoctor);

    }
}


