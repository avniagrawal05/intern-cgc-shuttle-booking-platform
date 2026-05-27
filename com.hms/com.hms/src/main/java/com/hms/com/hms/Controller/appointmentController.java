package com.hms.com.hms.Controller;

import com.hms.com.hms.models.appointment;
import com.hms.com.hms.service.appointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointment")
public class appointmentController {

    @Autowired
    public appointmentService appointmentService;

    @GetMapping
    public List<appointment> getAllAppointments(){
        return appointmentService.getAllAppointments();
    }
    @PostMapping
    public appointment createAppointment(@RequestBody appointment newAppointment){
        return appointmentService.createAppointment(newAppointment);
    }

    @GetMapping("/{ie}")
    public appointment getAllAppointmentsById(@PathVariable Long id){
        return appointmentService.getAllAppointmentsById(id);
    }

    @DeleteMapping("/{ie}")
    public void deleteAppointment(@PathVariable Long id){
        appointmentService.deleteAppointment(id);

    }
    @PutMapping("/{ie}")
    public void updateAppointment(@PathVariable Long id, @RequestBody appointment newAppointment){
        appointmentService.updateAppointment(id, newAppointment);

    }


}
