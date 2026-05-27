package com.hms.com.hms.service;


import com.hms.com.hms.models.billing;
import com.hms.com.hms.repository.billingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class billingService {

    @Autowired
    public billingRepository billingRepository;

    public List<billing> getAllBillings() {
        try {
            return billingRepository.findAll();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public billing getAllBillingsById(Long id) {
        try {
            Optional<billing> billing = billingRepository.findById(id);
            return null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public billing createBilling(billing newBillings) {
        try { billingRepository.save(newBillings);
            return newBillings;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void deleteBilling(Long id) {
        try { billingRepository.deleteById(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void updateBilling(Long id, @RequestBody billing newBillings) {
        try { Optional<billing> existingBilling = billingRepository.findById(id);
            if(existingBilling.isPresent()){
                billing b = existingBilling.get();
                b.setDate(newBillings.getDate());
                b.setStatus(newBillings.getStatus());
                b.setPatient_name(newBillings.getPatient_name());
                billingRepository.save(b);
            }else{}
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

