package com.hms.com.hms.Controller;

import com.hms.com.hms.models.billing;

import com.hms.com.hms.service.billingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/billing")
public class billingController {

    @Autowired
    public billingService billingService;

    @GetMapping
    public List<billing> getAllBillings() {
        return billingService.getAllBillings();
    }

    @PostMapping
    public billing createBilling(@RequestBody billing newBillings) {
        return billingService.createBilling(newBillings);
    }

    @GetMapping("/{id}")
    public billing getAllBillingsById(@PathVariable Long id) {
        return billingService.getAllBillingsById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteBilling(@PathVariable Long id) {
        billingService.deleteBilling(id);

    }

    @PutMapping("/{id}")
    public void updateBilling(@PathVariable long id, @RequestBody billing newBillings) {
        billingService.updateBilling(id, newBillings);

    }
}
