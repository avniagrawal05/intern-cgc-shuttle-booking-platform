package com.hms.com.hms.models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class billing {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String patient_name;
    private int date;
    private String status;



}
